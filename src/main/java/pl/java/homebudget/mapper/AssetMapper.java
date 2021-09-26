package pl.java.homebudget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.Asset;

@Mapper
public interface AssetMapper {

    @Mapping(target = "amount")
    Asset fromDtoToAsset(AssetDto dto);

    @Mapping(target = "amount")
    AssetDto fromAssetToDto(Asset asset);
}
