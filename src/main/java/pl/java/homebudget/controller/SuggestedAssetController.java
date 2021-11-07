package pl.java.homebudget.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.dto.SuggestedAssetDto;
import pl.java.homebudget.service.SuggestedAssetService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/suggest")
public class SuggestedAssetController {

    private final SuggestedAssetService suggestedAssetService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SuggestedAssetDto> getAllSuggestedAssets(@RequestParam(required = false, defaultValue = "false") Boolean sold) {
        return suggestedAssetService.getAllSuggestedAssets(sold);
    }
}
