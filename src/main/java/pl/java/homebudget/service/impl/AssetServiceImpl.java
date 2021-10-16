package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.enums.AssetCategory;
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
    private final UserLoggedInfo userLoggedInfo;

    @Override
    public List<AssetDto> getAssets() {
        log.info("Get all Assets");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return assetRepository.getAssetsByAppUser(loggedAppUser).stream()
                .map(assetMapper::fromAssetToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssetDto addAsset(AssetDto assetDto) {
        log.info("Add asset");
        log.debug("AssetDto details: {}", assetDto);

        AppUser loggedUser = userLoggedInfo.getLoggedAppUser();
        Asset asset = assetMapper.fromDtoToAsset(assetDto, loggedUser);
        asset.setAppUser(loggedUser);

        Asset savedAsset = assetRepository.save(asset);
        log.info("Asset added");
        return assetMapper.fromAssetToDto(savedAsset);
    }

    @Override
    public void deleteAsset(AssetDto assetDto) {
        log.info("Delete Asset");
        log.debug("AssetDto details: {}", assetDto);

        AppUser loggedUser = userLoggedInfo.getLoggedAppUser();
        Asset asset = assetMapper.fromDtoToAsset(assetDto,loggedUser);

        assetRepository.delete(asset);
        log.info("Asset deleted");
    }

    @Override
    @Transactional
    public void deleteAssetById(Long id) {
        log.info("Delete Asset by ID: {}", id);
        boolean existsById = assetRepository.existsById(id);

        if (!existsById) {
            throw new AssetNotFoundException(String.format("Asset with given id %d not found", id));
        }

        assetRepository.deleteById(id);
        log.info("Asset by ID deleted");
    }

    @Override
    @Transactional
    public AssetDto updateAsset(AssetDto assetDto) {
        log.info("Update Asset");
        log.debug("AssetDto details {} ", assetDto);
        Long id = assetDto.getId();

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        Asset assetToUpdate = assetRepository.findByIdAndAppUser(id, loggedAppUser)
                .orElseThrow(() -> new AssetNotFoundException(String.format("Asset with given id %d not found", id)));

        if (Objects.nonNull(assetDto.getAmount()) && !assetDto.getAmount().equals(assetToUpdate.getAmount())) {
            assetToUpdate.setAmount(assetDto.getAmount());
        }

        if (Objects.nonNull(assetDto.getCategory()) && !assetDto.getCategory().equals(assetToUpdate.getCategory())) {
            assetToUpdate.setCategory(assetDto.getCategory());
        }

        log.info("Asset updated");


        return assetMapper.fromAssetToDto(assetToUpdate);
    }

    @Override
    public List<AssetDto> getAssetsByCategory(AssetCategory assetCategory) {
        log.info("Getting Assets by category {}", assetCategory);
        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return assetRepository.getAssetEntitiesByCategoryAndAppUser(assetCategory, loggedAppUser)
                .stream()
                .map(assetMapper::fromAssetToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAssetsByAppUser() {
        AppUser loggedUser = userLoggedInfo.getLoggedAppUser();

        log.info("deleteAssetsByAppUser");

        assetRepository.deleteAllByAppUser(loggedUser);
    }
}
