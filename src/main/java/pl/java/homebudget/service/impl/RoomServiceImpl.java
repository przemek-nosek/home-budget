package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.RoomDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Room;
import pl.java.homebudget.enums.RoomSize;
import pl.java.homebudget.exception.RoomNotFoundException;
import pl.java.homebudget.mapper.RoomMapper;
import pl.java.homebudget.repository.RoomRepository;
import pl.java.homebudget.service.RoomService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final UserLoggedInfo userLoggedInfo;

    @Override
    public List<RoomDto> getRooms() {
        log.info("getRooms");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        List<Room> roomList = roomRepository.findAllByAppUser(loggedAppUser);

        if (roomList.isEmpty()) {
            return Arrays.stream(RoomSize.values())
                    .map(roomSize -> new RoomDto(roomSize, BigDecimal.ZERO))
                    .toList();
        }

        return roomList.stream()
                .map(roomMapper::fromRoomToDto)
                .toList();
    }


    @Override
    @Transactional
    public RoomDto makeRoomInactive(Long id) {
        log.info("makeRoomInactive");
        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        Room room = roomRepository.findByIdAndAppUser(id, loggedAppUser)
                .orElseThrow(() -> new RoomNotFoundException(String.format("Room with given id %d not found", id)));

        room.setCost(BigDecimal.ZERO);

        return roomMapper.fromRoomToDto(room);
    }

    @Override
    @Transactional
    public RoomDto saveOrUpdateRoom(RoomDto roomDto) {
        log.info("saveOrUpdateRoom");
        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return Objects.isNull(roomDto.getId())
                ? saveRoom(roomDto, loggedAppUser)
                : updateRoom(roomDto, loggedAppUser);
    }

    private RoomDto saveRoom(RoomDto roomDto, AppUser loggedAppUser) {
        log.info("saveRoom: roomDto {}", roomDto);

        Room room = roomMapper.fromRoomToDto(roomDto, loggedAppUser);

        log.info("saved room {}", room);

        Room savedRoom = roomRepository.save(room);

        return roomMapper.fromRoomToDto(savedRoom);
    }

    private RoomDto updateRoom(RoomDto roomDto, AppUser loggedAppUser) {
        log.info("updateRoom: roomDto {}", roomDto);

        Long id = roomDto.getId();

        Room room = roomRepository.findByIdAndAppUser(id, loggedAppUser)
                .orElseThrow(() -> new RoomNotFoundException(String.format("Room with given id %d not found", id)));


        Room updatedRoom = roomMapper.updateRoomFromRoomDto(roomDto, room);

        return roomMapper.fromRoomToDto(updatedRoom);
    }
}
