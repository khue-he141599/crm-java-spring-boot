package khuend.project.crm.modules.users.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import khuend.project.crm.model.entity.UserEntity;
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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
   @Transactional(readOnly = true)
   public UserResponse getMe(UUID userId) {
      UserEntity entity = userRepository.findById(Objects.requireNonNull(userId))
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
      return UserMapper.toResponse(entity);
   }

   @Override
   @Transactional
   public UserResponse create(CreateUserRequest request) {
      log.info("[AUTH_FLOW][AUTH-01][CREATE_USER] Start create user username={} email={}", request.getUsername(), request.getEmail());

      userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
         log.warn("[AUTH_FLOW][AUTH-01][CREATE_USER] Email already exists email={}", request.getEmail());
         throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
      });

      userRepository.findByPhone(request.getPhone()).ifPresent(user -> {
         log.warn("[AUTH_FLOW][AUTH-01][CREATE_USER] Phone already exists phone={}", request.getPhone());
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

      UserEntity savedUser = userRepository.save(entity);
      log.info("[AUTH_FLOW][AUTH-02][CREATE_USER] Create success userId={} username={}", savedUser.getId(), savedUser.getUsername());
      return UserMapper.toResponse(savedUser);
   }

   @Override
   public SignInResponse signIn(SingInRequest request) {
      log.info("[AUTH_FLOW][AUTH-01][SIGNIN] Start sign-in username={}", request.getUsername());
      UserEntity entity = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

      boolean isPasswordValid = passwordEncoder.matches(request.getPassword(), entity.getPassword());

      if (!isPasswordValid) {
         log.warn("[AUTH_FLOW][AUTH-01][SIGNIN] Invalid password username={}", request.getUsername());
         throw new AppException(ErrorCode.INVALID_CREDENTIALS);
      }

      // UserStatus status = entity.getStatus();
      // if (!UserStatus.ACTIVE.equals(status)) {
      //    throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
      // }

      String accessToken = jwtTokenService.generateAccessToken(entity);
      String refreshToken = jwtTokenService.generateRefreshToken(entity);
      log.info("[AUTH_FLOW][AUTH-02][SIGNIN] Token issued userId={} username={}", entity.getId(), entity.getUsername());

      return new SignInResponse(accessToken, refreshToken, UserMapper.toResponse(entity), "Bearer");
   }
}
