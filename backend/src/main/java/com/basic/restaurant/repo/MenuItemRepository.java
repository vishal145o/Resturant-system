package com.basic.restaurant.repo;

import com.basic.restaurant.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {}
