package shop.alten.shop.Product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product code is required")
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    private String description;
    private String image;
    private String category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(nullable = false)
    private Integer quantity;

    private String internalReference;
    private Long shellId;

    @NotNull(message = "Inventory status is required")
    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;

    private Double rating;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public enum InventoryStatus {
        INSTOCK, LOWSTOCK, OUTOFSTOCK
    }
}
