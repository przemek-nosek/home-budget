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
import pl.java.homebudget.filter.FilterRange;
import pl.java.homebudget.mapper.AssetMapper;
import pl.java.homebudget.repository.AssetRepository;
import pl.java.homebudget.service.AssetService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper = Mappers.getMapper(AssetMapper.class);
    private final UserLoggedInfo userLoggedInfo;
    private final FilterRange<Asset> assetFilterRange;

    @Override
    public List<AssetDto> getAssets() {
        log.info("Get all Assets");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return assetRepository.findAllByAppUser(loggedAppUser).stream()
                .map(assetMapper::fromAssetToDto)
                .toList();
    }

    @Override
    public AssetDto addAsset(AssetDto assetDto) {
        log.info("Add asset");
        log.debug("AssetDto details: {}", assetDto);

        AppUser loggedUser = userLoggedInfo.getLoggedAppUser();
        Asset asset = assetMapper.fromDtoToAsset(assetDto, loggedUser);

        Asset savedAsset = assetRepository.save(asset);
        log.info("Asset added");
        return assetMapper.fromAssetToDto(savedAsset);
    }

    @Override
    @Transactional
    public List<AssetDto> addAllAssets(List<AssetDto> assetDtos) {
        log.info("addAllAssets");
        log.debug("assetDtos {}", assetDtos);

        AppUser loggedUser = userLoggedInfo.getLoggedAppUser();

        List<Asset> assets = new ArrayList<>();

        assetDtos.forEach(assetDto -> {
            Asset asset = assetMapper.fromDtoToAsset(assetDto, loggedUser);
            asset.setAppUser(loggedUser);
            Asset savedAsset = assetRepository.save(asset);
            assets.add(savedAsset);
        });

        return assets.stream()
                .map(assetMapper::fromAssetToDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAsset(AssetDto assetDto) {
        log.info("Delete Asset");
        log.debug("AssetDto details: {}", assetDto);

        Long id = assetDto.getId();
        AppUser loggedUser = userLoggedInfo.getLoggedAppUser();

        boolean existsById = assetRepository.existsByIdAndAppUser(id, loggedUser);
        if (!existsById) {
            throw new AssetNotFoundException(String.format("Asset with given id %d not found", id));
        }

        Asset asset = assetMapper.fromDtoToAsset(assetDto, loggedUser);

        assetRepository.delete(asset);
        log.info("Asset deleted");
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

        if (Objects.nonNull(assetDto.getAmount())) {
            assetToUpdate.setAmount(assetDto.getAmount());
        }

        if (Objects.nonNull(assetDto.getCategory())) {
            assetToUpdate.setCategory(assetDto.getCategory());
        }

        log.info("Asset updated");


        return assetMapper.fromAssetToDto(assetToUpdate);
    }

    @Override
    public List<AssetDto> getAssetsByCategory(AssetCategory assetCategory) {
        log.info("Getting Assets by category {}", assetCategory);
        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return assetRepository.findAllByCategoryAndAppUser(assetCategory, loggedAppUser)
                .stream()
                .map(assetMapper::fromAssetToDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public void deleteAssetsByAppUser() {
        AppUser loggedUser = userLoggedInfo.getLoggedAppUser();

        log.info("deleteAssetsByAppUser");

        assetRepository.deleteAllByAppUser(loggedUser);
    }

    @Override
    public List<AssetDto> getFilteredAssets(Map<String, String> filters) {
        log.info("getFilteredExpenses");
        log.debug("filters {}", filters);

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return assetFilterRange.getAllByFilter(loggedAppUser, filters)
                .stream()
                .map(assetMapper::fromAssetToDto)
                .collect(toList());
    }


}
