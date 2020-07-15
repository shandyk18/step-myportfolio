// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> validTimes = new ArrayList<>();  // return of all valid ranges
    List<TimeRange> invalidTimes = new ArrayList<>();  // keeps track of invalid ranges of mandatory attendees
    List<TimeRange> allInvalidTimes = new ArrayList<>();  // keeps track of invalid ranges of all attendees
    Set<String> attendees = new HashSet<>(request.getAttendees());  // keeps track of mandatory attendees
    Set<String> allAttendees = new HashSet<>(attendees);
    allAttendees.addAll(request.getOptionalAttendees());

    int duration = (int) request.getDuration();

    // if length of meeting is longer than a whole day, return empty list
    if (duration > TimeRange.WHOLE_DAY.duration()) {
        return validTimes;
    }

    // if no conflicting events
    if (events.isEmpty()) {
        validTimes.add(TimeRange.WHOLE_DAY);
        return validTimes;
    }

    for (Event event : events) {
        for (String attendee : event.getAttendees()) {
            if (attendees.contains(attendee)) {
                invalidTimes.add(event.getWhen());
            }
            if (allAttendees.contains(attendee)) {
                allInvalidTimes.add(event.getWhen());
            }
        }
    }

    Collections.sort(invalidTimes, TimeRange.ORDER_BY_START);  // sort by start time
    Collections.sort(allInvalidTimes, TimeRange.ORDER_BY_START);  // sort by start time

    // if no events for attendees
    if (allInvalidTimes.isEmpty()) {
        validTimes.add(TimeRange.WHOLE_DAY);
        return validTimes;
    }

    validTimes = findValidTimes(allInvalidTimes, duration);

    // look at mandatory attendees only if no valid times
    if (validTimes.isEmpty()) {
        return findValidTimes(invalidTimes, duration);
    }

    return validTimes;
  }

  /*
  * Find valid times from invalid events/times
  */
  private List<TimeRange> findValidTimes(List<TimeRange> invalidTimes, int duration) {
    List<TimeRange> validTimes = new ArrayList<>();
    boolean first = false;  // first event of the day
    int start = TimeRange.START_OF_DAY;  // current start of meeting
    int prevEnd = TimeRange.START_OF_DAY;

    for (int i = 0; i < invalidTimes.size(); i++) {
        TimeRange invalid = invalidTimes.get(i);

        // if an event exists where it takes up whole day
        if (invalid.duration() + 1 == TimeRange.WHOLE_DAY.duration()) {
            validTimes.clear();
            return validTimes;
        }

        // Add meeting time range from start to start of event
        if (start + duration <= invalid.start()) {
            validTimes.add(TimeRange.fromStartEnd(start, invalid.start(), false));
        }
        
        if (prevEnd < invalid.end()) {
            start = invalid.end();  // move start to end of event for next iteration
        }

        // if last event and duration does not go past end of day
        if (TimeRange.END_OF_DAY >= start + duration && (i == invalidTimes.size() - 1)) {
            validTimes.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true));
        }

        prevEnd = invalid.end();
    }

    return validTimes;
  }
}
