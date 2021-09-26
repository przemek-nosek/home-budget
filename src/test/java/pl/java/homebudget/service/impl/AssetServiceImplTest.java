package pl.java.homebudget.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.mapper.AssetMapper;
import pl.java.homebudget.repository.AssetRepository;

import javax.validation.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest { // TODO: CLEAN UP CODE

    @Mock
    AssetRepository repository;

    @InjectMocks
    AssetServiceImpl service;

    List<Asset> assetDtoList = new ArrayList<>();

    Validator validator;

    AssetMapper mapper = Mappers.getMapper(AssetMapper.class);

    @BeforeEach
    void setUp() {
        //given
        assetDtoList.clear();
        Asset asset1 = new Asset(1L, BigDecimal.ONE, Instant.now(), AssetCategory.BONUS);
        Asset asset2 = new Asset(2L, BigDecimal.TEN, Instant.now(), AssetCategory.SALARY);
        Asset asset3 = new Asset(3L, BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER);

        assetDtoList.add(asset1);
        assetDtoList.add(asset2);
        assetDtoList.add(asset3);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should check if getAssets returns three elements and verify if repository was called once.")
    void getAssets_shouldReturnThreeAssetsAndVerifyRepositoryWasCalledOnce() {
        //given
        given(repository.findAll()).willReturn(assetDtoList);

        //when
        List<AssetDto> assets = service.getAssets();

        //then
        then(repository).should().findAll();
        assertThat(assets).hasSize(3)
                .containsExactly(assets.get(0), assets.get(1), assets.get(2));
    }

    @Test
    @DisplayName("Should successfully add new asset")
    void addAsset_shouldSuccessfullyAddNewAsset() {
        //given
        Asset asset = new Asset(1L, BigDecimal.ONE, Instant.now(), AssetCategory.BONUS);
        AssetDto assetDto = new AssetDto(1L, BigDecimal.ONE, Instant.now(), AssetCategory.BONUS);
        given(repository.save(asset)).willReturn(asset);

        //when
        AssetDto savedAssetDto = service.addAsset(assetDto);

        //then
        then(repository).should().save(asset);

        assertThat(savedAssetDto).isEqualTo(assetDto);
    }

    @Test
    @DisplayName("Should fail when Asset amount is null")
    void addAsset_invalidAmountShouldFailValidation() {
        //given
        Asset asset = new Asset();
        asset.setCategory(AssetCategory.OTHER);
        asset.setIncomeDate(Instant.now());

        AssetDto assetDto = new AssetDto();
        assetDto.setCategory(AssetCategory.OTHER);
        assetDto.setIncomeDate(asset.getIncomeDate());

        given(repository.save(asset)).willThrow(ConstraintViolationException.class);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> service.addAsset(assetDto));
        Set<ConstraintViolation<Asset>> validate = validator.validate(asset);
        assertFalse(validate.isEmpty());
        then(repository).should().save(asset);
    }

    @Test
    @DisplayName("Should fail when Asset incomeDate is null")
    void addAsset_invalidIncomeDateShouldFailValidation() {
        //given
        Asset asset = new Asset();
        asset.setCategory(AssetCategory.OTHER);
        asset.setAmount(BigDecimal.ONE);

        AssetDto assetDto = new AssetDto();
        assetDto.setCategory(AssetCategory.OTHER);
        assetDto.setAmount(BigDecimal.ONE);

        given(repository.save(asset)).willThrow(ConstraintViolationException.class);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> service.addAsset(assetDto));
        Set<ConstraintViolation<Asset>> validate = validator.validate(asset);
        assertFalse(validate.isEmpty());
        then(repository).should().save(asset);
    }

    @Test
    void deleteAsset() {
        //when
        service.deleteAsset(any());

        //then
        then(repository).should().delete(any());
    }

    @Test
    void deleteAssetById_successfully() {
        //given
        Long id = 1L;
        given(repository.existsById(id)).willReturn(true);

        //when
        service.deleteAssetById(id);

        //then
        then(repository).should().deleteById(id);
    }

    @Test
    @DisplayName("Should throw AssetNotFoundException when asset to DELETE was not found by id")
    void deleteAssetById_shouldThrowAssetNotFoundException() {
        //given
        Long id = 1L;
        given(repository.existsById(id)).willReturn(false);

        //when
        //then
        assertThrows(AssetNotFoundException.class, () -> service.deleteAssetById(id));
    }

    @Test
    @DisplayName("Should throw AssetNotFoundException when asset to UPDATE was not found by id")
    void updateAsset_shouldThrowAssetNotFoundException() {
        //given
        AssetDto assetDto = new AssetDto();
        assetDto.setId(1L);
        given(repository.findById(anyLong())).willThrow(AssetNotFoundException.class);

        //when
        //then
        assertThrows(AssetNotFoundException.class, () -> service.updateAsset(assetDto));
    }

    @Test
    void updateAsset_shouldNotUpdateEmptyFields() {
        //given
        AssetDto assetDto = new AssetDto();
        assetDto.setId(1L);

        Asset asset = new Asset();

        Asset testUpdateAsset = new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.OTHER);

        given(repository.findById(anyLong())).willReturn(Optional.of(asset));
        given(repository.saveAndFlush(testUpdateAsset)).willReturn(testUpdateAsset);

        //when
        AssetDto updateAssetDto = service.updateAsset(assetDto);

        //then
        then(repository).should().findById(anyLong());
        then(repository).should().saveAndFlush(any());

        AssetDto mapped = mapper.fromAssetToDto(testUpdateAsset);

        assertThat(mapped).isEqualTo(updateAssetDto);

    }

    @Test
    void updateAsset_shouldNotUpdateAllFields() {
        //given
        AssetDto assetDto = new AssetDto();
        assetDto.setId(1L);
        assetDto.setCategory(AssetCategory.OTHER);
        assetDto.setAmount(BigDecimal.ONE);
        assetDto.setIncomeDate(Instant.now());

        Asset asset = new Asset();

        Asset testUpdateAsset = new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.OTHER);

        given(repository.findById(anyLong())).willReturn(Optional.of(asset));
        given(repository.saveAndFlush(testUpdateAsset)).willReturn(testUpdateAsset);

        //when
        AssetDto updateAssetDto = service.updateAsset(assetDto);

        //then
        then(repository).should().findById(anyLong());
        then(repository).should().saveAndFlush(any());

        AssetDto mapped = mapper.fromAssetToDto(testUpdateAsset);

        assertThat(mapped).isEqualTo(updateAssetDto);

    }

    @Test
    void getAssetsByCategory() {
        //given
        AssetCategory assetCategory = AssetCategory.SALARY;
        List<Asset> asset = List.of(new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.SALARY));
        given(repository.getAssetEntitiesByCategory(assetCategory)).willReturn(asset);

        //when
        List<AssetDto> assetsByCategory = service.getAssetsByCategory(assetCategory);
        AssetDto assetDto = mapper.fromAssetToDto(asset.get(0));

        List<AssetDto> shouldBeEmpty = service.getAssetsByCategory(AssetCategory.OTHER);

        //then
        then(repository).should(times(2)).getAssetEntitiesByCategory(any());
        assertThat(shouldBeEmpty).hasSize(0);
        assertThat(assetsByCategory).hasSize(1);
        assertThat(assetsByCategory.get(0)).isEqualTo(assetDto);
    }
}