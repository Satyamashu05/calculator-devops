package com.spe.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private final CalculatorService service = new CalculatorService();

    @Test
    void addsTwoNumbers() {
        assertEquals(5.0, service.add(2, 3));
        assertEquals(0.3, service.add(0.1, 0.2), 1e-9);
    }

    @Test
    void subtractsTwoNumbers() {
        assertEquals(-1.0, service.subtract(2, 3));
    }

    @Test
    void multipliesTwoNumbers() {
        assertEquals(-6.0, service.multiply(2, -3));
    }

    @Test
    void dividesTwoNumbers() {
        assertEquals(2.5, service.divide(5, 2));
    }

    @Test
    void divideByZeroThrows() {
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> service.divide(1, 0));
        assertEquals("Division by zero is not allowed", ex.getMessage());
    }
}
