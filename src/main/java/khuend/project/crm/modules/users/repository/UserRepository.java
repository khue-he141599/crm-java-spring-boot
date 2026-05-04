package khuend.project.crm.modules.users.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import khuend.project.crm.model.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    // 1. @Query — JPQL (dùng tên class/field Java)
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u WHERE u.phone = :phone")
    Optional<UserEntity> findByPhone(@Param("phone") String phone);

    // // 2. @Query — Native SQL thuần (dùng tên bảng/cột thật)
    // @Query(value = "SELECT * FROM crm.departments WHERE name = :name",
    // nativeQuery = true)
    // Optional<DepartmentEntity> findByNameNative(@Param("name") String name);

    // // 3. Derived method name — Spring tự sinh query từ tên method
    // Optional<DepartmentEntity> findByName(String name);
    // List<DepartmentEntity> findByNameContainingIgnoreCase(String keyword);
}
