package com.pranay.easybuy.inventory.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pranay.easybuy.inventory.domain.InventoryItem;

import jakarta.persistence.LockModeType;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findBySku(String sku);

    Optional<InventoryItem> findByProductId(UUID productId);

    // find inventory by inventory item id
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventoryItem i where i.id = :id")
    Optional<InventoryItem> findByIdForUpdate(@Param("id") Long id);

    // find inventory by productid
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventoryItem i where i.productId = :productId")
    Optional<InventoryItem> findByProductIdForUpdate(@Param("productId") UUID productId);

    // query is create automatically
    boolean existsBySku(String sku);

    boolean existsByProductId(UUID productId);

    List<InventoryItem> findByActiveTrueOrderByProductNameAsc();

    // for to create custom finder methods
    List<InventoryItem> findByAvailableQuantityLessThanEqualAndActiveTrueOrderByAvailableQuantityAsc(int threshold);
}
