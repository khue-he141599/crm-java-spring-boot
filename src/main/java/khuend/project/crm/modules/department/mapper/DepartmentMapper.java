package khuend.project.crm.modules.department.mapper;

import khuend.project.crm.model.entity.DepartmentEntity;
import khuend.project.crm.modules.department.dto.DepartmentResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DepartmentMapper {

    private static DepartmentResponse toDepartmentResponse(DepartmentEntity departmentEntity) {
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
                departmentEntity.getParent(),
                departmentEntity.getTypes(),
                departmentEntity.getBusinessRoleId());
    }
}
