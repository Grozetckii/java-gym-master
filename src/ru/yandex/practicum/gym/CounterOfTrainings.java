package ru.yandex.practicum.gym;

import java.util.Objects;

public class CounterOfTrainings {

    private final Coach coach;
    private final long trainingCount;

    public CounterOfTrainings(Coach coach, long trainingCount) {
        this.coach = Objects.requireNonNull(coach, "coach must not be null");;
        this.trainingCount = trainingCount;
    }

    public Coach getCoach() {
        return coach;
    }

    public long getTrainingCount() {
        return trainingCount;
    }
}
