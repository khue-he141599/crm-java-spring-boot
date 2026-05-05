package khuend.project.crm.modules.users.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

   @NotBlank(message = "Username is required")
   @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
   private String username;

   @Size(max = 100, message = "Name must not exceed 100 characters")
   private String name;

   @Size(max = 200, message = "Full name must not exceed 200 characters")
   private String fullname;

   @NotBlank(message = "Email is required")
   @Email(message = "Email is not valid")
   @Size(max = 255, message = "Email must not exceed 255 characters")
   private String email;

   @NotBlank(message = "Phone number is required")
   @Pattern(regexp = "^(0[35789][0-9]{8})$", message = "Phone number is not a valid Vietnam phone number")
   private String phone;

   @Size(max = 50, message = "Account type must not exceed 50 characters")
   private String accountType;

   @Size(max = 50, message = "Employee number must not exceed 50 characters")
   private String employeeNo;

   private UUID departmentId;

   @Size(max = 100, message = "Business role ID must not exceed 100 characters")
   private String businessRoleId;

   @Min(value = 0, message = "Status code must be >= 0")
   @Max(value = 9, message = "Status code must be <= 9")
   private Integer statusCode = 1;
}
