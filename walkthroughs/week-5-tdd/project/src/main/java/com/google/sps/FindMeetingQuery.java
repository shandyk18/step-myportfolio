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
import java.util.Iterator;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> validTimes = new ArrayList<>();  // return of all valid ranges
    List<TimeRange> invalidTimes = new ArrayList<>();  // keeps track of invalid ranges of mandatory attendees
    List<TimeRange> allInvalidTimes = new ArrayList<>();  // keeps track of invalid ranges of all attendees
    Collection<String> attendees = request.getAttendees();  // keeps track of mandatory attendees
    Collection<String> allAttendees = new ArrayList<>(attendees);
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

    // Add to invalid times collection
    for (Event event : events) {
        for (String attendee : allAttendees) {
            if(event.getAttendees().contains(attendee)) {
                allInvalidTimes.add(event.getWhen());
            }
        }
        for(String attendee : attendees) {
            if(event.getAttendees().contains(attendee)) {
                invalidTimes.add(event.getWhen());
            }
        }
    }

    // if no events for attendees
    if (allInvalidTimes.isEmpty()) {
        validTimes.add(TimeRange.WHOLE_DAY);
        return validTimes;
    }

    invalidTimes = mergeOverlap(invalidTimes);
    allInvalidTimes = mergeOverlap(allInvalidTimes);
    validTimes = findValidTimes(allInvalidTimes, duration);

    // look at mandatory attendees only if no valid times
    if (validTimes.isEmpty()) {
        return findValidTimes(invalidTimes, duration);
    }

    return validTimes;
  }

  /*
  * Merges overlapping/nested events into larger ranges
  */
  private List<TimeRange> mergeOverlap(List<TimeRange> invalidTimes) {
    // sort in order for ease in finding overlapping events
    List<TimeRange> mergedTimes = new ArrayList<>(invalidTimes);
    Collections.sort(mergedTimes, TimeRange.ORDER_BY_START);  // sort by start time

    // finds overlapping invalid events 
    for (int i = 0; i < mergedTimes.size() - 1; i++) {
        TimeRange eventOne = mergedTimes.get(i);
        TimeRange eventTwo = mergedTimes.get(i+1);
        // if any event takes up whole day, no valid times available
        if (eventOne.duration() + 1 == TimeRange.WHOLE_DAY.duration() ||
            eventTwo.duration() + 1 == TimeRange.WHOLE_DAY.duration()) {
            mergedTimes.clear();
            mergedTimes.add(TimeRange.WHOLE_DAY);
        }
        // choose longer event in case of nesting
        if (eventOne.contains(eventTwo)) {
            mergedTimes.remove(eventTwo);
            continue;
        }
        if (eventTwo.contains(eventOne)) {
            mergedTimes.remove(eventOne);
            continue;
        }
        if (!eventOne.overlaps(mergedTimes.get(i+1))) {
            continue;
        }
        // if overlap, create new range
        TimeRange newRange = TimeRange.fromStartEnd(eventOne.start(), eventTwo.end(), false);
        mergedTimes.remove(eventOne);
        mergedTimes.remove(eventTwo);
        mergedTimes.add(i, newRange);
    }
    return mergedTimes;
  }

  /*
  * Find valid times from invalid events/times
  */
  private List<TimeRange> findValidTimes(List<TimeRange> invalidTimes, int duration) {
    List<TimeRange> validTimes = new ArrayList<>();
    boolean first = false;  // first event of the day
    int start = TimeRange.START_OF_DAY;  // current start of meeting

    for (int i = 0; i < invalidTimes.size(); i++) {
        TimeRange invalid = invalidTimes.get(i);

        // if first event at beginning of day, move start to end of event
        if (invalid.start() == TimeRange.START_OF_DAY && first) {
            start = invalid.end();
            first = false;
            continue;
        }
        // Add meeting time range from start to start of event
        if (start + duration <= invalid.start()) {
            validTimes.add(TimeRange.fromStartEnd(start, invalid.start(), false));
        }
        
        start = invalid.end();  // move start to end of event for next iteration

        // if last event and duration does not go past end of day
        if (TimeRange.END_OF_DAY >= start + duration && (i == invalidTimes.size() - 1)) {
            validTimes.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true));
        }
    }

    return validTimes;
  }
}
