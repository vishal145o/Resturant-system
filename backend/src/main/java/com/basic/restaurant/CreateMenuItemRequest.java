package com.basic.restaurant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateMenuItemRequest {
  @NotBlank(message = "name is required")
  private String name;

  @Min(value = 1, message = "price must be >= 1")
  private int price;

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public int getPrice() { return price; }
  public void setPrice(int price) { this.price = price; }
}
