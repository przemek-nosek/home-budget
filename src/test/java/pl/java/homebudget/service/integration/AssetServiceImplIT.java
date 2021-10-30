package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.dto.ExpenseDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.exception.MissingExpenseFilterSettingException;
import pl.java.homebudget.mapper.AssetMapper;
import pl.java.homebudget.service.AssetService;
import pl.java.homebudget.service.init.InitDataForIT;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AssetServiceImplIT extends InitDataForIT {

    @Autowired
    private AssetService assetService;

    private final AssetMapper assetMapper = Mappers.getMapper(AssetMapper.class);


    @Test
    void shouldGetAllAssets_AssociatedWithFirstUser() {
        //given
        initDatabaseWithTwoUsersAndAssets();

        //when
        List<AssetDto> assets = assetService.getAssets();

        //then
        assertThat(assets).hasSize(4);

        List<Asset> allAssets = assetRepository.findAll();
        assertThat(allAssets).hasSize(6);
    }

    @Test
    void shouldAddAsset() {
        //given
        initDatabaseWithFirstUser();
        AssetDto assetDto = new AssetDto(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER);

        //when
        AssetDto addedAsset = assetService.addAsset(assetDto);

        //then
        assertThat(addedAsset.getAmount()).isEqualTo(assetDto.getAmount());
        assertThat(addedAsset.getIncomeDate()).isEqualTo(assetDto.getIncomeDate());
        assertThat(addedAsset.getCategory()).isEqualTo(assetDto.getCategory());
    }

    @Test
    void shouldDeleteAsset() {
        //given
        initDatabaseWithTwoUsersAndAssets();
        List<Asset> assetList = assetRepository.findAll();
        Asset asset = assetList.get(0);
        AssetDto assetDto = assetMapper.fromAssetToDto(asset);

        //when
        assetService.deleteAsset(assetDto);

        //then
        int size = assetRepository.findAll().size();

        assertThat(size).isEqualTo(assetList.size() - 1);
    }

    @Test
    void shouldDeleteAssetById() {
        //given
        initDatabaseWithTwoUsersAndAssets();
        List<Asset> assetList = assetRepository.findAll();
        Asset asset = assetList.get(0);

        Long id = asset.getId();

        //when
        assetService.deleteAssetById(id);

        //then
        assertThat(assetRepository.existsById(id)).isFalse();
    }

    @Test
    void shouldNotDeleteById_WhenIdDoesNotExist() {
        //given
        Long notExistsId = -52L;


        //when
        //then
        assertThrows(AssetNotFoundException.class, () -> assetService.deleteAssetById(notExistsId));
    }

    @Test
    void shouldUpdateAsset() {
        //given
        initDatabaseWithTwoUsersAndAssets();
        Asset asset = assetRepository.findAll().get(0);

        AssetDto assetDto = new AssetDto(asset.getId(), BigDecimal.valueOf(51283L), Instant.now(), AssetCategory.BONUS);

        //when
        AssetDto updateAsset = assetService.updateAsset(assetDto);

        //then
        assertThat(updateAsset.getAmount()).isEqualTo(assetDto.getAmount());
        assertThat(updateAsset.getCategory()).isEqualTo(assetDto.getCategory());
    }

    @Test
    void shouldNotUpdateAsset_WhenItDoesNotExistById() {
        //given
        initDatabaseWithFirstUser();
        Long notExistsId = -52L;
        AssetDto assetDto = new AssetDto(notExistsId, BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER);

        //when
        //then
        assertThrows(AssetNotFoundException.class, () -> assetService.updateAsset(assetDto));
    }

    @Test
    void shouldGetAssetsByCategory() {
        //given
        initDatabaseWithTwoUsersAndAssets();
        AssetCategory assetCategory = AssetCategory.OTHER;

        //when
        List<AssetDto> assetsByCategory = assetService.getAssetsByCategory(assetCategory);

        //then
        assertThat(assetsByCategory.size()).isNotZero();
        assetsByCategory
                .forEach(assetDto -> assertThat(assetDto.getCategory()).isEqualTo(assetCategory));
    }

    @Test
    void shouldDeleteAllAssetsByAppUser() {
        //given
        initDatabaseWithTwoUsersAndAssets();
        AppUser appUser = appUserRepository.findByUsername("user").get();
        //when
        List<Asset> assetList = assetRepository.findAllByAppUser(appUser);
        assertThat(assetList).isNotEmpty();

        assetService.deleteAssetsByAppUser();

        //then
        List<Asset> afterDeletion = assetRepository.findAllByAppUser(appUser);
        assertThat(afterDeletion).isEmpty();

        List<Asset> allAssets = assetRepository.findAll();
        assertThat(allAssets).isNotEmpty();
    }

    @ParameterizedTest(name = "Filters: {0} and {1}")
    @MethodSource(value = "getFilters")
    void shouldGetFilteredExpenses(String firstFilter, String secondFilter, Map<String, String> filters) {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        initDatabaseWithAssetAndUser(appUser, "2021-10-01");
        initDatabaseWithAssetAndUser(appUser, "2021-10-10");
        initDatabaseWithAssetAndUser(appUser, "2021-10-30");
        initDatabaseWithAssetAndUser(appUser, "2021-11-01");

        //when
        List<AssetDto> filteredExpenses = assetService.getFilteredAssets(filters);

        //then
        assertThat(filteredExpenses).hasSize(3);
    }

    @ParameterizedTest(name = "existing filter: {0}, missing filter: {1}")
    @MethodSource(value = "getFiltersWithOneMissingFilter")
    void shouldNotGetFilteredExpenses_andThrowMissingExpenseFilterSettingException(String existingFilter, String missingFilter, Map<String, String> filters) {
        //given
        initDatabaseWithFirstUser();

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
}
