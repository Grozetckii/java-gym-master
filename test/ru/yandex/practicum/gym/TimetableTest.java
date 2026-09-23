package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testAddNewTrainingSessionPassInvalidParam() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession trainingSessionWithoutDayOfWeek = new TrainingSession(group, coach, null, new TimeOfDay(13, 0));
        TrainingSession trainingSessionWithoutTimeOfDay = new TrainingSession(group, coach, DayOfWeek.MONDAY, null);

        NullPointerException trainingSessionNpe = assertThrows(NullPointerException.class, () -> timetable.addNewTrainingSession(null));
        assertEquals("trainingSession must not be null", trainingSessionNpe.getMessage());

        NullPointerException dayOfWeekNpe = assertThrows(NullPointerException.class, () -> timetable.addNewTrainingSession(trainingSessionWithoutDayOfWeek));
        assertEquals("dayOfWeek must not be null", dayOfWeekNpe.getMessage());

        NullPointerException timeOfDayNpe = assertThrows(NullPointerException.class, () -> timetable.addNewTrainingSession(trainingSessionWithoutTimeOfDay));
        assertEquals("timeOfDay must not be null", timeOfDayNpe.getMessage());
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(singleTrainingSession, mondaySessions.getFirst());
        //Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(mondayChildTrainingSession, mondaySessions.getFirst());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        assertEquals(thursdayChildTrainingSession, thursdaySessions.getFirst());
        assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1));
        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayPassInvalidParam() {
        Timetable timetable = new Timetable();

        NullPointerException dayOfWeekNpe = assertThrows(NullPointerException.class, () -> timetable.getTrainingSessionsForDay(null));
        assertEquals("dayOfWeek must not be null", dayOfWeekNpe.getMessage());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, mondaySessions.size());
        assertEquals(singleTrainingSession, mondaySessions.getFirst());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> mondayEmptySessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(mondayEmptySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTimePassInvalidParam() {
        Timetable timetable = new Timetable();

        NullPointerException dayOfWeekNpe = assertThrows(NullPointerException.class, () -> timetable.getTrainingSessionsForDayAndTime(null, null));
        assertEquals("dayOfWeek must not be null", dayOfWeekNpe.getMessage());

        NullPointerException timeOfDayNpe = assertThrows(NullPointerException.class, () -> timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, null));
        assertEquals("timeOfDay must not be null", timeOfDayNpe.getMessage());
    }

    @Test
    void testGetCountByCoachesEmptyTimeTable() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();
        assertEquals(0, countByCoaches.size());
    }

    @Test
    void testGetCountByCoachesNotEmptyTimeTable() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();
        assertEquals(1, countByCoaches.size());

        Coach returnedCoach = countByCoaches.getFirst().getCoach();
        assertEquals(coach, returnedCoach);

        long returnedCountTrainingsByCoach = countByCoaches.getFirst().getTrainingCount();
        assertEquals(1, returnedCountTrainingsByCoach);
    }

    @Test
    void testGetCountByCoachesNotEmptyTimeTableCheckOrder() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Николаевич", "Василий", "Сергеевич");
        Coach coach3 = new Coach("Сергеев", "Николай", "Васильевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Group groupAdultPro = new Group("Акробатика для взрослых (PRO)", Age.ADULT, 120);
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 45);
        Group groupChildPro = new Group("Акробатика для взрослых (PRO)", Age.CHILD, 90);

        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        TrainingSession thursdayAdultProTrainingSession = new TrainingSession(groupAdultPro, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(18, 0));
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach3,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach3,
                DayOfWeek.TUESDAY, new TimeOfDay(20, 0));
        TrainingSession fridayChildProTrainingSession = new TrainingSession(groupChildPro, coach3,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(thursdayAdultProTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(fridayChildProTrainingSession);

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();
        assertEquals(3, countByCoaches.size());

        Coach returnedCoachWithMostTrainingSessions = countByCoaches.getFirst().getCoach();
        assertEquals(coach3, returnedCoachWithMostTrainingSessions);

        long returnedCountTrainingsByCoachWithMostTrainingSessions = countByCoaches.getFirst().getTrainingCount();
        assertEquals(3, returnedCountTrainingsByCoachWithMostTrainingSessions);

        Coach returnedCoachWithFewestTrainingSessions = countByCoaches.getLast().getCoach();
        assertEquals(coach1, returnedCoachWithFewestTrainingSessions);

        long returnedCountTrainingsByCoachWithFewestTrainingSessions = countByCoaches.getLast().getTrainingCount();
        assertEquals(1, returnedCountTrainingsByCoachWithFewestTrainingSessions);
    }

}
