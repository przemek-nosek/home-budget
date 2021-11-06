package pl.java.homebudget.mapper;

import org.mapstruct.*;
import pl.java.homebudget.dto.RoomDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Room;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RoomMapper {

    @Mapping(source = "dto.id", target = "id")
    Room fromRoomToDto(RoomDto dto, AppUser appUser);

    RoomDto fromRoomToDto(Room room);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appUser", ignore = true)
    Room updateRoomFromRoomDto(RoomDto roomDto, @MappingTarget Room room);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appUser", ignore = true)
    List<Room> updateRoomFromRoomDto(List<RoomDto> roomDto, @MappingTarget List<Room>  room);
}
