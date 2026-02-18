package com.basic.restaurant;

import com.basic.restaurant.repo.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
  private final MenuItemRepository repo;

  public MenuService(MenuItemRepository repo) {
    this.repo = repo;
  }

  public List<MenuItem> getAll() {
    return repo.findAll();
  }

  public Optional<MenuItem> findById(Long id) {
    return repo.findById(id);
  }

  public MenuItem add(CreateMenuItemRequest req) {
    MenuItem mi = new MenuItem(req.getName(), req.getPrice(), true);
    return repo.save(mi);
  }

  public Optional<MenuItem> update(long id, UpdateMenuItemRequest req) {
    return repo.findById(id).map(mi -> {
      mi.setName(req.getName());
      mi.setPrice(req.getPrice());
      mi.setAvailable(req.isAvailable());
      return repo.save(mi);
    });
  }

  public boolean delete(long id) {
    if (!repo.existsById(id)) return false;
    repo.deleteById(id);
    return true;
  }

  public long count() {
    return repo.count();
  }
}
