package khuend.project.crm.modules.department.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import khuend.project.crm.model.entity.DepartmentEntity;

/**
 * Provides database access for department entities.
 */
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, UUID> {

    @Query("SELECT d FROM DepartmentEntity d WHERE d.code = :code")
    Optional<DepartmentEntity> findByCode(String code);
}
