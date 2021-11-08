package pl.java.homebudget.service.downloader;

import pl.java.homebudget.enums.DownloadSetting;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface DownloadService {
    void addToResponse(Map<String, String> filters, HttpServletResponse response, DownloadSetting asset);
}
