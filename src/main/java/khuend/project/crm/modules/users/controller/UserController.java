package khuend.project.crm.modules.users.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import khuend.project.crm.modules.users.dto.CreateUserRequest;
import khuend.project.crm.modules.users.dto.SignInResponse;
import khuend.project.crm.modules.users.dto.SingInRequest;
import khuend.project.crm.modules.users.dto.UpdateUserRequest;
import khuend.project.crm.modules.users.dto.UserResponse;
import khuend.project.crm.modules.users.service.UserService;
import khuend.project.crm.shared.security.AuthType;
import khuend.project.crm.shared.security.Guard;
import khuend.project.crm.shared.security.PublicEndpoint;
import lombok.RequiredArgsConstructor;

@Tag(name = "User", description = "User management APIs")
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Guard(AuthType.JWT)
    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.findAll();
    }

    @Operation(summary = "Get current authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current user info"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @Guard(AuthType.JWT)
    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return userService.getMe(userId);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @Guard(AuthType.ANY)
    @GetMapping("/{id}")
    public UserResponse getUserById(
            @Parameter(description = "User UUID", required = true) @PathVariable UUID id) {
        return userService.findById(id);
    }

    @Operation(summary = "Create a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Email or phone already exists")
    })
    @Guard(AuthType.ANY)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @Operation(summary = "Update user by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @Guard(AuthType.JWT)
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @Parameter(description = "User UUID", required = true) @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @Operation(summary = "Sign in and get access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sign in successful"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PublicEndpoint
    @PostMapping("/signin")
    public SignInResponse signIn(@Valid @RequestBody SingInRequest request) {
        return userService.signIn(request);
    }

}
