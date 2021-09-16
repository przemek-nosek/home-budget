package pl.java.homebudget.service;

import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.AssetEntity;
import pl.java.homebudget.mapper.AssetMapper;
import pl.java.homebudget.repository.AssetRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper =Mappers.getMapper(AssetMapper.class);

    public List<AssetDto> getAssets() {
        return assetRepository.findAll().stream()
                .map(assetMapper::fromAssetToDto)
                .collect(Collectors.toList());
    }
}
