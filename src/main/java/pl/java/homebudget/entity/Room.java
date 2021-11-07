package pl.java.homebudget.entity;

import lombok.*;
import pl.java.homebudget.enums.RoomSize;

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
    private RoomSize roomSize;

    @Column(nullable = false)
    private BigDecimal cost;

    @Column(nullable = false)
    private Boolean rent = false;

    public Room(AppUser appUser, RoomSize roomSize, BigDecimal cost) {
        super(appUser);
        this.roomSize = roomSize;
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        if (!super.equals(o)) return false;
        return getRoomSize() == room.getRoomSize() && Objects.equals(getCost(), room.getCost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRoomSize(), getCost());
    }
}
