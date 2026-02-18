package com.basic.restaurant;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "customers", uniqueConstraints = {
  @UniqueConstraint(name = "uk_customer_name", columnNames = {"name"})
})
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false, length = 100)
  private String name;

  public Customer() {}
  public Customer(String name) { this.name = name; }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
}
