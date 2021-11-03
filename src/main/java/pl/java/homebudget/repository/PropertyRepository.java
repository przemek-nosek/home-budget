package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Property;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findAllByAppUser(AppUser appUser);

    void deleteAllByAppUser(AppUser appUser);

    boolean existsByIdAndAppUser(Long id, AppUser appUser);
}
