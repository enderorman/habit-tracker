package com.tracker.habittracker.service;

import com.tracker.habittracker.dto.CreateHabitRequest;
import com.tracker.habittracker.model.Habit;
import com.tracker.habittracker.model.Frequency;
import com.tracker.habittracker.repository.HabitRepository;
import com.tracker.habittracker.repository.HabitLogRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;

    public HabitService(HabitRepository habitRepository, HabitLogRepository habitLogRepository) {
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
    }

    public List<Habit> getAllHabits() {
        return habitRepository.findAll();
    }

    public Habit getHabitById(Long id) {
        return habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
    }

    public Habit addHabit(CreateHabitRequest request) {
        Frequency frequency = Frequency.fromString(request.frequency);
        Habit habit = new Habit();
        habit.setName(request.name);
        habit.setDescription(request.description);
        habit.setFrequency(frequency);
        return habitRepository.save(habit);
    }

    @Transactional
    public void deleteHabit(Long id) {
        Habit habit = getHabitById(id);
        habitLogRepository.deleteByHabit(habit);
        habitRepository.delete(habit);
    }
}
