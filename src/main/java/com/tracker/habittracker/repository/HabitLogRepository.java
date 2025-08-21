package com.tracker.habittracker.repository;

import com.tracker.habittracker.model.HabitLog;
import com.tracker.habittracker.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
    List<HabitLog> findByHabitAndDate(Habit habit, LocalDate date);
    List<HabitLog> findByHabitAndDateBetween(Habit habit, LocalDate start, LocalDate end);
    void deleteByHabit(Habit habit);
}
