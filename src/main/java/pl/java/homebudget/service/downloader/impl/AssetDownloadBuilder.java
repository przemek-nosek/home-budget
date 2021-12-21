package pl.java.homebudget.service.downloader.impl;

import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AssetDto;

import java.util.List;

@Service
class AssetDownloadBuilder {

    public StringBuilder prepareAsset(List<AssetDto> dtos, String separator) {
        StringBuilder builder = new StringBuilder("Amount" + separator + "Category" + separator + "Income Date" + separator + "Description");

        dtos.forEach(asset -> {
            builder.append("\n");
            builder.append(asset.getAmount());
            builder.append(separator);
            builder.append(asset.getCategory());
            builder.append(separator);
            builder.append(asset.getIncomeDate());
            builder.append(separator);
            builder.append(asset.getDescription());
        });

        return builder;
    }
}
