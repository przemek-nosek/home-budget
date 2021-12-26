package pl.java.homebudget.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdditionalUserDataDto {

    private Long id;

    @Email
    @NotEmpty
    private String email;
}
