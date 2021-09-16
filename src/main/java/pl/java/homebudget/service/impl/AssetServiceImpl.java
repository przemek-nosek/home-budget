package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.AssetEntity;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.mapper.AssetMapper;
import pl.java.homebudget.repository.AssetRepository;
import pl.java.homebudget.service.AssetService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper = Mappers.getMapper(AssetMapper.class);

    @Override
    public List<AssetDto> getAssets() {
        log.info("Get all Assets");
        return assetRepository.findAll().stream()
                .map(assetMapper::fromAssetToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssetDto addAsset(AssetDto assetDto) {
        log.info("Add asset");
        log.debug("AssetDto details: " + assetDto);
        AssetEntity assetEntity = assetMapper.fromDtoToAsset(assetDto);

        AssetEntity savedAsset = assetRepository.save(assetEntity);
        log.info("Asset added");
        return assetMapper.fromAssetToDto(savedAsset);
    }

    @Override
    public void deleteAsset(AssetDto assetDto) {
        log.info("Delete Asset");
        log.debug("AssetDto details: " + assetDto);
        AssetEntity assetEntity = assetMapper.fromDtoToAsset(assetDto);

        assetRepository.delete(assetEntity);
        log.info("Asset deleted");
    }

    @Override
    public void deleteAssetById(Long id) {
        log.info("Delete Asset by ID");
        boolean existsById = assetRepository.existsById(id);

        if (!existsById) {
            throw new AssetNotFoundException(String.format("Asset with given id %d not found", id));
        }

        assetRepository.deleteById(id);
        log.info("Asset by ID deleted");
    }

    @Override
    public AssetDto updateAsset(AssetDto assetDto) {
        log.info("Update Asset");
        log.debug("AssetDto details: " + assetDto);
        Long id = assetDto.getId();

        AssetEntity assetToUpdate = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(String.format("Asset with given id %d not found", id)));

        if (Objects.nonNull(assetDto.getAmount())) {
            assetToUpdate.setAmount(assetDto.getAmount());
        }

        if (Objects.nonNull(assetDto.getCategory())) {
            assetToUpdate.setCategory(assetDto.getCategory());
        }

        if (Objects.nonNull(assetDto.getIncomeDate())) {
            assetToUpdate.setIncomeDate(assetDto.getIncomeDate());
        }

        assetRepository.saveAndFlush(assetToUpdate);

        log.info("Asset updated");

        return assetMapper.fromAssetToDto(assetToUpdate);
    }
}
