package pl.java.homebudget.service.downloader.impl;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AssetDto;

import java.util.List;

@Service
class AssetDownloadBuilder {

    private final static String SEPARATOR = ";";

    public StringBuilder prepareAsset(List<AssetDto> dtos) {
        StringBuilder builder = new StringBuilder();

        dtos.forEach(asset -> {
            builder.append(asset.getAmount());
            builder.append(SEPARATOR);
            builder.append(asset.getCategory());
            builder.append(SEPARATOR);
            builder.append(asset.getIncomeDate());
            builder.append(SEPARATOR);
            builder.append(asset.getDescription());
            builder.append("\n");
        });

        return builder;
    }
}
