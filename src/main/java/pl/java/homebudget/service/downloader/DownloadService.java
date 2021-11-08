package pl.java.homebudget.service.downloader;

import pl.java.homebudget.enums.DownloadSetting;

import javax.servlet.http.HttpServletResponse;

public interface DownloadService {
    void addToResponse(HttpServletResponse response, DownloadSetting asset);
}
