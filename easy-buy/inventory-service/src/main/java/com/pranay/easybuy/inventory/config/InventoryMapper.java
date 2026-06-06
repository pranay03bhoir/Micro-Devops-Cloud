package com.pranay.easybuy.inventory.config;

import java.util.List;

import org.mapstruct.Mapper;

import com.pranay.easybuy.inventory.domain.InventoryItem;
import com.pranay.easybuy.inventory.dto.InventoryResponse;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryItem toEntity(InventoryResponse inventoryResponse);

    InventoryResponse toResponse(InventoryItem inventoryItem);

    List<InventoryResponse> toResponseList(List<InventoryItem> inventoryItems);
}
