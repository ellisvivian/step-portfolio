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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Get the attributes of the requested meeting.
    Collection<String> requestedAttendees = request.getAttendees();
    int requestedDuration = (int) request.getDuration();

    // Get all the conflicting times based on the requested attendees and their planned events.
    Collection<TimeRange> conflictingEventTimeRanges = new HashSet<>();
    for (Event event : events) {
      Set<String> attendees = event.getAttendees();
      for (String requestedAttendee : requestedAttendees) {
        if (attendees.contains(requestedAttendee)) {
          conflictingEventTimeRanges.add(event.getWhen());
          break;
        }
      }
    }

    // Get all the times of the requested duration or longer that have no overlap with a conflicting time.
    Collection<TimeRange> freeTimes = new ArrayList<>();
    int startTime = TimeRange.START_OF_DAY;
    int duration = requestedDuration;
    while (startTime + duration <= TimeRange.END_OF_DAY + 1) {
      TimeRange requestEventTime = TimeRange.fromStartDuration(startTime, duration);
      boolean hasConflict = false;
      for (TimeRange conflictingEvent : conflictingEventTimeRanges) {
        if (requestEventTime.overlaps(conflictingEvent)) {
          hasConflict = true;
          int shortenedDuration = conflictingEvent.start() - startTime;
          if (shortenedDuration >= requestedDuration) {
            freeTimes.add(TimeRange.fromStartDuration(startTime, shortenedDuration));
          }
          startTime = conflictingEvent.end();
          duration = requestedDuration;
        }
      }
      if (!hasConflict) {
        if (startTime + duration == TimeRange.END_OF_DAY + 1) {
          freeTimes.add(requestEventTime);
        }
        duration += requestedDuration;
      }
    }
    return freeTimes;
  }
}
