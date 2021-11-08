package pl.java.homebudget.service.downloader.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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


    public void addToResponse(HttpServletResponse response, DownloadSetting downloadSetting) {
        switch (downloadSetting) {
            case ASSET -> prepareAsset(response);
            case EXPENSE -> prepareExpense(response);
            default -> throw new RuntimeException("SOMETHING UNPREDICTED HAPPENED");
        }
    }

    private void prepareAsset(HttpServletResponse response) {
        List<AssetDto> dtos = assetService.getAssets();
        StringBuilder builder = assetDownloadBuilder.prepareAsset(dtos);
        String filename = "assets";

        downloadPrepareService.prepareToDownload(response, builder, filename);
    }

    private void prepareExpense(HttpServletResponse response) {
        List<ExpenseDto> dtos = expenseService.getExpenses();
        StringBuilder builder = expenseDownloadBuilder.prepareExpense(dtos);
        String filename = "expenses";

        downloadPrepareService.prepareToDownload(response, builder, filename);
    }


}
