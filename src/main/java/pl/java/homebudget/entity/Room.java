package pl.java.homebudget.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.java.homebudget.enums.RoomType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Room extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private BigDecimal cost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        if (!super.equals(o)) return false;
        return getRoomType() == room.getRoomType() && Objects.equals(getCost(), room.getCost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRoomType(), getCost());
    }
}
