package pl.java.homebudget.dto;

import lombok.*;
import pl.java.homebudget.enums.RoomSize;

import java.math.BigDecimal;

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
}
