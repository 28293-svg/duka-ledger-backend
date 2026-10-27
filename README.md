# duka-ledger-backend

- **Student:** Rugema Bill Gates
- **Student ID:** 28293
- **Project:** DukaLedger (INSY 8322 Web Technology and Internet, mid-term)

DukaLedger replaces the paper notebook that small shops use to track customer credit.
Spring Boot + Spring Data JPA + PostgreSQL.

## Steps to run the API

1. Install JDK 17+ and PostgreSQL.
2. Create the database: `CREATE DATABASE duka_ledger;` (pgAdmin or psql).
3. Open `src/main/resources/application.properties` and put your own PostgreSQL password.
4. Run: `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`).
5. The API is on `http://localhost:8080`. Tables are created by Hibernate (`ddl-auto=update`).
6. Import `duka-ledger-midterm.postman_collection.json` in Postman and run folder `0 Seed data` first, then folders A to F.
   (The seed goes through the save endpoints, so run it before any credit entry in the past gets blocked by D1 - or use `seed.sql` in pgAdmin.)
