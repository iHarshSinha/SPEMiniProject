
package com.harshsinha.miniprojectspe.controller;

import com.harshsinha.miniprojectspe.dto.CalculatorResponse;
import com.harshsinha.miniprojectspe.service.CalculatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/add")
    public CalculatorResponse add(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        return new CalculatorResponse(
                "add", a, b, calculatorService.add(a, b)
        );
    }

    @GetMapping("/subtract")
    public CalculatorResponse subtract(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        return new CalculatorResponse(
                "subtract", a, b, calculatorService.subtract(a, b)
        );
    }

    @GetMapping("/multiply")
    public CalculatorResponse multiply(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        return new CalculatorResponse(
                "multiply", a, b, calculatorService.multiply(a, b)
        );
    }

    @GetMapping("/divide")
    public CalculatorResponse divide(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        return new CalculatorResponse(
                "divide", a, b, calculatorService.divide(a, b)
        );
    }
}
