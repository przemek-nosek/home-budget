package pl.java.homebudget.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString
abstract class BaseEntity {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    private AppUser appUser;

    public BaseEntity(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getAppUser(), that.getAppUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAppUser());
    }
}
