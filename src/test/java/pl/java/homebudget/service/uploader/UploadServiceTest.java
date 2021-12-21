package pl.java.homebudget.service.uploader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.service.AssetService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UploadServiceTest {

    @Mock
    private AssetService assetService;

    private UploadService uploadService;

    private AssetParserService assetParserService = new AssetParserService();

    @BeforeEach
    void setUp() {
        uploadService = new UploadService(assetService, assetParserService);
    }

    @Test
    void shouldUploadDataFromCsvFile() throws IOException {
        //given
        MultipartFile mock = mock(MultipartFile.class);
        String csvData = """
                Amount;Category;Income Date;Description
                666.00;OTHER;2021-10-14;opis123
                """;

        byte[] bytes = csvData.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        List<AssetDto> assetDtos = List.of(AssetDto.builder()
                .amount(new BigDecimal("666.00"))
                .category(AssetCategory.OTHER)
                .incomeDate(Instant.parse("2021-10-14T00:00:00.000Z"))
                .description("opis123")

                .build());
        given(mock.getInputStream()).willReturn(byteArrayInputStream);

        //when
        uploadService.uploadFile(mock);

        //then
        then(assetService).should().addAllAssets(assetDtos);

    }
}