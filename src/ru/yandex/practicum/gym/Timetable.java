package ru.yandex.practicum.gym;

import java.util.*;
import java.util.stream.Collectors;

public class Timetable {

    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();
        Arrays.stream(DayOfWeek.values())
                .forEach(dayOfWeek -> timetable.put(dayOfWeek, new TreeMap<>()));
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        Objects.requireNonNull(trainingSession, "trainingSession must not be null");
        DayOfWeek dayOfWeek = Objects.requireNonNull(trainingSession.getDayOfWeek(), "dayOfWeek must not be null");
        TimeOfDay timeOfDay = Objects.requireNonNull(trainingSession.getTimeOfDay(), "timeOfDay must not be null");

        timetable.get(dayOfWeek)
                .computeIfAbsent(timeOfDay, k -> new ArrayList<>())
                .add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        Objects.requireNonNull(dayOfWeek, "dayOfWeek must not be null");

        return timetable.get(dayOfWeek)
                .values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Objects.requireNonNull(dayOfWeek, "dayOfWeek must not be null");
        Objects.requireNonNull(timeOfDay, "timeOfDay must not be null");

        List<TrainingSession> sessions = timetable.get(dayOfWeek).get(timeOfDay);
        return sessions == null ? List.of() : List.copyOf(sessions);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        return timetable.values()
                .stream()
                .flatMap(dayMap -> dayMap.values().stream())
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        TrainingSession::getCoach,
                        Collectors.counting()))
                .entrySet().stream()
                .map(e -> new CounterOfTrainings(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(CounterOfTrainings::getTrainingCount).reversed())
                .toList();
    }
}
