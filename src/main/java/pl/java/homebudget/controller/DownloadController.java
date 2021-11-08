package pl.java.homebudget.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.enums.DownloadSetting;
import pl.java.homebudget.service.downloader.DownloadService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/download")
@RequiredArgsConstructor
public class DownloadController {

    private final DownloadService downloadService;


    @GetMapping("/assets")
    @ResponseStatus(HttpStatus.OK)
    public void downloadAssets(@RequestParam Map<String, String> filters, HttpServletResponse response) {
        downloadService.addToResponse(filters, response, DownloadSetting.ASSET);
    }

    @GetMapping("/expenses")
    @ResponseStatus(HttpStatus.OK)
    public void downloadExpenses(@RequestParam Map<String, String> filters, HttpServletResponse response) {
        downloadService.addToResponse(filters, response, DownloadSetting.EXPENSE);
    }
}
