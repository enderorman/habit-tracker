package com.tracker.habittracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;              // Unique ID for each habit

    private String name;          // Name of habit, e.g., "Coding"

    private String description;   // Optional short description

    @Enumerated(EnumType.STRING)
    private Frequency frequency;     // DAILY, WEEKLY, MONTHLY

    // --- Constructors ---

    public Habit() {
        // JPA requires a default constructor
    }

    public Habit(Long id, String name, String description, Frequency frequency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }

    public Habit(String name, String description, Frequency frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }
}
