package com.basic.restaurant;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items", uniqueConstraints = {
  @UniqueConstraint(name = "uk_cart_customer_menu", columnNames = {"customer_id", "menu_item_id"})
})
public class CartItemEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "menu_item_id", nullable = false)
  private MenuItem menuItem;

  @Column(nullable = false)
  private int quantity;

  public CartItemEntity() {}
  public CartItemEntity(Customer customer, MenuItem menuItem, int quantity) {
    this.customer = customer;
    this.menuItem = menuItem;
    this.quantity = quantity;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Customer getCustomer() { return customer; }
  public void setCustomer(Customer customer) { this.customer = customer; }

  public MenuItem getMenuItem() { return menuItem; }
  public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }

  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
}
