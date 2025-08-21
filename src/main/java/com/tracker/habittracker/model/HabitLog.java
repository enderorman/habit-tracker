package com.tracker.habittracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "habit_logs")
public class HabitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;

    private LocalDate date;  // Date the habit was completed

    public HabitLog() {}

    public HabitLog(Habit habit, LocalDate date) {
        this.habit = habit;
        this.date = date;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Habit getHabit() { return habit; }
    public void setHabit(Habit habit) { this.habit = habit; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
