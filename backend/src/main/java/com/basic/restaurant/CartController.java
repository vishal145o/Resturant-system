package com.basic.restaurant;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {
  private final CartService cartService;
  public CartController(CartService cartService) { this.cartService = cartService; }

  @GetMapping
  public ResponseEntity<?> get(@RequestParam(name = "customer") String customer) {
    try { return ResponseEntity.ok(cartService.getCart(customer)); }
    catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(new ApiError(e.getMessage())); }
  }

  @PostMapping("/items")
  public ResponseEntity<?> add(@RequestParam(name = "customer") String customer, @Valid @RequestBody AddToCartRequest req) {
    try { return ResponseEntity.ok(cartService.addToCart(customer, req)); }
    catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(new ApiError(e.getMessage())); }
    catch (java.util.NoSuchElementException e) { return ResponseEntity.status(404).body(new ApiError(e.getMessage())); }
  }

  @PutMapping("/items/{menuItemId}")
  public ResponseEntity<?> updateQty(@RequestParam(name = "customer") String customer,
                                     @PathVariable long menuItemId,
                                     @RequestParam(name = "quantity") int quantity) {
    try { return ResponseEntity.ok(cartService.updateQuantity(customer, menuItemId, quantity)); }
    catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(new ApiError(e.getMessage())); }
    catch (java.util.NoSuchElementException e) { return ResponseEntity.status(404).body(new ApiError(e.getMessage())); }
  }

  @DeleteMapping
  public ResponseEntity<?> clear(@RequestParam(name = "customer") String customer) {
    try { return ResponseEntity.ok(cartService.clearCart(customer)); }
    catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(new ApiError(e.getMessage())); }
  }
}
