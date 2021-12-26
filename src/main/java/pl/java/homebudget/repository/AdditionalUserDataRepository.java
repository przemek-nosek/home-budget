package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AdditionalUserData;
import pl.java.homebudget.entity.AppUser;

@Repository
public interface AdditionalUserDataRepository extends JpaRepository<AdditionalUserData, Long> {

    AdditionalUserData findAllByAppUser(AppUser appUser);
}
