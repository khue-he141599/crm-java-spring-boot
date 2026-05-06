package khuend.project.crm.modules.department.dto;

import java.time.Instant;
import java.util.UUID;

import khuend.project.crm.model.entity.DepartmentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // Add no-args constructor for deserialization
@AllArgsConstructor // Add all-args constructor for easy instantiation
public class DepartmentResponse {
    private UUID id;
    private String code;
    private String name;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private String organizationId;
    private String ownerId;
    private DepartmentEntity parent;
    private String[] types;
    private String businessRoleId;
}
