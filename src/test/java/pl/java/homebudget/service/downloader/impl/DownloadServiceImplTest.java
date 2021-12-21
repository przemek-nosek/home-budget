package pl.java.homebudget.service.downloader.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import pl.java.homebudget.config.DownloadConfigurer;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.enums.DownloadSetting;
import pl.java.homebudget.service.AssetService;
import pl.java.homebudget.service.ExpenseService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DownloadServiceImplTest {

    @Mock
    private AssetService assetService;
    @Mock
    private ExpenseService expenseService;

    private final AssetDownloadBuilder assetDownloadBuilder = new AssetDownloadBuilder();
    private final ExpenseDownloadBuilder expenseDownloadBuilder = new ExpenseDownloadBuilder();
    private final DownloadPrepareService downloadPrepareService = new DownloadPrepareService();
    private final DownloadConfigurer downloadConfigurer = new DownloadConfigurer();

    @BeforeEach
    void setUp() {
        downloadService = new DownloadServiceImpl(assetService, expenseService, assetDownloadBuilder,
                expenseDownloadBuilder, downloadPrepareService, downloadConfigurer);
    }

    private DownloadServiceImpl downloadService;

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void checkIfHeadersValid(DownloadSetting setting, String expectedHeader) throws UnsupportedEncodingException {
        //given
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        downloadService.downloadFile(null, response, setting);

        //then
        assertThat(response.getContentAsString()).contains(expectedHeader);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void checkIfValidContentMappedToCsv(String name) {

    }

    private static Stream<Arguments> checkIfValidContentMappedToCsv() {
        return Stream.of(
                Arguments.of(
                    "all assets",
                        List.of(
                                new AssetDto(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER),
                                new AssetDto(BigDecimal.ONE, Instant.now(), AssetCategory.SALARY),
                                new AssetDto(BigDecimal.TEN, Instant.now(), AssetCategory.BONUS)
                        )
                ),
                Arguments.of(
                        "filtered assets",
                        List.of(
                                new AssetDto(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER),
                                new AssetDto(BigDecimal.ONE, Instant.now(), AssetCategory.SALARY),
                                new AssetDto(BigDecimal.TEN, Instant.now(), AssetCategory.BONUS)
                        )
                )
        );
    }

    private static Stream<Arguments> checkIfHeadersValid() {
        return Stream.of(
                Arguments.of(
                        DownloadSetting.ASSET,
                        "Amount,Category,Income Date,Description"
                ),
                Arguments.of(
                        DownloadSetting.EXPENSE,
                        "Amount,Category,Purchase Date,Description"
                )
        );
    }
}
