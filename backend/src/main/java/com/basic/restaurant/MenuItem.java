package com.basic.restaurant;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "menu_items")
public class MenuItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "name is required")
  @Column(nullable = false)
  private String name;

  @Min(value = 1, message = "price must be >= 1")
  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private boolean available = true;

  // Fix for DBs where menu_items already has a NOT NULL created_at column
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public MenuItem() {}
  public MenuItem(String name, int price, boolean available) {
    this.name = name;
    this.price = price;
    this.available = available;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public int getPrice() { return price; }
  public void setPrice(int price) { this.price = price; }

  public boolean isAvailable() { return available; }
  public void setAvailable(boolean available) { this.available = available; }

  public LocalDateTime getCreatedAt() { return createdAt; }
}
