# CRM module structure for migration from NestJS to Spring Boot

Muc tieu: chuyen source NestJS (TypeORM) sang Spring Boot theo huong module-first, de tach nghiep vu ro rang va de scale.

## 1) Package structure de xuat

```text
src/main/java/khuend/project/crm
├── CrmApplication.java
├── shared
│   ├── config
│   ├── exception
│   ├── security
│   └── util
└── modules
    ├── users
    │   ├── controller
    │   ├── dto
    │   ├── entity
    │   ├── mapper
    │   ├── repository
    │   ├── service
    │   └── specification
    ├── customers
    │   ├── controller
    │   ├── dto
    │   ├── entity
    │   ├── mapper
    │   ├── repository
    │   ├── service
    │   └── specification
    ├── leads
    ├── deals
    ├── tasks
    └── auth
```

Ban da co san module `users` trong project. Cac module khac co the copy cung pattern.

## 2) Mapping tu NestJS sang Spring Boot

- `*.module.ts` -> package module (khong can class Module nhu NestJS)
- `*.controller.ts` -> `controller/*Controller.java`
- `*.service.ts` -> `service/*Service.java`
- `*.entity.ts` (TypeORM) -> `entity/*Entity.java` (JPA)
- `Repository<Entity>` TypeORM -> `repository/*Repository.java` (extends `JpaRepository`)
- `class-validator DTO` -> `dto/*Request.java`, `dto/*Response.java` + Jakarta Validation
- `guards/interceptors/filters` -> `shared/security`, `shared/exception`, `shared/config`
- query builder dynamic filter -> `specification/*Specification.java`

## 3) Convention naming

- Entity: `UserEntity`
- Repository: `UserRepository`
- Service: `UserService`
- Controller: `UserController`
- DTO:
  - `CreateUserRequest`
  - `UpdateUserRequest`
  - `UserResponse`

## 4) Luong migrate de nghi cho crm-users

1. Chuyen `user.entity.ts` sang `UserEntity` (JPA annotation).
2. Tao `UserRepository extends JpaRepository<UserEntity, UUID>`.
3. Chuyen business logic sang `UserService`.
4. Chuyen API route sang `UserController`.
5. Chuyen validation sang DTO request (`@NotBlank`, `@Email`, ...).
6. Tao mapper giua Entity <-> DTO.
7. Neu can filter search, them `UserSpecification`.

## 5) Muc tieu coupling

- Controller chi xu ly HTTP, khong chua business logic.
- Service chua business logic va transaction.
- Repository chi truy cap du lieu.
- Shared chua cross-cutting concerns, khong chua nghiep vu module.
