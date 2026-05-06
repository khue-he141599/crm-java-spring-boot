package khuend.project.crm.modules.department.service;

import khuend.project.crm.modules.department.dto.DepartmentResponse;

/**
 * Defines department business operations exposed to controllers.
 */
public interface DepartmentService {
    DepartmentResponse findById(String id);
}
