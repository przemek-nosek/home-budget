package pl.java.homebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Room;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByAppUser(AppUser appUser);

    Optional<Room> findByIdAndAppUser(Long id, AppUser appUser);
}
