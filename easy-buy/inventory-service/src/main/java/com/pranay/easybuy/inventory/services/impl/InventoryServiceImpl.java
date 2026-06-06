package com.pranay.easybuy.inventory.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.pranay.easy_buy.commonservice.payload.ProductSnapshot;
import com.pranay.easybuy.inventory.client.ProductClient;
import com.pranay.easybuy.inventory.config.InventoryMapper;
import com.pranay.easybuy.inventory.domain.InventoryItem;
import com.pranay.easybuy.inventory.dto.AdjustStockRequest;
import com.pranay.easybuy.inventory.dto.CreateInventoryRequest;
import com.pranay.easybuy.inventory.dto.InventoryResponse;
import com.pranay.easybuy.inventory.dto.ReleaseStockRequest;
import com.pranay.easybuy.inventory.dto.ReserveStockRequest;
import com.pranay.easybuy.inventory.dto.UpdateInventoryRequest;
import com.pranay.easybuy.inventory.exceptions.BusinessRuleException;
import com.pranay.easybuy.inventory.exceptions.ResourceNotFoundException;
import com.pranay.easybuy.inventory.repository.InventoryItemRepository;
import com.pranay.easybuy.inventory.services.InventoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductClient productClient;

    @Override
    public InventoryResponse create(CreateInventoryRequest request) {
        // Check if product exists.
        ProductSnapshot productSnapshot = null;
        try {
            productSnapshot = productClient.getProductById(request.productId());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Product not found");
        }

        // business rules
        String sku = normalizeSku(request.sku());
        if (inventoryItemRepository.existsBySku(sku)) {
            throw new BusinessRuleException("Inventory already exixts for sku: " + sku);
        }
        // check if inventory exists by product or not.
        if (inventoryItemRepository.existsByProductId(request.productId())) {
            throw new BusinessRuleException("Inventory already exists for productId: " + request.productId());
        }
        InventoryItem item = new InventoryItem();
        item.setProductId(request.productId());
        item.setSku(sku);
        item.setProductName(trim(productSnapshot.title()));
        item.setWarehouseLocation(trim(request.warehouseLocation()));
        item.setAvailableQuantity(defaultZero(request.availableQuantity()));
        item.setReservedQuantity(defaultZero(request.reservedQuantity()));
        item.setReorderLevel(defaultZero(request.reorderLevel()));
        item.setActive(request.active() == null || request.active());

        return inventoryMapper.toResponse(inventoryItemRepository.save(item));

    }

    @Override
    public InventoryResponse update(Long id, UpdateInventoryRequest request) {
        InventoryItem item = findEntity(id);
        item.setProductName(trim(request.productName()));
        item.setWarehouseLocation(trim(request.warehouseLocation()));
        item.setReorderLevel(request.reorderLevel());
        item.setActive(request.active());
        return inventoryMapper.toResponse(inventoryItemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getById(Long id) {
        return inventoryMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getBySku(String sku) {
        return inventoryMapper.toResponse(inventoryItemRepository.findBySku(normalizeSku(sku))
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for sku: " + sku)));
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getByProductId(UUID productId) {
        return inventoryMapper.toResponse(inventoryItemRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for productId: " + productId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAll() {
        return inventoryMapper.toResponseList(inventoryItemRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getLowStock(int threshold) {
        return inventoryMapper.toResponseList(inventoryItemRepository
                .findByAvailableQuantityLessThanEqualAndActiveTrueOrderByAvailableQuantityAsc(threshold));
    }

    @Override
    public InventoryResponse adjustStock(Long id, AdjustStockRequest request) {
        InventoryItem item = findEntityForUpdate(id);
        int delta = request.quantityDelta();
        int nextAvailable = safeInt(item.getAvailableQuantity()) + delta;
        if (nextAvailable < 0) {
            throw new BusinessRuleException("Adjustment would make available quantity negative");
        }
        item.setAvailableQuantity(nextAvailable);
        item.setReasonToAdjustQuantity(trim(request.reason()));
        return inventoryMapper.toResponse(inventoryItemRepository.save(item));
    }

    @Override
    public InventoryResponse reserveStock(Long id, ReserveStockRequest request) {
        InventoryItem item = findEntityForUpdate(id);
        int quantity = request.quantity();
        int available = safeInt(item.getAvailableQuantity());
        if (available < quantity) {
            throw new BusinessRuleException("Insufficient available stock to reserve");
        }
        item.setAvailableQuantity(available - quantity);
        item.setReservedQuantity(safeInt(item.getReservedQuantity()) + quantity);
        return inventoryMapper.toResponse(inventoryItemRepository.save(item));
    }

    @Override
    public InventoryResponse releaseStock(Long id, ReleaseStockRequest request) {
        InventoryItem item = findEntityForUpdate(id);
        int quantity = request.quantity();
        int reserved = safeInt(item.getReservedQuantity());
        if (reserved < quantity) {
            throw new BusinessRuleException("Insufficient reserved stock to release");
        }
        item.setReservedQuantity(reserved - quantity);
        item.setAvailableQuantity(safeInt(item.getAvailableQuantity()) + quantity);
        return inventoryMapper.toResponse(inventoryItemRepository.save(item));
    }

    @Override
    public InventoryResponse reserveStockByProductId(UUID productId, ReserveStockRequest request) {
        InventoryItem item = inventoryItemRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for productId: " + productId));
        return reserve(item, request.quantity());
    }

    @Override
    public InventoryResponse releaseStockByProductId(UUID productId, ReleaseStockRequest request) {
        InventoryItem item = inventoryItemRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for productId: " + productId));
        return release(item, request.quantity());
    }

    @Override
    public void delete(Long id) {
        InventoryItem item = findEntity(id);
        inventoryItemRepository.delete(item);
    }

    private InventoryItem findEntity(Long id) {
        return inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for id: " + id));
    }

    private InventoryItem findEntityForUpdate(Long id) {
        return inventoryItemRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for id: " + id));
    }

    private InventoryResponse reserve(InventoryItem item, int quantity) {
        int available = safeInt(item.getAvailableQuantity());
        if (available < quantity) {
            throw new BusinessRuleException("Insufficient available stock to reserve");
        }
        item.setAvailableQuantity(available - quantity);
        item.setReservedQuantity(safeInt(item.getReservedQuantity()) + quantity);
        return inventoryMapper.toResponse(inventoryItemRepository.save(item));
    }

    private InventoryResponse release(InventoryItem item, int quantity) {
        int reserved = safeInt(item.getReservedQuantity());
        if (reserved < quantity) {
            throw new BusinessRuleException("Insufficient reserved stock to release");
        }
        item.setReservedQuantity(reserved - quantity);
        item.setAvailableQuantity(safeInt(item.getAvailableQuantity()) + quantity);
        return inventoryMapper.toResponse(inventoryItemRepository.save(item));
    }

    // checks for sku contains text:
    private String normalizeSku(String sku) {
        // sku normaize rules
        if (!StringUtils.hasText(sku)) {
            throw new BusinessRuleException("SKU is required");
        }
        // IPHONE-14-BLACK
        return sku.trim().toUpperCase();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

}
