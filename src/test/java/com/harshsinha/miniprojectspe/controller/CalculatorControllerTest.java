
package com.harshsinha.miniprojectspe.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAddTwoNumbers() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "2")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.a").value(2))
                .andExpect(jsonPath("$.b").value(3))
                .andExpect(jsonPath("$.result").value(5));
    }

    @Test
    void shouldReturnBadRequestWhenDividingByZero() throws Exception {
        mockMvc.perform(get("/api/v1/divide")
                        .param("a", "6")
                        .param("b", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Division by zero is not allowed"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/divide"));
    }

    @Test
    void shouldReturnBadRequestWhenParameterIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Missing required parameter: b"));
    }

    @Test
    void shouldReturnBadRequestWhenParameterIsNotNumeric()
            throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "abc")
                        .param("b", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Parameter 'a' must be a valid number"));
    }
}
