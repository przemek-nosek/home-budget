package pl.java.homebudget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;

@Mapper
public interface AssetMapper {

    @Mapping(source = "dto.id", target = "id")
    Asset fromDtoToAsset(AssetDto dto, AppUser appUser);

    @Mapping(target = "amount")
    AssetDto fromAssetToDto(Asset asset);
}
