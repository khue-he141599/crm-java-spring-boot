package khuend.project.crm.modules.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

   @NotBlank
   private String username;

   private String name;

   private String fullname;

   @Email
   private String email;

   private String phone;

   private String accountType;

   private String employeeNo;
}
