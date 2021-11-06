package pl.java.homebudget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.java.homebudget.dto.PropertyDto;
import pl.java.homebudget.dto.RoomDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Property;
import pl.java.homebudget.entity.Room;

import java.util.List;

@Mapper
public interface PropertyMapper {

    @Mapping(source = "dto.id", target = "id")
    Property fromDtoToProperty(PropertyDto dto, AppUser appUser);

    @Mapping(target = "house")
    PropertyDto fromPropertyToDto(Property property);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appUser", ignore = true)
    @Mapping(target = "rooms", source = "rooms")
    Property updatePropertyFromDto(PropertyDto propertyDto, List<Room> rooms, @MappingTarget Property property);
}
