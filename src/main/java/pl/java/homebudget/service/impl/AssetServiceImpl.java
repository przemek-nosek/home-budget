package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.AssetEntity;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.mapper.AssetMapper;
import pl.java.homebudget.repository.AssetRepository;
import pl.java.homebudget.service.AssetService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper = Mappers.getMapper(AssetMapper.class);


    @Override
    public List<AssetDto> getAssets() {
        return assetRepository.findAll().stream()
                .map(assetMapper::fromAssetToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssetDto addAsset(AssetDto assetDto) {
        AssetEntity assetEntity = assetMapper.fromDtoToAsset(assetDto);

        AssetEntity savedAsset = assetRepository.save(assetEntity);

        return assetMapper.fromAssetToDto(savedAsset);
    }

    @Override
    public void deleteAsset(AssetDto assetDto) {
        AssetEntity assetEntity = assetMapper.fromDtoToAsset(assetDto);

        assetRepository.delete(assetEntity);
    }

    @Override
    public void deleteAssetById(Long id) {
        boolean existsById = assetRepository.existsById(id);

        if (!existsById) {
            throw new AssetNotFoundException(String.format("Asset with given id %d not found", id));
        }

        assetRepository.deleteById(id);
    }

    @Override
    public AssetDto updateAsset(AssetDto assetDto) { // TODO: VALID FIELDS AND UPDATE
        Long id = assetDto.getId();

        AssetEntity assetToUpdate = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(String.format("Asset with given id %d not found", id)));

        assetToUpdate.setAmount(assetDto.getAmount());

        assetRepository.saveAndFlush(assetToUpdate);

        return assetMapper.fromAssetToDto(assetToUpdate);
    }
}
