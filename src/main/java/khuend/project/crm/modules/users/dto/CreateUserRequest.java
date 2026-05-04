package khuend.project.crm.modules.users.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

   private String username;

   private String name;

   private String fullname;

   @Email
   @NotBlank
   private String email;

   @NotBlank
   @Pattern(regexp = "^(0[35789][0-9]{8})$", message = "Phone number is not a valid Vietnam phone number")
   private String phone;

   private String accountType;

   private String employeeNo;

   private UUID departmentId;

   private String businessRoleId;

   private Integer statusCode = 1;
}
