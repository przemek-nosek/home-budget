package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.dto.RoomDto;
import pl.java.homebudget.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<RoomDto>> getRooms() {
        List<RoomDto> roomDtoList = roomService.getRooms();

        return new ResponseEntity<>(roomDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RoomDto> saveOrUpdateRoom(@RequestBody RoomDto roomDto) {
        RoomDto savedOrUpdatedRoom = roomService.saveOrUpdateRoom(roomDto);

        return new ResponseEntity<>(savedOrUpdatedRoom, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoomDto> makeRoomInactive(@PathVariable Long id) {
        RoomDto inactiveRoom = roomService.makeRoomInactive(id);

        return new ResponseEntity<>(inactiveRoom, HttpStatus.OK);
    }
}
