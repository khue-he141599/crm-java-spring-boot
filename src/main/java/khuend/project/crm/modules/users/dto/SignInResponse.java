package khuend.project.crm.modules.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
   private String accessToken;
   private String refreshToken;
   private UserResponse user;
}
