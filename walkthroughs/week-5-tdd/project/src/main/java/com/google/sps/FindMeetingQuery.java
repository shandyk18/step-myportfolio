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
import java.util.ArrayList;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> validTimes = new ArrayList<>();
    Collection<TimeRange> invalidTimes = new ArrayList<>();
    Collection<String> attendees = request.getAttendees();
    int duration = (int) request.getDuration();
    int start = TimeRange.START_OF_DAY;

    // if length of meeting is longer than a whole day, return empty list
    if (duration > TimeRange.WHOLE_DAY.duration()) {
        return new ArrayList<TimeRange>();
    }

    // if no conflicting events or no attendees
    if (attendees.isEmpty() || events.isEmpty()) {
        validTimes.add(TimeRange.WHOLE_DAY);
        return validTimes;
    }

    // Add to invalid times collection
    for (Event event : events) {
        invalidTimes.add(event.getWhen());
    }

    Collections.sort(busyTimeRanges, TimeRange.ORDER_BY_START);

    // find intersection between the events for easier view of invalid times
    // for loop for start times (int start) and check if:
      // start time and duration conflicts with start time of current invalid event
        // if it does, break and increment start time
      // if not, keep looping through all events and do same check
      // keep track of end time
        // have a variable for end time of first event, decrease end time if event overlaps with range
        // and still fits within duration
      // increment start time var by duration of range
      // add range to validTimes


    for (Event invalid : invalidTimes) {
    }

    return validTimes;
  }
}
