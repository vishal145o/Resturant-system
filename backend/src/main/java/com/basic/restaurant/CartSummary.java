package com.basic.restaurant;

import java.util.List;

public class CartSummary {
  private String customerName;
  private List<CartItemDto> items;
  private int totalAmount;

  public CartSummary() {}

  public CartSummary(String customerName, List<CartItemDto> items, int totalAmount) {
    this.customerName = customerName;
    this.items = items;
    this.totalAmount = totalAmount;
  }

  public String getCustomerName() { return customerName; }
  public void setCustomerName(String customerName) { this.customerName = customerName; }

  public List<CartItemDto> getItems() { return items; }
  public void setItems(List<CartItemDto> items) { this.items = items; }

  public int getTotalAmount() { return totalAmount; }
  public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
}
