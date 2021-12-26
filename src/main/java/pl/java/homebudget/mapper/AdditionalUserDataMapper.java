package pl.java.homebudget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.java.homebudget.dto.AdditionalUserDataDto;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.AdditionalUserData;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;

@Mapper(componentModel = "spring")
public interface AdditionalUserDataMapper {

    @Mapping(source = "dto.id", target = "id")
    @Mapping(source = "appUser", target = "appUser")
    AdditionalUserData fromDtoToAdditionalUserData(AdditionalUserDataDto dto, AppUser appUser);

    @Mapping(target = "email")
    AdditionalUserDataDto fromAdditionalUserDataToDto(AdditionalUserData additionalUserData);
}
