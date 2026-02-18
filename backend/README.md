# Backend (Spring Boot + MySQL) - Student Level (DB persistence)

## 1) Create DB and User (MySQL 9.6)
Login as root and run:

```sql
CREATE DATABASE IF NOT EXISTS restaurant_db;

DROP USER IF EXISTS 'rm_user'@'localhost';
CREATE USER 'rm_user'@'localhost' IDENTIFIED BY 'rm_pass123';

GRANT ALL PRIVILEGES ON restaurant_db.* TO 'rm_user'@'localhost';
FLUSH PRIVILEGES;
```

## 2) Run Backend
```bash
cd backend
mvn spring-boot:run
```

Backend: http://localhost:8080

### APIs
Menu:
- GET    /api/menu
- POST   /api/menu
- PUT    /api/menu/{id}
- DELETE /api/menu/{id}

Cart (customer wise, stored in DB):
- GET    /api/cart?customer=Vishal
- POST   /api/cart/items?customer=Vishal   body: {"menuItemId":1,"quantity":2}
- PUT    /api/cart/items/{menuItemId}?customer=Vishal&quantity=3
- DELETE /api/cart?customer=Vishal


## If you get: Field 'created_at' doesn't have a default value
This happens when your `menu_items` table already exists and has `created_at NOT NULL` without a default.

### Option A (clean reset - easiest)
```sql
USE restaurant_db;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS menu_items;
```

Then run backend again. Hibernate will recreate tables.

### Option B (keep existing table)
```sql
USE restaurant_db;
ALTER TABLE menu_items
  MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
```
