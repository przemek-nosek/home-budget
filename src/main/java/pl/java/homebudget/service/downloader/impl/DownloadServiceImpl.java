package pl.java.homebudget.service.downloader.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.java.homebudget.config.DownloadConfigurer;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.enums.DownloadSetting;
import pl.java.homebudget.service.AssetService;
import pl.java.homebudget.service.ExpenseService;
import pl.java.homebudget.service.downloader.DownloadService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService {

    private final AssetDownloadBuilder assetDownloadBuilder;
    private final AssetService assetService;
    private final ExpenseDownloadBuilder expenseDownloadBuilder;
    private final ExpenseService expenseService;
    private final DownloadPrepareService downloadPrepareService;
    private final DownloadConfigurer downloadConfigurer;


    public void addToResponse(Map<String, String> filters, HttpServletResponse response, DownloadSetting downloadSetting) {
        switch (downloadSetting) {
            case ASSET -> prepareAsset(filters, response);
            case EXPENSE -> prepareExpense(filters, response);
            default -> throw new RuntimeException("SOMETHING UNPREDICTED HAPPENED");
        }
    }

    private void prepareAsset(Map<String, String> filters, HttpServletResponse response) {
        String separator = downloadConfigurer.getFileSeparator().getSeparator();
        List<AssetDto> dtos = assetService.getFilteredAssets(filters);

        StringBuilder builder = assetDownloadBuilder.prepareAsset(dtos, separator);
        String filename = downloadConfigurer.getAssetFileName();

        downloadPrepareService.prepareToDownload(response, builder, filename);
    }

    private void prepareExpense(Map<String, String> filters, HttpServletResponse response) {
        List<ExpenseDto> dtos = expenseService.getFilteredExpenses(filters);
        String separator = downloadConfigurer.getFileSeparator().getSeparator();

        StringBuilder builder = expenseDownloadBuilder.prepareExpense(dtos, separator);
        String filename = downloadConfigurer.getExpenseFileName();

        downloadPrepareService.prepareToDownload(response, builder, filename);
    }
}
