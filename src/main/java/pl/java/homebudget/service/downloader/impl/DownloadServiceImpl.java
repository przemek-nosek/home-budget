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
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService {

    private final AssetService assetService;
    private final ExpenseService expenseService;

    private final AssetDownloadBuilder assetDownloadBuilder;
    private final ExpenseDownloadBuilder expenseDownloadBuilder;
    private final DownloadPrepareService downloadPrepareService;

    private final DownloadConfigurer downloadConfigurer;


    public void downloadFile(Map<String, String> filters, HttpServletResponse response, DownloadSetting downloadSetting) {
        switch (downloadSetting) {
            case ASSET -> prepareAsset(filters, response);
            case EXPENSE -> prepareExpense(filters, response);
            default -> throw new RuntimeException("SOMETHING UNPREDICTED HAPPENED");
        }
    }

    private void prepareAsset(Map<String, String> filters, HttpServletResponse response) {
        String separator = downloadConfigurer.getFileSeparator().getSeparator();

        List<AssetDto> dtos = getAssets(filters);

        StringBuilder builder = assetDownloadBuilder.prepareAsset(dtos, separator);
        String filename = downloadConfigurer.getAssetFileName();

        downloadPrepareService.prepareResponseToDownload(response, builder, filename);
    }

    private void prepareExpense(Map<String, String> filters, HttpServletResponse response) {
        String separator = downloadConfigurer.getFileSeparator().getSeparator();

        List<ExpenseDto> dtos = getExpenses(filters);

        StringBuilder builder = expenseDownloadBuilder.prepareExpense(dtos, separator);
        String filename = downloadConfigurer.getExpenseFileName();

        downloadPrepareService.prepareResponseToDownload(response, builder, filename);
    }

    private List<AssetDto> getAssets(Map<String, String> filters) {
        if (Objects.isNull(filters)) {
            return assetService.getAssets();
        }
        return assetService.getFilteredAssets(filters);
    }


    private List<ExpenseDto> getExpenses(Map<String, String> filters) {
        if (Objects.isNull(filters)) {
            return expenseService.getExpenses();
        }
        return expenseService.getFilteredExpenses(filters);
    }

}
