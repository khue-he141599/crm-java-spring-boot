package khuend.project.crm.modules.department.service;

import org.springframework.stereotype.Service;

import khuend.project.crm.model.entity.DepartmentEntity;
import khuend.project.crm.modules.department.dto.DepartmentResponse;
import khuend.project.crm.modules.department.mapper.DepartmentMapper;
import khuend.project.crm.modules.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implements department business logic and delegates persistence to repository.
 */
@Service
@RequiredArgsConstructor // Generate constructor with required arguments (final fields)
@Slf4j // Add logging support
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse findById(String code) {
        DepartmentEntity department = departmentRepository.findByCode(code).orElseThrow(() -> {
            log.warn("Department with code {} not found", code);
            return new RuntimeException("Department not found");
        });
        return DepartmentMapper.toResponse(department);
    }
}
