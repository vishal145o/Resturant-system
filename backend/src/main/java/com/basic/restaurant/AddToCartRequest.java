package com.basic.restaurant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddToCartRequest {
  @NotNull(message = "menuItemId is required")
  private Long menuItemId;

  @Min(value = 1, message = "quantity must be >= 1")
  private int quantity = 1;

  public Long getMenuItemId() { return menuItemId; }
  public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }

  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
}
