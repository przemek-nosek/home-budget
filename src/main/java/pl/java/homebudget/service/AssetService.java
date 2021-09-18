package pl.java.homebudget.service;

import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.enums.AssetCategory;

import java.util.List;

public interface AssetService {
    List<AssetDto> getAssets();

    AssetDto addAsset(AssetDto assetDto);

    void deleteAsset(AssetDto assetDto);

    void deleteAssetById(Long id);

    AssetDto updateAsset(AssetDto assetDto);

    List<AssetDto> getAssetsByCategory(AssetCategory assetCategory);
}
