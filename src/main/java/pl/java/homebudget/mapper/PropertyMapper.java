package pl.java.homebudget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.java.homebudget.dto.PropertyDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Property;

@Mapper
public interface PropertyMapper {

    @Mapping(source = "dto.id", target = "id")
    Property fromDtoToProperty(PropertyDto dto, AppUser appUser);

    @Mapping(target = "house")
    PropertyDto fromPropertyToDto(Property property);

}
