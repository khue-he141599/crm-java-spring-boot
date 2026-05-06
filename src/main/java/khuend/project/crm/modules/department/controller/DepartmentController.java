package khuend.project.crm.modules.department.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import khuend.project.crm.shared.security.AuthType;
import khuend.project.crm.shared.security.Guard;
import lombok.RequiredArgsConstructor;

/**
 * Exposes HTTP endpoints for department-related operations.
 */
@Tag(name = "Department", description = "Department management APIs")
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Validated
public class DepartmentController {

    @Operation(summary = "Get method name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Method name retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @Guard(AuthType.JWT)
    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

}
