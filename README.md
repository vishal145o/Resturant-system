# Restaurant Basic Student Project (MySQL DB + Add to Cart)

This is a simple, college-level project using basic concepts **with DB persistence**.

## Start Backend
1) Setup MySQL DB & user (see backend/README.md)
2) Run:
```bash
cd backend
mvn spring-boot:run
```

## Start Frontend
```bash
cd frontend
npm install
npm run dev
```

Open: http://localhost:5173

## Notes
- Uses Hibernate `ddl-auto=update` (no Flyway) to support MySQL 9.6.
- Cart is saved **customer-wise** in MySQL tables.
