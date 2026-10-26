
package com.harshsinha.miniprojectspe.dto;

import java.math.BigDecimal;

public record CalculatorResponse(
        String operation,
        BigDecimal a,
        BigDecimal b,
        BigDecimal result
) {
}
