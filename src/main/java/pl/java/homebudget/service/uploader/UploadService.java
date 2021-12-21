package pl.java.homebudget.service.uploader;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.service.AssetService;
import pl.java.homebudget.service.ExpenseService;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UploadService {

    private final AssetService assetService;
    private final ExpenseService expenseService;
    private final AssetParserService assetParserService;
    private final ExpenseParserService expenseParserService;

    public void uploadFile(MultipartFile multipartFile, String option) {

        try {
            byte[] bytes = multipartFile.getInputStream().readAllBytes();
            String s = new String(bytes);

            if (option.equals("ASSET")) {
                List<AssetDto> assetDtoList = assetParserService.parseToAsset(s);
                assetService.addAllAssets(assetDtoList);
                log.info("Saved assets from file: {}", assetDtoList);
            } else if (option.equals("EXPENSE")){
                List<ExpenseDto> expenseDtoList = expenseParserService.parseToExpense(s);
                expenseService.saveAllExpenses(expenseDtoList);
                log.info("Saved expenses from file: {}", expenseDtoList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
