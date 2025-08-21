package com.tracker.habittracker.service;

import com.tracker.habittracker.dto.CreateHabitRequest;
import com.tracker.habittracker.model.Habit;
import com.tracker.habittracker.model.Frequency;
import com.tracker.habittracker.repository.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private com.tracker.habittracker.repository.HabitLogRepository habitLogRepository;

    @InjectMocks
    private HabitService habitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test getAllHabits() with some habits
    @Test
    void testGetAllHabits() {
        Habit habit1 = new Habit(1L, "Coding", "Practice coding daily", Frequency.DAILY);
        Habit habit2 = new Habit(2L, "Exercise", "Go to gym", Frequency.WEEKLY);

        when(habitRepository.findAll()).thenReturn(Arrays.asList(habit1, habit2));

        List<Habit> habits = habitService.getAllHabits();

        assertEquals(2, habits.size());
        assertEquals("Coding", habits.get(0).getName());
        assertEquals("Exercise", habits.get(1).getName());
    }

    // Test getAllHabits() when repository is empty
    @Test
    void testGetAllHabitsReturnsEmptyList() {
        when(habitRepository.findAll()).thenReturn(Collections.emptyList());

        List<Habit> habits = habitService.getAllHabits();

        assertNotNull(habits);
        assertTrue(habits.isEmpty());
    }

    // Test addHabit() functionality
    @Test
    void testAddHabit() {
        CreateHabitRequest request = new CreateHabitRequest();
        request.name = "Reading";
        request.description = "Read a book";
        request.frequency = "daily";

        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName(request.name);
        habit.setDescription(request.description);
        habit.setFrequency(Frequency.DAILY);

        when(habitRepository.save(any(Habit.class))).thenReturn(habit);

        Habit savedHabit = habitService.addHabit(request);

        assertNotNull(savedHabit);
        assertEquals("Reading", savedHabit.getName());
        assertEquals("Read a book", savedHabit.getDescription());
        assertEquals(Frequency.DAILY, savedHabit.getFrequency());
    }

    // Test deleteHabit() functionality
    @Test
    void testDeleteHabit() {
        Long habitId = 5L;
        Habit habit = new Habit(habitId, "Test Habit", "Test Description", Frequency.DAILY);

        when(habitRepository.findById(habitId)).thenReturn(java.util.Optional.of(habit));
        doNothing().when(habitLogRepository).deleteByHabit(habit);
        doNothing().when(habitRepository).delete(habit);

        habitService.deleteHabit(habitId);

        verify(habitRepository, times(1)).findById(habitId);
        verify(habitLogRepository, times(1)).deleteByHabit(habit);
        verify(habitRepository, times(1)).delete(habit);
    }
}
