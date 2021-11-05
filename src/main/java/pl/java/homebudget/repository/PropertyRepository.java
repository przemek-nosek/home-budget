package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Property;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findAllByAppUser(AppUser appUser);

    List<Property> findAllByAppUserAndSold(AppUser appUser, Boolean sold);

    void deleteAllByAppUser(AppUser appUser);

    boolean existsByIdAndAppUser(Long id, AppUser appUser);

    Optional<Property> findByIdAndAppUser(Long id, AppUser appUser);
}
