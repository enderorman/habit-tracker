package com.tracker.habittracker.service;

import com.tracker.habittracker.model.Habit;
import com.tracker.habittracker.model.HabitLog;
import com.tracker.habittracker.repository.HabitLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;

@Service
public class HabitLogService {

    private final HabitLogRepository habitLogRepository;

    public HabitLogService(HabitLogRepository habitLogRepository) {
        this.habitLogRepository = habitLogRepository;
    }

    public HabitLog markHabit(Habit habit) {
        return markHabitOnDate(habit, LocalDate.now());
    }

    public HabitLog markHabitOnDate(Habit habit, LocalDate date) {
        LocalDate start;
        LocalDate end;

        switch (habit.getFrequency()) {
            case DAILY:
                start = date;
                end = date;
                break;
            case WEEKLY:
                start = date.with(DayOfWeek.MONDAY);
                end = date.with(DayOfWeek.SUNDAY);
                break;
            case MONTHLY:
                start = date.withDayOfMonth(1);
                end = date.withDayOfMonth(date.lengthOfMonth());
                break;
            default:
                start = date;
                end = date;
        }

        List<HabitLog> existing = habitLogRepository.findByHabitAndDateBetween(habit, start, end);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }

        HabitLog log = new HabitLog(habit, date);
        return habitLogRepository.save(log);
    }

    public List<HabitLog> getHabitLogs(Habit habit, LocalDate start, LocalDate end) {
        return habitLogRepository.findByHabitAndDateBetween(habit, start, end);
    }

    public void unmarkHabitOnDate(Habit habit, LocalDate date) {
        LocalDate start;
        LocalDate end;

        switch (habit.getFrequency()) {
            case DAILY:
                start = date;
                end = date;
                break;
            case WEEKLY:
                start = date.with(DayOfWeek.MONDAY);
                end = date.with(DayOfWeek.SUNDAY);
                break;
            case MONTHLY:
                start = date.withDayOfMonth(1);
                end = date.withDayOfMonth(date.lengthOfMonth());
                break;
            default:
                start = date;
                end = date;
        }

        List<HabitLog> existing = habitLogRepository.findByHabitAndDateBetween(habit, start, end);
        if (!existing.isEmpty()) {
            habitLogRepository.deleteAll(existing);
        }
    }
}
