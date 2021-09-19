package pl.java.homebudget.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationUserDto {
    private String username;
    private String password;
}
