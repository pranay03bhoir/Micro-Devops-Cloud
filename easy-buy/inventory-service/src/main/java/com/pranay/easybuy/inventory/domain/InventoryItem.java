package com.pranay.easybuy.inventory.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventories", uniqueConstraints = {
        @UniqueConstraint(name = "uk_inventory_sku", columnNames = "sku"),
        @UniqueConstraint(name = "uk_inventory_product_id", columnNames = "productId")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID productId;

    @Column(nullable = false, length = 128, unique = true)
    private String sku;

    @Column(nullable = false, length = 200)
    private String productName;

    @Column(nullable = false, length = 120)
    private String warehouseLocation;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Column(nullable = false)
    private Integer reservedQuantity;

    // threshold--> refill--> 5
    @Column(nullable = false)
    private Integer reorderLevel;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private String reasonToAdjustQuantity;
    @Column(nullable = false)
    Integer totalQuantity;

    Boolean lowStock;

    // executed before saving the entity
    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
        if (availableQuantity == null) {
            availableQuantity = 0;
        }
        if (reservedQuantity == null) {
            reservedQuantity = 0;
        }
        if (reorderLevel == null) {
            reorderLevel = 0;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
