package khuend.project.crm.modules.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingInRequest {
   
   @NotBlank
   private String username;

   @NotBlank
   private String password;
}
