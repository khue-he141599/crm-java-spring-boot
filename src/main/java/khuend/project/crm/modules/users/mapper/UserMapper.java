package khuend.project.crm.modules.users.mapper;

import khuend.project.crm.model.entity.UserEntity;
import khuend.project.crm.modules.users.dto.UserResponse;

public final class UserMapper {

   private UserMapper() {
   }

   public static UserResponse toResponse(UserEntity entity) {
      return new UserResponse(
            entity.getId(),
            entity.getUsername(),
            entity.getName(),
            entity.getFullname(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt());
   }
}
