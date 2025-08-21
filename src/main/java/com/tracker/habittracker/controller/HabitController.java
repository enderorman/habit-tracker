package com.tracker.habittracker.controller;

import com.tracker.habittracker.dto.CreateHabitRequest;
import com.tracker.habittracker.model.Habit;
import com.tracker.habittracker.service.HabitService;
import com.tracker.habittracker.service.HabitLogService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;
    private final HabitLogService habitLogService;

    public HabitController(HabitService habitService, HabitLogService habitLogService) {
        this.habitService = habitService;
        this.habitLogService = habitLogService;
    }

    @GetMapping
    public List<Habit> getAllHabits() {
        return habitService.getAllHabits();
    }

    @PostMapping
    public Habit addHabit(@RequestBody CreateHabitRequest request) {
        return habitService.addHabit(request);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
    }

    @PostMapping("/{id}/mark")
    public void markHabit(@PathVariable Long id) {
        Habit habit = habitService.getHabitById(id);
        habitLogService.markHabit(habit);
    }

    @PostMapping("/{id}/markOn")
    public void markHabitOnDate(@PathVariable Long id, @RequestParam String date) {
        Habit habit = habitService.getHabitById(id);
        LocalDate target = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        habitLogService.markHabitOnDate(habit, target);
    }

    @DeleteMapping("/{id}/unmarkOn")
    public void unmarkHabitOnDate(@PathVariable Long id, @RequestParam String date) {
        Habit habit = habitService.getHabitById(id);
        LocalDate target = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        habitLogService.unmarkHabitOnDate(habit, target);
    }

    @GetMapping("/stats")
    public Map<String, Integer> getStats(@RequestParam int month, @RequestParam int year) {
        List<Habit> habits = habitService.getAllHabits();
        Map<String, Integer> stats = new HashMap<>();
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        for (Habit habit : habits) {
            int count = habitLogService.getHabitLogs(habit, start, end).size();
            stats.put(habit.getName(), count);
        }
        return stats;
    }

    @GetMapping("/{id}/logs")
    public List<?> getHabitLogs(@PathVariable Long id, @RequestParam String start, @RequestParam String end) {
        Habit habit = habitService.getHabitById(id);
        LocalDate s = LocalDate.parse(start, DateTimeFormatter.ISO_DATE);
        LocalDate e = LocalDate.parse(end, DateTimeFormatter.ISO_DATE);
        return habitLogService.getHabitLogs(habit, s, e);
    }

    @GetMapping("/stats/range")
    public Map<String, Object> getStatsForRange(@RequestParam String start, @RequestParam String end) {
        LocalDate s = LocalDate.parse(start, DateTimeFormatter.ISO_DATE);
        LocalDate e = LocalDate.parse(end, DateTimeFormatter.ISO_DATE);

        List<Habit> habits = habitService.getAllHabits();
        Map<String, Object> result = new HashMap<>();

        for (Habit habit : habits) {
            int completions = habitLogService.getHabitLogs(habit, s, e).size();

            long possible;
            switch (habit.getFrequency()) {
                case DAILY:
                    possible = e.toEpochDay() - s.toEpochDay() + 1;
                    break;
                case WEEKLY:
                    possible = Math.max(1, (int) Math.ceil((e.toEpochDay() - s.toEpochDay() + 1) / 7.0));
                    break;
                case MONTHLY:
                    possible = Math.max(1, (int) Math.ceil((e.getYear() - s.getYear()) * 12 + (e.getMonthValue() - s.getMonthValue()) + 1));
                    break;
                default:
                    possible = 0;
            }

            double rate = possible == 0 ? 0.0 : (double) completions / possible;
            Map<String, Object> stats = new HashMap<>();
            stats.put("completions", completions);
            stats.put("possible", possible);
            stats.put("rate", rate);
            result.put(habit.getName(), stats);
        }
        return result;
    }
}
