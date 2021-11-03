package pl.java.homebudget.service;

import pl.java.homebudget.dto.RoomDto;

import java.util.List;

public interface RoomService {
    List<RoomDto> getRooms();

    RoomDto makeRoomInactive(Long id);

    RoomDto saveOrUpdateRoom(RoomDto roomDto);
}
