package khuend.project.crm.modules.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingInRequest {

   @NotBlank(message = "Username is required")
   @Size(max = 50, message = "Username must not exceed 50 characters")
   private String username;

   @NotBlank(message = "Password is required")
   @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
   private String password;
}
