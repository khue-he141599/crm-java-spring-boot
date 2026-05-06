package khuend.project.crm.modules.department.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents response payload for department creation operations.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDepartmentResponse {
    private String id;
    private String code;
    private String name;
    private String status;
    private String organizationId;
    private String ownerId;
    private String parentId;
    private DepartmentResponse parent;
    private String[] types;
    private String businessRoleId;
}
