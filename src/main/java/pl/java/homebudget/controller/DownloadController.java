package pl.java.homebudget.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.enums.DownloadSetting;
import pl.java.homebudget.service.downloader.DownloadService;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("api/v1/download")
@RequiredArgsConstructor
public class DownloadController {

    private final DownloadService downloadService;

    @GetMapping("/assets")
    @ResponseStatus(HttpStatus.OK)
    public void downloadAssets(HttpServletResponse response) {
        downloadService.downloadFile(null, response, DownloadSetting.ASSET);
    }

    @GetMapping("/expenses")
    @ResponseStatus(HttpStatus.OK)
    public void downloadExpenses(HttpServletResponse response) {
        downloadService.downloadFile(null, response, DownloadSetting.EXPENSE);
    }


    @GetMapping("/assets/filter")
    @ResponseStatus(HttpStatus.OK)
    public void downloadFilteredAssets(@RequestParam Map<String, String> filters, HttpServletResponse response) {
        downloadService.downloadFile(filters, response, DownloadSetting.ASSET);
    }

    @GetMapping("/expenses/filter")
    @ResponseStatus(HttpStatus.OK)
    public void downloadFilteredExpenses(@RequestParam Map<String, String> filters, HttpServletResponse response) {
        downloadService.downloadFile(filters, response, DownloadSetting.EXPENSE);
    }
}
