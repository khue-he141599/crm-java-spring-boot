package khuend.project.crm.modules.users.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import khuend.project.crm.modules.users.dto.CreateUserRequest;
import khuend.project.crm.modules.users.dto.SignInResponse;
import khuend.project.crm.modules.users.dto.SingInRequest;
import khuend.project.crm.modules.users.dto.UserResponse;
import khuend.project.crm.modules.users.service.UserService;
import khuend.project.crm.shared.security.AuthType;
import khuend.project.crm.shared.security.Guard;
import khuend.project.crm.shared.security.PublicEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Guard(AuthType.JWT)
    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.findAll();
    }

    @Guard(AuthType.ANY)
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @Guard(AuthType.ANY)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @Guard(AuthType.JWT)
    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return userService.getMe(userId);
    }

    @PublicEndpoint
    @PostMapping("/signin")
    public SignInResponse signIn(@Valid @RequestBody SingInRequest request) {
        return userService.signIn(request);
    }
}
