package pl.java.homebudget.service.uploader;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.service.AssetService;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UploadService {

    private final AssetService assetService;
    private final AssetParserService assetParserService;

    public void uploadFile(MultipartFile multipartFile) {

        try {
            byte[] bytes = multipartFile.getInputStream().readAllBytes();
            String s = new String(bytes);
            List<AssetDto> assetDtoList = assetParserService.parseToAsset(s);
            assetService.addAllAssets(assetDtoList);
            log.info("Saved assets from file: {}", assetDtoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
