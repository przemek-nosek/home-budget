package pl.java.homebudget.service.downloader.impl;

import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AssetDto;

import java.util.List;

@Service
class AssetDownloadBuilder {

    public StringBuilder prepareAsset(List<AssetDto> dtos, String separator) {
        StringBuilder builder = new StringBuilder();

        builder
                .append("Amount")
                .append(separator)
                .append("Category")
                .append(separator)
                .append("Income Date")
                .append(separator)
                .append("Description")
                .append("\n");

        dtos.forEach(asset -> {
            builder.append(asset.getAmount());
            builder.append(separator);
            builder.append(asset.getCategory());
            builder.append(separator);
            builder.append(asset.getIncomeDate());
            builder.append(separator);
            builder.append(asset.getDescription());
            builder.append("\n");
        });

        return builder;
    }
}
