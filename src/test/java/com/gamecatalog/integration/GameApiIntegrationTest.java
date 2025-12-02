package com.gamecatalog.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GameApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnGamesList_WithoutLogin() throws Exception {
        mockMvc.perform(get("/api/games?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(greaterThan(0))));
    }

    @Test
    void shouldReturnGameDetails_WithHeaderImage() throws Exception {
        mockMvc.perform(get("/api/games/411"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Act of Aggression"))
                .andExpect(jsonPath("$.headerImage").exists());
    }
    @Test
    void shouldSearchGamesByName() throws Exception {
        mockMvc.perform(get("/api/games?search=Act")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(containsString("Act")));
    }

    @Test
    void shouldSortGamesByPriceDesc() throws Exception {
        mockMvc.perform(get("/api/games?sort=price,desc&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].price").exists());
    }

    @Test
    void shouldReturn404_WhenGameDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/games/999999"))
                .andExpect(status().isNotFound());
    }
}