package pl.java.homebudget.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDto {
    private Long id;

    private List<RoomDto> rooms;

    @NotNull(message = "Single is null.")
    private Boolean single;

    @NotEmpty(message = "City is empty.")
    @Size(min = 2, max = 40, message = "City length must be between {min} and {max} characters")
    private String city;

    @NotEmpty(message = "Post code is empty.")
    @Size(min = 2, max = 10, message = "Post length must be between {min} and {max} characters")
    private String postCode;

    @NotEmpty(message = "Street is empty.")
    @Size(min = 2, max = 40, message = "Street length must be between {min} and {max} characters")
    private String street;

    private String house;

    private Boolean sold = false;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertyDto that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getCity(), that.getCity()) && Objects.equals(getPostCode(), that.getPostCode()) &&
                Objects.equals(getStreet(), that.getStreet()) && Objects.equals(getHouse(), that.getHouse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCity(), getPostCode(), getStreet(), getHouse());
    }
}
