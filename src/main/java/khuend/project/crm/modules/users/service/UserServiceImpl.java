package khuend.project.crm.modules.users.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import khuend.project.crm.model.entity.UserEntity;
import khuend.project.crm.modules.users.dto.CreateUserRequest;
import khuend.project.crm.modules.users.dto.UserResponse;
import khuend.project.crm.modules.users.mapper.UserMapper;
import khuend.project.crm.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;

   @Override
   @Transactional(readOnly = true)
   public List<UserResponse> findAll() {
      return userRepository.findAll().stream().map(UserMapper::toResponse).toList();
   }

   @Override
   @Transactional(readOnly = true)
   public UserResponse findById(UUID id) {
      UserEntity entity = userRepository.findById(Objects.requireNonNull(id))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
      return UserMapper.toResponse(entity);
   }

   @Override
   @Transactional
   public UserResponse create(CreateUserRequest request) {
      userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
         throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
      });

      UserEntity entity = new UserEntity();
      entity.setUsername(request.getUsername());
      entity.setName(request.getName());
      entity.setFullname(request.getFullname());
      entity.setEmail(request.getEmail());
      entity.setPhone(request.getPhone());
      entity.setAccountType(request.getAccountType());
      entity.setEmployeeNo(request.getEmployeeNo());
      entity.setIamUserId("local-" + request.getUsername());

      return UserMapper.toResponse(userRepository.save(entity));
   }
}
