package pl.java.homebudget.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Property {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Integer rooms;

    @Column(nullable = false)
    private Boolean single;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postCode;

    @Column(nullable = false)
    private String street;

    private String house;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    private AppUser appUser;

    public Property(Integer rooms, Boolean single, String city, String postCode, String street, String house, AppUser appUser) {
        this.rooms = rooms;
        this.single = single;
        this.city = city;
        this.postCode = postCode;
        this.street = street;
        this.house = house;
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property property)) return false;
        return Objects.equals(getId(), property.getId()) && Objects.equals(getCity(), property.getCity()) && Objects.equals(getPostCode(), property.getPostCode()) &&
                Objects.equals(getStreet(), property.getStreet()) && Objects.equals(getHouse(), property.getHouse()) && Objects.equals(getAppUser(), property.getAppUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCity(), getPostCode(), getStreet(), getHouse(), getAppUser());
    }
}


