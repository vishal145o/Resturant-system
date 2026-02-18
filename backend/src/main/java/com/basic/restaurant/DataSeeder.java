package com.basic.restaurant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
  private final MenuService menuService;
  public DataSeeder(MenuService menuService) { this.menuService = menuService; }

  @Override
  public void run(String... args) {
    if (menuService.count() == 0) {
      CreateMenuItemRequest a = new CreateMenuItemRequest(); a.setName("Pizza"); a.setPrice(199); menuService.add(a);
      CreateMenuItemRequest b = new CreateMenuItemRequest(); b.setName("Burger"); b.setPrice(149); menuService.add(b);
      CreateMenuItemRequest c = new CreateMenuItemRequest(); c.setName("Pasta"); c.setPrice(179); menuService.add(c);
    }
  }
}
