package khuend.project.crm.modules.users.dto;

import java.time.Instant;
import java.util.UUID;

import khuend.project.crm.model.entity.UserEntity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

   private UUID id;
   private String username;
   private String name;
   private String fullname;
   private String email;
   private String phone;
   private UserStatus status;
   private Instant createdAt;
   private Instant updatedAt;
}
