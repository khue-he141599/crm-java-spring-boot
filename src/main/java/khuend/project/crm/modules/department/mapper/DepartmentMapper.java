package khuend.project.crm.modules.department.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import khuend.project.crm.model.entity.DepartmentEntity;
import khuend.project.crm.modules.department.dto.DepartmentResponse;
import lombok.NoArgsConstructor;

/**
 * Maps department entities to response DTOs with cycle-safe parent recursion.
 */
@NoArgsConstructor
public class DepartmentMapper {

    // Start recursive mapping with an empty visited set for cycle protection.
    public static DepartmentResponse toResponse(DepartmentEntity departmentEntity) {
        return toResponse(departmentEntity, new HashSet<>());
    }

    private static DepartmentResponse toResponse(DepartmentEntity departmentEntity, Set<UUID> visited) {
        if (departmentEntity == null) {
            return null;
        }

        UUID currentId = departmentEntity.getId();
        // Stop mapping this branch if the same department appears again in parent
        // chain.
        if (currentId != null && !visited.add(currentId)) {
            return null;
        }

        return new DepartmentResponse(
                departmentEntity.getId(),
                departmentEntity.getCode(),
                departmentEntity.getName(),
                departmentEntity.getStatus(),
                departmentEntity.getCreatedAt(),
                departmentEntity.getUpdatedAt(),
                departmentEntity.getDeletedAt(),
                departmentEntity.getOrganizationId(),
                departmentEntity.getOwnerId(),
                toResponse(departmentEntity.getParent(), visited),
                departmentEntity.getTypes(),
                departmentEntity.getBusinessRoleId());
    }
}
