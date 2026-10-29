package com.spe.calculator;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CalculatorController {

    private final CalculatorService service;

    public CalculatorController(CalculatorService service) {
        this.service = service;
    }

    @GetMapping("/add")
    public CalculationResponse add(@RequestParam double a, @RequestParam double b) {
        return new CalculationResponse("add", a, b, service.add(a, b));
    }

    @GetMapping("/subtract")
    public CalculationResponse subtract(@RequestParam double a, @RequestParam double b) {
        return new CalculationResponse("subtract", a, b, service.subtract(a, b));
    }

    @GetMapping("/multiply")
    public CalculationResponse multiply(@RequestParam double a, @RequestParam double b) {
        return new CalculationResponse("multiply", a, b, service.multiply(a, b));
    }

    @GetMapping("/divide")
    public CalculationResponse divide(@RequestParam double a, @RequestParam double b) {
        return new CalculationResponse("divide", a, b, service.divide(a, b));
    }
}
