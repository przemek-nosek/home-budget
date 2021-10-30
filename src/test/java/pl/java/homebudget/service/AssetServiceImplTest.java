package pl.java.homebudget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.entity.Expense;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.enums.ExpensesCategory;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.exception.MissingExpenseFilterSettingException;
import pl.java.homebudget.filter.FilterRange;
import pl.java.homebudget.mapper.AssetMapper;
import pl.java.homebudget.repository.AssetRepository;
import pl.java.homebudget.service.impl.AssetServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private UserLoggedInfo userLoggedInfo;

    @InjectMocks
    private AssetServiceImpl assetService;

    private final AssetMapper assetMapper = Mappers.getMapper(AssetMapper.class);

    @Mock
    private FilterRange<Asset> assetFilterRange;

    @Test
    void getAssets() {
        //given
        List<Asset> assets = new ArrayList<>();
        AppUser appUser = new AppUser("user", "password");
        assets.add(new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, appUser));
        assets.add(new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.SALARY, appUser));
        assets.add(new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.BONUS, appUser));

        given(assetRepository.findAllByAppUser(any())).willReturn(assets);
        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);

        //when
        List<AssetDto> assetDtoList = assetService.getAssets();

        //then
        List<AssetDto> assetDtos = assets.stream()
                .map(assetMapper::fromAssetToDto)
                .collect(Collectors.toList());

        then(assetRepository).should().findAllByAppUser(any());
        then(userLoggedInfo).should().getLoggedAppUser();
        assertThat(assetDtoList).hasSize(3);
        assertThat(assetDtoList).containsExactlyElementsOf(assetDtos);
    }

    @Test
    void addAsset() {
        //given
        AppUser appUser = new AppUser("user", "password");
        Asset asset = new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, appUser);
        AssetDto assetDto = new AssetDto(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER);

        given(assetRepository.save(asset)).willReturn(asset);
        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);

        //when
        AssetDto savedAsset = assetService.addAsset(assetDto);

        //then
        assertThat(savedAsset).isNotNull();
        assertThat(savedAsset).isEqualTo(assetDto);
    }

    @Test
    void deleteAsset() {
        //given
        AssetDto assetDto = new AssetDto();

        //when
        assetService.deleteAsset(assetDto);

        //then
        then(assetRepository).should().delete(any());

    }

    @Test
    void deleteAssetById_successfully_whenAssetExistsById() {
        //given
        Long id = 1L;
        given(assetRepository.existsById(anyLong())).willReturn(true);

        //when
        assetService.deleteAssetById(id);

        //then
        then(assetRepository).should().deleteById(anyLong());
    }

    @Test
    void shouldDeleteAssetsByAppUser() {
        //given
        //when
        assetService.deleteAssetsByAppUser();

        //then
        then(assetRepository).should().deleteAllByAppUser(any());
    }

    @Test
    void deleteAssetById_fails_whenAssetDoesNotExistsById_andThrow_AssetNotFoundException() {
        //given
        given(assetRepository.existsById(anyLong())).willReturn(false);

        //when
        //then
        assertThrows(AssetNotFoundException.class, () -> assetService.deleteAssetById(anyLong()));
        then(assetRepository).should().existsById(anyLong());
        then(assetRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateAsset_successfully() {
        //given
        AssetDto assetDto = new AssetDto(1L, BigDecimal.TEN, Instant.now(), AssetCategory.BONUS);
        AppUser appUser = new AppUser("username", "password");
        Asset asset = new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, appUser);
//        asset.setId(1L);

        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);
        given(assetRepository.findByIdAndAppUser(assetDto.getId(), appUser)).willReturn(Optional.of(asset));

        //when
        AssetDto updatedAsset = assetService.updateAsset(assetDto);

        //then
        assertThat(updatedAsset.getCategory()).isEqualTo(assetDto.getCategory());
        assertThat(updatedAsset.getAmount()).isEqualTo(assetDto.getAmount());
        then(assetRepository).should().findByIdAndAppUser(anyLong(), any());
        then(assetRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateAsset_fails_andThrowsAssetNotFoundException() {
        //given
        AssetDto assetDto = new AssetDto(-1L, BigDecimal.TEN, Instant.now(), AssetCategory.BONUS);
        given(assetRepository.findByIdAndAppUser(anyLong(), any())).willThrow(AssetNotFoundException.class);

        //when
        //then
        assertThrows(AssetNotFoundException.class, () -> assetService.updateAsset(assetDto));
        then(assetRepository).should().findByIdAndAppUser(anyLong(), any());

    }

    @Test
    void getAssetsByCategory() {
        //given
        AssetCategory assetCategory = AssetCategory.OTHER;
        AppUser appUser = new AppUser("user", "password");
        given(assetRepository.findAllByCategoryAndAppUser(assetCategory, appUser)).willReturn(
                List.of(
                        new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, appUser),
                        new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.OTHER, appUser),
                        new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.OTHER, appUser)
                )
        );
        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);

        //when
        List<AssetDto> assetsByCategory = assetService.getAssetsByCategory(assetCategory);

        //then
        assertThat(assetsByCategory).hasSize(3);
        then(assetRepository).should().findAllByCategoryAndAppUser(assetCategory, appUser);
    }

    @ParameterizedTest(name = "Filters: {0} and {1}")
    @MethodSource(value = "getFilters")
    void shouldGetFilteredAssets_byFilters(String firstFilter, String secondFilter, Map<String, String> filters) {
        //given


        AppUser appUser = getAppUser();
        List<Asset> assetList = List.of(
                new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, appUser),
                new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.LOAN_RETURNED, appUser),
                new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.SALARY, appUser)
        );
        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);
        given(assetFilterRange.getAllByFilter(appUser, filters)).willReturn(assetList);

        //when
        List<AssetDto> filteredExpenses = assetService.getFilteredAssets(filters);


        //then
        assertThat(filteredExpenses).hasSize(3);
    }


    @ParameterizedTest(name = "existing filter: {0}, missing filter: {1}")
    @MethodSource(value = "getFiltersWithOneMissingFilter")
    void shouldNotGetFilteredAssets_andThrowMissingExpenseFilterSettingException(String existingFilter, String missingFilter, Map<String, String> filters) {
        //given
        AppUser appUser = getAppUser();
        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);
        doThrow(new MissingExpenseFilterSettingException("Missing filter setting: " + missingFilter)).when(assetFilterRange).getAllByFilter(appUser, filters);

        //when
        MissingExpenseFilterSettingException ex = assertThrows(MissingExpenseFilterSettingException.class, () -> assetService.getFilteredAssets(filters));

        //then
        assertThat(ex.getMessage()).isEqualTo("Missing filter setting: " + missingFilter);
    }

    private static Stream<Arguments> getFiltersWithOneMissingFilter() {
        return Stream.of(
                Arguments.of("from", "to", Map.of("from", "2021-10-01", "missingTo", "2021-10-30")),
                Arguments.of("to", "from", Map.of("missingFrom", "2021-10-01", "to", "2021-10-30")),
                Arguments.of("month", "year", Map.of("month", "october", "missingYear", "2021")),
                Arguments.of("year", "month", Map.of("missingMonth", "october", "year", "2021")));
    }

    private static Stream<Arguments> getFilters() {
        return Stream.of(
                Arguments.of("from", "to", Map.of("from", "2021-10-01", "to", "2021-10-30")),
                Arguments.of("month", "year", Map.of("month", "october", "year", "2021")));
    }

    private AppUser getAppUser() {
        return new AppUser("user", "password");
    }
}