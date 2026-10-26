
package com.harshsinha.miniprojectspe.controller;

import com.harshsinha.miniprojectspe.dto.CalculatorResponse;
import com.harshsinha.miniprojectspe.service.CalculatorService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CalculatorResponse> add(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        BigDecimal result = calculatorService.add(a, b);

        return ResponseEntity.ok(
                new CalculatorResponse("add", a, b, result)
        );
    }

    @GetMapping("/subtract")
    public ResponseEntity<CalculatorResponse> subtract(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        BigDecimal result = calculatorService.subtract(a, b);

        return ResponseEntity.ok(
                new CalculatorResponse("subtract", a, b, result)
        );
    }

    @GetMapping("/multiply")
    public ResponseEntity<CalculatorResponse> multiply(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        BigDecimal result = calculatorService.multiply(a, b);

        return ResponseEntity.ok(
                new CalculatorResponse("multiply", a, b, result)
        );
    }

    @GetMapping("/divide")
    public ResponseEntity<CalculatorResponse> divide(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        BigDecimal result = calculatorService.divide(a, b);

        return ResponseEntity.ok(
                new CalculatorResponse("divide", a, b, result)
        );
    }
}
