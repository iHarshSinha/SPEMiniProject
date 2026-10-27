
package com.harshsinha.miniprojectspe.service;

import com.harshsinha.miniprojectspe.exception.DivisionByZeroException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorServiceTest {

    private final CalculatorService calculatorService =
            new CalculatorService();

    @Test
    void shouldAddTwoNumbers() {
        assertEquals(
                new BigDecimal("5"),
                calculatorService.add(
                        new BigDecimal("2"),
                        new BigDecimal("3")
                )
        );
    }

    @Test
    void shouldSubtractTwoNumbers() {
        assertEquals(
                new BigDecimal("2"),
                calculatorService.subtract(
                        new BigDecimal("5"),
                        new BigDecimal("3")
                )
        );
    }

    @Test
    void shouldMultiplyTwoNumbers() {
        assertEquals(
                new BigDecimal("6"),
                calculatorService.multiply(
                        new BigDecimal("2"),
                        new BigDecimal("3")
                )
        );
    }

    @Test
    void shouldDivideTwoNumbers() {
        assertEquals(
                new BigDecimal("2"),
                calculatorService.divide(
                        new BigDecimal("6"),
                        new BigDecimal("3")
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenDividingByZero() {
        assertThrows(
                DivisionByZeroException.class,
                () -> calculatorService.divide(
                        new BigDecimal("6"),
                        BigDecimal.ZERO
                )
        );
    }
    @Test
    void shouldReturnZeroWhenMultiplyingByZero() {
        assertEquals(
                BigDecimal.ZERO,
                calculatorService.multiply(
                        new BigDecimal("125"),
                        BigDecimal.ZERO
                )
        );
    }
    @Test
    void simpleMultiplication() {
        assertEquals(
                BigDecimal.TEN,
                calculatorService.multiply(
                        new BigDecimal("5"),
                        new BigDecimal("2")
                )
        );
    }
}
