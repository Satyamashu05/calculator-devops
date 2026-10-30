package com.spe.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addReturnsJsonResult() throws Exception {
        mockMvc.perform(get("/api/v1/add").param("a", "2").param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.result").value(5.0));
    }

    @Test
    void subtractReturnsJsonResult() throws Exception {
        mockMvc.perform(get("/api/v1/subtract").param("a", "10").param("b", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(6.0));
    }

    @Test
    void multiplyReturnsJsonResult() throws Exception {
        mockMvc.perform(get("/api/v1/multiply").param("a", "4").param("b", "2.5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(10.0));
    }

    @Test
    void divideReturnsJsonResult() throws Exception {
        mockMvc.perform(get("/api/v1/divide").param("a", "9").param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(3.0));
    }

    @Test
    void divideByZeroReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/divide").param("a", "1").param("b", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Division by zero is not allowed"));
    }

    @Test
    void missingParameterReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/add").param("a", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("'b'")));
    }

    @Test
    void nonNumericParameterReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/add").param("a", "abc").param("b", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("'a'")));
    }

    @Test
    void healthEndpointIsUp() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
