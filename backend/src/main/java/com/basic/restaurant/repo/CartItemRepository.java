package com.basic.restaurant.repo;

import com.basic.restaurant.CartItemEntity;
import com.basic.restaurant.Customer;
import com.basic.restaurant.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
  List<CartItemEntity> findByCustomer(Customer customer);
  Optional<CartItemEntity> findByCustomerAndMenuItem(Customer customer, MenuItem menuItem);
  void deleteByCustomer(Customer customer);
}
