package pl.java.homebudget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.AssetEntity;

@Mapper
public interface AssetMapper {

    @Mapping(target = "amount")
    AssetEntity fromDtoToAsset(AssetDto dto);

    @Mapping(target = "amount")
    AssetDto fromAssetToDto(AssetEntity asset);
}
