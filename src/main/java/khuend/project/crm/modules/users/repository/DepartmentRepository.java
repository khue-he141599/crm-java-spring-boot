package khuend.project.crm.modules.users.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import feign.Param;
import khuend.project.crm.model.entity.DepartmentEntity;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, UUID> {

    @Override
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DepartmentEntity d WHERE d.id = :id")
    public abstract boolean existsById(@Param("id") @NonNull UUID id);

}
