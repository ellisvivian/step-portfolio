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
  /**
  * Returns the time ranges in which the requested meeting can be held without conflicting
  * with any of the attendees schedules (and the optional attendees, if such time ranges exist).
  */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> requestedAttendees = request.getAttendees();
    Collection<String> requestedOptionalAttendees = request.getOptionalAttendees();
    int requestedDuration = (int) request.getDuration();

    Collection<TimeRange> mandatoryConflictingEventTimeRanges = getConflictingEventTimeRanges(events, requestedAttendees);
    if (!requestedOptionalAttendees.isEmpty()) {
      Collection<TimeRange> allConflictingEventTimeRanges = getConflictingEventTimeRanges(events, requestedOptionalAttendees);
      allConflictingEventTimeRanges.addAll(mandatoryConflictingEventTimeRanges);
      Collection<TimeRange> allAvailableEventTimeRanges = getAvailableEventTimeRanges(allConflictingEventTimeRanges, 
        requestedDuration);
      if (!allAvailableEventTimeRanges.isEmpty() || requestedAttendees.isEmpty()) {
        return allAvailableEventTimeRanges;
      }
    }
    return getAvailableEventTimeRanges(mandatoryConflictingEventTimeRanges, requestedDuration);
  }

  /**
  * Returns the time ranges in which the the requested attendees already have events planned.
  */
  public Collection<TimeRange> getConflictingEventTimeRanges(Collection<Event> plannedEvents, 
      Collection<String> requestedAttendees) {
    Collection<TimeRange> conflictingEventTimeRanges = new HashSet<>();
    for (Event plannedEvent : plannedEvents) {
      Set<String> eventAttendees = plannedEvent.getAttendees();
      for (String requestedAttendee : requestedAttendees) {
        if (eventAttendees.contains(requestedAttendee)) {
          conflictingEventTimeRanges.add(plannedEvent.getWhen());
          break;
        }
      }
    }
    return conflictingEventTimeRanges;
  }

  /**
  * Returns the time ranges throughout the day that are greater than or equal to the requested duration
  * and do not overlap with the time ranges of any conflicting events.
  */
  public Collection<TimeRange> getAvailableEventTimeRanges(Collection<TimeRange> conflictingEventTimeRanges, int requestedDuration) {
    Collection<TimeRange> availableTimes = new ArrayList<>();
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
            availableTimes.add(TimeRange.fromStartDuration(startTime, shortenedDuration));
          }
          startTime = conflictingEvent.end();
          duration = requestedDuration;
        }
      }
      if (!hasConflict) {
        if (startTime + duration == TimeRange.END_OF_DAY + 1) {
          availableTimes.add(requestEventTime);
        }
        duration += requestedDuration;
      }
    }
    return availableTimes;
  }
}
