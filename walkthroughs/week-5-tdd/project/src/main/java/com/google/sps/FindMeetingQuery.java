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
    List<TimeRange> invalidTimes = new ArrayList<>();  // keeps track of invalid ranges
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

    Collections.sort(invalidTimes, TimeRange.ORDER_BY_START);

    // finds overlapping invalid events 
    for (int i = 0; i < invalidTimes.size() - 1; i++) {
        TimeRange eventOne = invalidTimes.get(i);
        if (invalidTimes.size() == 1 || !eventOne.overlaps(invalidTimes.get(i+1))) {
            continue;
        }

        TimeRange eventTwo = invalidTimes.get(i+1);
        TimeRange newRange = TimeRange.fromStartEnd(eventOne.start(), eventTwo.end(), false);
        invalidTimes.remove(eventOne);
        invalidTimes.remove(eventTwo);
        invalidTimes.add(i, newRange);
    }

    boolean first = true;

    // find any free meeting time for any event
    for (TimeRange invalid : invalidTimes) {
        // if first event is at the start of the day, move start position
        if (invalid.start() == TimeRange.START_OF_DAY && first) {
            start = invalid.end();
            first = false;
        }
        
        // check if meeting possible within duration
        if (start + duration <= invalid.start()) {
            validTimes.add(TimeRange.fromStartEnd(start, invalid.start(), false));
            start = invalid.end();
        }
        
        if (TimeRange.END_OF_DAY >= start + duration) {
            validTimes.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true));
        }
    }

    Iterator<TimeRange> itrValid = validTimes.iterator();
    Iterator<TimeRange> itrInvalid = invalidTimes.iterator();

    while (itrValid.hasNext()) {
        TimeRange valid = itrValid.next();
        while (itrInvalid.hasNext()) {
            TimeRange invalid = itrInvalid.next();
            System.out.println(invalid.toString());
            if (invalid.end() < valid.start()) {
                //itrInvalid.remove();
                continue;
            }
            if (valid.end() > invalid.start()) {
                System.out.println("HIIII!!!!");
                itrValid.remove();
                break;
            }
        }
    }

    return validTimes;
  }
}
