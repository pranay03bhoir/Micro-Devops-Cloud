package com.pranay.easybuy.inventory.dto;

import jakarta.validation.constraints.NotNull;

public record AdjustStockRequest(@NotNull Integer quantityDelta,
        String reason) {

}
