package com.tracker.habittracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.habittracker.dto.CreateHabitRequest;
import com.tracker.habittracker.model.Habit;
import com.tracker.habittracker.model.Frequency;
import com.tracker.habittracker.service.HabitLogService;
import com.tracker.habittracker.service.HabitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HabitController.class)
class HabitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HabitService habitService;

    @MockBean
    private HabitLogService habitLogService;

    @Autowired
    private ObjectMapper objectMapper;

    private Habit habit1;
    private Habit habit2;

    @BeforeEach
    void setUp() {
        habit1 = new Habit(1L, "Coding", "Practice coding daily", Frequency.DAILY);
        habit2 = new Habit(2L, "Exercise", "Go to gym", Frequency.WEEKLY);
    }

    // Test GET /api/habits
    @Test
    void testGetAllHabits() throws Exception {
        when(habitService.getAllHabits()).thenReturn(Arrays.asList(habit1, habit2));

        mockMvc.perform(get("/api/habits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Coding"))
                .andExpect(jsonPath("$[1].name").value("Exercise"));

        verify(habitService, times(1)).getAllHabits();
    }

    // Test POST /api/habits
    @Test
    void testAddHabit() throws Exception {
        CreateHabitRequest request = new CreateHabitRequest();
        request.name = "Reading";
        request.description = "Read books daily";
        request.frequency = "daily";

        Habit savedHabit = new Habit(3L, request.name, request.description, Frequency.fromString(request.frequency));

        when(habitService.addHabit(any(CreateHabitRequest.class))).thenReturn(savedHabit);

        mockMvc.perform(post("/api/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Reading"))
                .andExpect(jsonPath("$.description").value("Read books daily"))
                .andExpect(jsonPath("$.frequency").value("daily"));

        verify(habitService, times(1)).addHabit(any(CreateHabitRequest.class));
    }

    // Test DELETE /api/habits/{id}
    @Test
    void testDeleteHabit() throws Exception {
        Long habitId = 1L;
        doNothing().when(habitService).deleteHabit(habitId);

        mockMvc.perform(delete("/api/habits/{id}", habitId))
                .andExpect(status().isOk());

        verify(habitService, times(1)).deleteHabit(habitId);
    }
}
