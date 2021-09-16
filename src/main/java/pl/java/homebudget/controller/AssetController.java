package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
