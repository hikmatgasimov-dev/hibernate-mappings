# Hibernate Mappings Practice

Small hands-on project for learning how **Hibernate ORM** maps Java objects to relational tables.
The same domain (`Manufacturer` and `Model`) is used for each relationship type, so the only thing
that changes between examples is the mapping itself.

## What it demonstrates

| Mapping | Rule in the example | Where the link is stored | Package | Status |
|---|---|---|---|---|
| One-to-Many / Many-to-One | one manufacturer has many models | foreign key `model.manufacturer_id` | `org.example` | done |
| One-to-One | one manufacturer has exactly one model | the same foreign key plus a `UNIQUE` constraint | `org.example.onetoone` | done |
| Many-to-Many | a model can belong to many manufacturers | a separate join table | `org.example.manytomany` | planned |

Each example shows:
- entity mapping (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`)
- relationship mapping (`@OneToMany`, `@ManyToOne`, `@OneToOne`, `@JoinColumn`, `mappedBy`)
- cascading (`CascadeType.ALL`) and `orphanRemoval`
- keeping both sides of a bidirectional relationship in sync (`addModel`, `setModel`)
- loading related data with HQL `join fetch`, and why lazy loading matters
- a database constraint (`UNIQUE`) rejecting an invalid second model in the One-to-One example

## Tech stack
- Java 21
- Hibernate ORM 6.4 (`jakarta.persistence`)
- MySQL 8 and `mysql-connector-j`
- Maven

## Project structure
```
src/main/java/org/example/
├── Main.java                 one-to-many demo
├── Manufacturer.java
├── Model.java
└── onetoone/
    ├── MainOneToOne.java     one-to-one demo
    ├── Manufacturer.java
    └── Model.java
src/main/resources/
└── hibernate.cfg.xml.example connection settings template
```

## Setup

### 1. Database
Run as a MySQL admin user:

```sql
CREATE DATABASE hb_one_to_many;
USE hb_one_to_many;
CREATE TABLE manufacturer (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100)
);
CREATE TABLE model (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  manufacturer_id INT,
  FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id)
);

CREATE DATABASE hb_one_to_one;
USE hb_one_to_one;
CREATE TABLE manufacturer (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100)
);
CREATE TABLE model (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  manufacturer_id INT UNIQUE,
  FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id)
);
```

Create a limited user instead of using `root`:

```sql
CREATE USER 'hb_user'@'localhost' IDENTIFIED BY 'choose-a-password';
GRANT ALL ON hb_one_to_many.* TO 'hb_user'@'localhost';
GRANT ALL ON hb_one_to_one.*  TO 'hb_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Configuration
Copy the template and fill in your own credentials:

```
copy src\main\resources\hibernate.cfg.xml.example src\main\resources\hibernate.cfg.xml
```

`hibernate.cfg.xml` is listed in `.gitignore`, so your real credentials are never committed.
The file name must be exactly `hibernate.cfg.xml` (no leading space), or Hibernate fails with
`Could not locate cfg.xml resource`.

### 3. Run
Open the file in IntelliJ IDEA and click the green run icon next to `main`:

| Class | Output to expect |
|---|---|
| `org.example.Main` | one `insert` into `manufacturer`, two into `model`, then the manufacturer and its models read back with a `join fetch` query |
| `org.example.onetoone.MainOneToOne` | one manufacturer with one model, then a rejected attempt to add a second model |

Each run inserts new rows. Clear the tables between runs if you want clean data (delete from `model` first, then `manufacturer`).

## Key ideas
- The **foreign key lives in the "many" table**. That side is the *owning side* (`@JoinColumn`).
- The other side uses `mappedBy`. It does not create a column.
- Hibernate writes the foreign key from the owning side's field, so helper methods
  (`addModel`, `setModel`) set both sides.
- One-to-One is the same foreign key as One-to-Many plus a `UNIQUE` constraint.
- `toString()` must not print the other side of a bidirectional link, or it recurses forever.
- `@OneToMany` is lazy by default: read the collection inside the session, or fetch it with `join fetch`.

## Roadmap
- [ ] Many-to-Many example with a join table
- [ ] Hibernate mini project using JPA with MySQL
- [ ] CRUD operations with HQL and pagination
- [ ] Move the same domain to Spring Data JPA