package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
}
