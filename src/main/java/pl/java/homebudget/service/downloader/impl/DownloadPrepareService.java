package pl.java.homebudget.service.downloader.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
class DownloadPrepareService {

    public void prepareToDownload(HttpServletResponse response, StringBuilder builder, String filename) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename="+filename+".csv");

        try (CSVPrinter printer = new CSVPrinter(response.getWriter(), CSVFormat.DEFAULT)) {

            String[] content = builder.toString().split("\n");

            for (String s : content) {
                String[] split = s.split(";");
                for (String s1 : split) {
                    printer.print(s1);
                }
                printer.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
