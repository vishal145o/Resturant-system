package com.basic.restaurant;

public class CartItemDto {
  private Long menuItemId;
  private String menuItemName;
  private int unitPrice;
  private int quantity;
  private int lineTotal;

  public CartItemDto() {}

  public CartItemDto(Long menuItemId, String menuItemName, int unitPrice, int quantity) {
    this.menuItemId = menuItemId;
    this.menuItemName = menuItemName;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
    this.lineTotal = unitPrice * quantity;
  }

  public Long getMenuItemId() { return menuItemId; }
  public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }

  public String getMenuItemName() { return menuItemName; }
  public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }

  public int getUnitPrice() { return unitPrice; }
  public void setUnitPrice(int unitPrice) { this.unitPrice = unitPrice; }

  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }

  public int getLineTotal() { return lineTotal; }
  public void setLineTotal(int lineTotal) { this.lineTotal = lineTotal; }
}
