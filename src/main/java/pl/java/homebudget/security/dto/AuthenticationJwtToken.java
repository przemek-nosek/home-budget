package pl.java.homebudget.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record AuthenticationJwtToken(String jwtToken) {
}
