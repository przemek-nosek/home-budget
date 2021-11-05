package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.java.homebudget.dto.RoomDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Room;
import pl.java.homebudget.enums.RoomSize;
import pl.java.homebudget.exception.RoomNotFoundException;
import pl.java.homebudget.service.RoomService;
import pl.java.homebudget.service.init.InitDataForIT;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoomServiceImplIT extends InitDataForIT {

    @Autowired
    private RoomService roomService;

    @Test
    void shouldGetAllRooms_whenEmpty_return_RoomSizeValuesWithPriceZero() {
        //given
        initDatabaseWithFirstUser();
        //when
        List<RoomDto> rooms = roomService.getRooms();

        //then
        assertThat(rooms).hasSize(6);
        rooms.forEach(roomDto -> assertThat(roomDto.getCost()).isEqualTo(BigDecimal.ZERO));
    }

    @Test
    void shouldGetAllRoomsInDatabase_whenExists() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        initDatabaseWithRoomAndUser(appUser);

        //when
        List<RoomDto> rooms = roomService.getRooms();

        //then
        assertThat(rooms).hasSize(1);
        RoomDto roomDto = rooms.get(0);
        assertThat(roomDto.getRoomSize()).isEqualTo(RoomSize.ROOM_XXL);
        assertThat(roomDto.getCost()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void shouldMakeRoomInactive_whenExists() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        initDatabaseWithRoomAndUser(appUser);
        Room room = roomRepository.findAllByAppUser(appUser).get(0);
        assertThat(room.getCost()).isEqualTo(BigDecimal.TEN);

        //when
        RoomDto roomDto = roomService.makeRoomInactive(room.getId());

        //then
        assertThat(roomDto.getCost()).isEqualTo(BigDecimal.ZERO);
        assertThat(roomDto.getRoomSize()).isEqualTo(room.getRoomSize());
    }

    @Test
    void shouldNotMakeRoomInactive_whenDoesNotExist_andThrowRoomNotFoundException() {
        //given
        initDatabaseWithFirstUser();
        Long notExistId = -15L;
        //when
        //then
        RoomNotFoundException ex = assertThrows(RoomNotFoundException.class, () -> roomService.makeRoomInactive(notExistId));
        assertThat(ex.getMessage()).isEqualTo(String.format("Room with given id %d not found", notExistId));
    }

    @Test
    void shouldSaveRoom_whenDoesNotExist() {
        //given
        initDatabaseWithFirstUser();
        RoomDto roomDto = new RoomDto(RoomSize.ROOM_XXL, BigDecimal.TEN);
        long repositoryCount = roomRepository.count();
        assertThat(repositoryCount).isZero();

        //when
        RoomDto savedRoom = roomService.saveOrUpdateRoom(roomDto);

        //then
        assertThat(roomRepository.count()).isEqualTo(1L);
        assertThat(savedRoom.getRoomSize()).isEqualTo(roomDto.getRoomSize());
        assertThat(savedRoom.getCost()).isEqualTo(roomDto.getCost());
    }

    @Test
    void shouldUpdateRoom_whenExists() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        initDatabaseWithRoomAndUser(appUser);
        Room room = roomRepository.findAllByAppUser(appUser).get(0);

        RoomDto roomDto = new RoomDto(room.getId(), RoomSize.ROOM_S, BigDecimal.TEN);
        long repositoryCount = roomRepository.count();
        assertThat(repositoryCount).isEqualTo(1L);

        //when
        RoomDto savedRoom = roomService.saveOrUpdateRoom(roomDto);

        //then
        assertThat(roomRepository.count()).isEqualTo(1L);
        assertThat(savedRoom.getRoomSize()).isEqualTo(RoomSize.ROOM_S);
        assertThat(savedRoom.getCost()).isEqualTo(roomDto.getCost());
    }
}
