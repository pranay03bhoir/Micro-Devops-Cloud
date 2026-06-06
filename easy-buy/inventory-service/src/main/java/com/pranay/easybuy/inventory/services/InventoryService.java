package com.pranay.easybuy.inventory.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pranay.easybuy.inventory.dto.AdjustStockRequest;
import com.pranay.easybuy.inventory.dto.CreateInventoryRequest;
import com.pranay.easybuy.inventory.dto.InventoryResponse;
import com.pranay.easybuy.inventory.dto.ReleaseStockRequest;
import com.pranay.easybuy.inventory.dto.ReserveStockRequest;
import com.pranay.easybuy.inventory.dto.UpdateInventoryRequest;

@Service
public interface InventoryService {
    // create the inventory
    InventoryResponse create(CreateInventoryRequest request);

    // update inventory
    InventoryResponse update(Long id, UpdateInventoryRequest request);

    InventoryResponse getById(Long id);

    InventoryResponse getBySku(String sku);

    InventoryResponse getByProductId(UUID productId);

    List<InventoryResponse> getAll();

    List<InventoryResponse> getLowStock(int threshold);

    InventoryResponse adjustStock(Long id, AdjustStockRequest request);

    InventoryResponse reserveStock(Long id, ReserveStockRequest request);

    InventoryResponse releaseStock(Long id, ReleaseStockRequest request);

    InventoryResponse reserveStockByProductId(UUID productId, ReserveStockRequest request);

    InventoryResponse releaseStockByProductId(UUID productId, ReleaseStockRequest request);

    void delete(Long id);
}
