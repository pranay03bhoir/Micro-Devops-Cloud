package com.pranay.easybuy.products.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity{

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	private String title;
	@Column(columnDefinition = "TEXT")
	private String short_desc;
	@Column(columnDefinition = "TEXT")
	private String long_desc;
	private Double price;
	private Integer discount;
	private Boolean live = false;
	/**
	 * JPA annotation to map collections of basic types or embeddable objects to
	 * separate database tables. - Maps the productImages List<String> to a separate
	 * database table (typically named products_productImages) - Each string in the
	 * list becomes a separate row in that table - fetch = FetchType.EAGER means the
	 * collection is loaded immediately when the Products entity is loaded - Creates
	 * a join table with foreign key to the parent entity - The productImages field
	 * stores multiple image URLs as strings, and this annotation creates a separate
	 * table to store them with a one-to-many relationship to the products table.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> productImages = new ArrayList<>();

//    @ManyToOne
//    private Category category;

	@ManyToMany
	private List<Category> categories = new ArrayList<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews = new ArrayList<>();
}
