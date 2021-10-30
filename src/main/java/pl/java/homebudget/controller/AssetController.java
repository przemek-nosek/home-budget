package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.service.AssetService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/assets")
@AllArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping
    public ResponseEntity<List<AssetDto>> getAssets() {
        List<AssetDto> assets = assetService.getAssets();

        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<List<AssetDto>> getAssetsByCategory(@PathParam("category") String category) {
        List<AssetDto> assetsByCategory = assetService
                .getAssetsByCategory(AssetCategory.valueOf(category.toUpperCase()));

        return new ResponseEntity<>(assetsByCategory, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<AssetDto>> getFilteredAssets(@RequestParam Map<String, String> filters) {
        List<AssetDto> assetDtoList = assetService.getFilteredAssets(filters);

        return new ResponseEntity<>(assetDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AssetDto> addAsset(@Valid @RequestBody AssetDto assetDto) {
        AssetDto addedAsset = assetService.addAsset(assetDto);

        return new ResponseEntity<>(addedAsset, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAsset(@RequestBody AssetDto assetDto) {
        assetService.deleteAsset(assetDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteAssetsByAppUser() {
        assetService.deleteAssetsByAppUser();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssetById(@PathVariable("id") Long id) {
        assetService.deleteAssetById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<AssetDto> updateAsset(@RequestBody AssetDto assetDto) {
        AssetDto updatedAsset = assetService.updateAsset(assetDto);

        return new ResponseEntity<>(updatedAsset, HttpStatus.OK);
    }
}
