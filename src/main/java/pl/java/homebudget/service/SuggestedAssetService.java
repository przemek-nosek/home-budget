package pl.java.homebudget.service;

import pl.java.homebudget.dto.SuggestedAssetDto;

import java.util.List;

public interface SuggestedAssetService {
    List<SuggestedAssetDto> getAllSuggestedAssets(boolean rent);
}
