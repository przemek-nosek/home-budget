package pl.java.homebudget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.java.homebudget.dto.RoomDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Room;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "dto.id", target = "id")
    Room fromDtoToRoom(RoomDto dto, AppUser appUser);

    RoomDto fromRoomToDto(Room room);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appUser", ignore = true)
    Room fromDtoToRoom(RoomDto roomDto, @MappingTarget Room room);
}
