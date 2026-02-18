package com.basic.restaurant;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "http://localhost:5173")
public class MenuController {
  private final MenuService service;
  public MenuController(MenuService service) { this.service = service; }

  @GetMapping
  public List<MenuItem> list() { return service.getAll(); }

  @PostMapping
  public ResponseEntity<MenuItem> create(@Valid @RequestBody CreateMenuItemRequest req) {
    return ResponseEntity.ok(service.add(req));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody UpdateMenuItemRequest req) {
    return service.update(id, req)
      .<ResponseEntity<?>>map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.status(404).body(new ApiError("Menu item not found")));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable long id) {
    boolean ok = service.delete(id);
    if (!ok) return ResponseEntity.status(404).body(new ApiError("Menu item not found"));
    return ResponseEntity.noContent().build();
  }
}
