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


    public void addToResponse(HttpServletResponse response, DownloadSetting downloadSetting) {
        switch (downloadSetting) {
            case ASSET -> prepareAsset(response);
            case EXPENSE -> prepareExpense(response);
            default -> throw new RuntimeException("SOMETHING UNPREDICTED HAPPENED");
        }
    }

    private void prepareAsset(HttpServletResponse response) {
        List<AssetDto> dtos = assetService.getAssets();
        String separator = downloadConfigurer.getFileSeparator().getSeparator();

        StringBuilder builder = assetDownloadBuilder.prepareAsset(dtos, separator);
        String filename = downloadConfigurer.getAssetFileName();

        downloadPrepareService.prepareToDownload(response, builder, filename);
    }

    private void prepareExpense(HttpServletResponse response) {
        List<ExpenseDto> dtos = expenseService.getExpenses();
        String separator = downloadConfigurer.getFileSeparator().getSeparator();

        StringBuilder builder = expenseDownloadBuilder.prepareExpense(dtos, separator);
        String filename = downloadConfigurer.getExpenseFileName();

        downloadPrepareService.prepareToDownload(response, builder, filename);
    }
}
