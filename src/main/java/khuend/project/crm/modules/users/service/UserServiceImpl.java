package khuend.project.crm.modules.users.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import khuend.project.crm.model.entity.UserEntity;
import khuend.project.crm.model.entity.UserEntity.UserStatus;
import khuend.project.crm.modules.users.dto.CreateUserRequest;
import khuend.project.crm.modules.users.dto.SignInResponse;
import khuend.project.crm.modules.users.dto.SingInRequest;
import khuend.project.crm.modules.users.dto.UserResponse;
import khuend.project.crm.modules.users.mapper.UserMapper;
import khuend.project.crm.modules.users.repository.DepartmentRepository;
import khuend.project.crm.modules.users.repository.UserRepository;
import khuend.project.crm.shared.exception.AppException;
import khuend.project.crm.shared.exception.ErrorCode;
import khuend.project.crm.shared.security.JwtTokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final JwtTokenService jwtTokenService;
   private final DepartmentRepository departmentRepository;

   @Override
   @Transactional(readOnly = true)
   public List<UserResponse> findAll() {
      return userRepository.findAll().stream().map(UserMapper::toResponse).toList();
   }

   @Override
   @Transactional(readOnly = true)
   public UserResponse findById(UUID id) {
      UserEntity entity = userRepository.findById(Objects.requireNonNull(id))
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
      return UserMapper.toResponse(entity);
   }

   @Override
   @Transactional
   public UserResponse create(CreateUserRequest request) {

      userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
         throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
      });

      userRepository.findByPhone(request.getPhone()).ifPresent(user -> {
         throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
      });

      if (request.getDepartmentId() != null) {
         boolean isDepartmentExist = departmentRepository.existsById(Objects.requireNonNull(request.getDepartmentId()));
         if (!isDepartmentExist) {
            throw new AppException(ErrorCode.DEPARTMENT_NOT_FOUND);
         }
      }

      if (request.getBusinessRoleId() != null) {
         // xử lý lấy từ iam service
      }

      UserEntity entity = new UserEntity();
      entity.setUsername(request.getUsername());
      entity.setName(request.getName());
      entity.setFullname(request.getFullname());
      entity.setEmail(request.getEmail());
      entity.setPhone(request.getPhone());
      entity.setAccountType(request.getAccountType());
      entity.setEmployeeNo(request.getEmployeeNo());
      entity.setIamUserId("local-" + request.getUsername());

      String password = "123456"; // default password
      String hashPassword = passwordEncoder.encode(password);
      entity.setPassword(hashPassword);

      return UserMapper.toResponse(userRepository.save(entity));
   }

   @Override
   public SignInResponse signIn(SingInRequest request) {
      UserEntity entity = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

      boolean isPasswordValid = passwordEncoder.matches(request.getPassword(), entity.getPassword());

      if (!isPasswordValid) {
         throw new AppException(ErrorCode.INVALID_CREDENTIALS);
      }

      UserStatus status = entity.getStatus();
      if (!UserStatus.ACTIVE.equals(status)) {
         throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
      }

      String accessToken = jwtTokenService.generateAccessToken(entity);
      String refreshToken = jwtTokenService.generateRefreshToken(entity);

      return new SignInResponse(accessToken, refreshToken, UserMapper.toResponse(entity));
   }
}
