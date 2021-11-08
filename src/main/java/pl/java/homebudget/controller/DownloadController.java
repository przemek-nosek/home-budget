package pl.java.homebudget.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.java.homebudget.enums.DownloadSetting;
import pl.java.homebudget.service.downloader.DownloadService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/download")
@RequiredArgsConstructor
public class DownloadController {

    private final DownloadService downloadService;

    @GetMapping("/assets")
    public void downloadAssets(HttpServletResponse response) {
        downloadService.addToResponse(response, DownloadSetting.ASSET);
    }

    @GetMapping("/expenses")
    public void downloadExpenses(HttpServletResponse response) {
        downloadService.addToResponse(response, DownloadSetting.EXPENSE);
    }
}
