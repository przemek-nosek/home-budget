package pl.java.homebudget.dto;

import lombok.*;
import pl.java.homebudget.enums.RoomSize;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private Long id;
    private RoomSize roomSize;
    private BigDecimal cost;

    public RoomDto(RoomSize roomSize, BigDecimal cost) {
        this.roomSize = roomSize;
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomDto roomDto)) return false;
        return Objects.equals(getId(), roomDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
