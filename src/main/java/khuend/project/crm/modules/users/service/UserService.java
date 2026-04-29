package khuend.project.crm.modules.users.service;

import java.util.List;
import java.util.UUID;
import khuend.project.crm.modules.users.dto.CreateUserRequest;
import khuend.project.crm.modules.users.dto.UserResponse;

public interface UserService {

   List<UserResponse> findAll();

   UserResponse findById(UUID id);

   UserResponse create(CreateUserRequest request);
}
