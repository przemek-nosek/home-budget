package pl.java.homebudget.service.uploader;

import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.enums.AssetCategory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetParserService {

    public List<AssetDto> parseToAsset(String content) {
        return Arrays.stream(content.split("\n"))
                .skip(1)
                .map(s -> s.split(";"))
                .map(array -> AssetDto.builder()
                        .amount(new BigDecimal(array[0]))
                        .category(AssetCategory.valueOf(array[1]))
                        .incomeDate(Instant.parse(array[2] + "T00:00:00.000Z"))
                        .description(array[3])
                        .build())
                .collect(Collectors.toList());

    }
}
