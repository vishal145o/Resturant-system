package com.basic.restaurant;

import com.basic.restaurant.repo.CartItemRepository;
import com.basic.restaurant.repo.CustomerRepository;
import com.basic.restaurant.repo.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CartService {
  private final CustomerRepository customerRepo;
  private final MenuItemRepository menuRepo;
  private final CartItemRepository cartRepo;

  public CartService(CustomerRepository customerRepo, MenuItemRepository menuRepo, CartItemRepository cartRepo) {
    this.customerRepo = customerRepo;
    this.menuRepo = menuRepo;
    this.cartRepo = cartRepo;
  }

  private String normalize(String name) {
    return name == null ? "" : name.trim();
  }

  private Customer getOrCreateCustomer(String customerName) {
    String n = normalize(customerName);
    if (n.isBlank()) throw new IllegalArgumentException("customerName is required");
    return customerRepo.findByNameIgnoreCase(n).orElseGet(() -> customerRepo.save(new Customer(n)));
  }

  private CartSummary toSummary(String customerName, List<CartItemEntity> items) {
    List<CartItemDto> dto = items.stream().map(ci ->
      new CartItemDto(
        ci.getMenuItem().getId(),
        ci.getMenuItem().getName(),
        ci.getMenuItem().getPrice(),
        ci.getQuantity()
      )
    ).collect(Collectors.toList());
    int total = dto.stream().mapToInt(CartItemDto::getLineTotal).sum();
    return new CartSummary(customerName.trim(), dto, total);
  }

  @Transactional(readOnly = true)
  public CartSummary getCart(String customerName) {
    Customer c = getOrCreateCustomer(customerName);
    return toSummary(c.getName(), cartRepo.findByCustomer(c));
  }

  @Transactional
  public CartSummary addToCart(String customerName, AddToCartRequest req) {
    Customer c = getOrCreateCustomer(customerName);
    MenuItem mi = menuRepo.findById(req.getMenuItemId()).orElseThrow(() -> new NoSuchElementException("Menu item not found"));

    CartItemEntity item = cartRepo.findByCustomerAndMenuItem(c, mi).orElseGet(() -> new CartItemEntity(c, mi, 0));
    item.setQuantity(item.getQuantity() + req.getQuantity());
    cartRepo.save(item);

    return toSummary(c.getName(), cartRepo.findByCustomer(c));
  }

  @Transactional
  public CartSummary updateQuantity(String customerName, long menuItemId, int quantity) {
    Customer c = getOrCreateCustomer(customerName);
    MenuItem mi = menuRepo.findById(menuItemId).orElseThrow(() -> new NoSuchElementException("Menu item not found"));

    CartItemEntity item = cartRepo.findByCustomerAndMenuItem(c, mi).orElseThrow(() -> new NoSuchElementException("Cart item not found"));

    if (quantity <= 0) cartRepo.delete(item);
    else { item.setQuantity(quantity); cartRepo.save(item); }

    return toSummary(c.getName(), cartRepo.findByCustomer(c));
  }

  @Transactional
  public CartSummary clearCart(String customerName) {
    Customer c = getOrCreateCustomer(customerName);
    cartRepo.deleteByCustomer(c);
    return new CartSummary(c.getName(), List.of(), 0);
  }
}
