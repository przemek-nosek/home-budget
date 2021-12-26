package pl.java.homebudget.service;

import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.enums.AssetCategory;

import java.util.List;
import java.util.Map;

public interface AssetService {
    List<AssetDto> getAssets();

    AssetDto addAsset(AssetDto assetDto);

    void deleteAsset(AssetDto assetDto);

    AssetDto updateAsset(AssetDto assetDto);

    List<AssetDto> getAssetsByCategory(AssetCategory assetCategory);

    void deleteAssetsByAppUser();

    List<AssetDto> getFilteredAssets(Map<String, String> filters);

    List<AssetDto> getFilteredAssets(AppUser appUser, Map<String, String> filters);

    List<AssetDto> addAllAssets(List<AssetDto> assetDtos);
}
