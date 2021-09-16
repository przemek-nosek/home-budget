package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.service.AssetService;

import java.util.List;

@RestController
@RequestMapping("/assets")
@AllArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping
    public ResponseEntity<List<AssetDto>> getAssets() {
        List<AssetDto> assets = assetService.getAssets();

        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AssetDto> addAsset(@RequestBody AssetDto assetDto) {
        AssetDto addedAsset = assetService.addAsset(assetDto);

        return new ResponseEntity<>(addedAsset, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<AssetDto> deleteAsset(@RequestBody AssetDto assetDto) {
        assetService.deleteAsset(assetDto);

        return new ResponseEntity<>(assetDto, HttpStatus.OK);
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
