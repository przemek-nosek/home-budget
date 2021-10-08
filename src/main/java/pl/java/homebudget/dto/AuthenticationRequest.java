package pl.java.homebudget.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthenticationRequest {
    @Size(min = 3, max = 15, message = "Username should be between {min} and {max} characters.")
    private String username;
    @Size(min = 3, max = 15, message = "Password should be between {min} and {max} characters.")
    private String password;
}
