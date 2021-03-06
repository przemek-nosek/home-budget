package pl.java.homebudget.service.downloader.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Service
@RequiredArgsConstructor
class DownloadPrepareService {

    public void prepareResponseToDownload(HttpServletResponse response, StringBuilder builder, String filename) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".csv");

        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            printWriter.println(builder);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while trying to create CSV file: " + e.getMessage());
        }
    }
}
