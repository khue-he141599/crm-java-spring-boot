package khuend.project.crm.modules.users.service;

import java.util.List;
import java.util.UUID;

import khuend.project.crm.modules.users.dto.CreateUserRequest;
import khuend.project.crm.modules.users.dto.SignInResponse;
import khuend.project.crm.modules.users.dto.SingInRequest;
import khuend.project.crm.modules.users.dto.UpdateUserRequest;
import khuend.project.crm.modules.users.dto.UserResponse;

public interface UserService { // Define the service interface for user-related operations

   SignInResponse signIn(SingInRequest request);

   // SignUpResponse signUp(SignUpRequest request);

   List<UserResponse> findAll();

   UserResponse findById(UUID id);

   UserResponse getMe(UUID userId);

   UserResponse create(CreateUserRequest request);

   UserResponse update(UUID id, UpdateUserRequest request);
}
