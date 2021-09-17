package pl.java.homebudget.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.AssetEntity;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.repository.AssetRepository;
import pl.java.homebudget.service.AssetService;

import javax.validation.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    AssetRepository repository;

    @InjectMocks
    AssetServiceImpl service;

    List<AssetEntity> assetDtoList = new ArrayList<>();

    Validator validator;

    @BeforeEach
    void setUp() {
        //given
        assetDtoList.clear();
        AssetEntity assetEntity1 = new AssetEntity(1L, BigDecimal.ONE, Instant.now(), AssetCategory.BONUS);
        AssetEntity assetEntity2 = new AssetEntity(2L, BigDecimal.TEN, Instant.now(), AssetCategory.SALARY);
        AssetEntity assetEntity3 = new AssetEntity(3L, BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER);

        assetDtoList.add(assetEntity1);
        assetDtoList.add(assetEntity2);
        assetDtoList.add(assetEntity3);

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
        AssetEntity assetEntity = new AssetEntity(1L, BigDecimal.ONE, Instant.now(), AssetCategory.BONUS);
        AssetDto assetDto = new AssetDto(1L, BigDecimal.ONE, Instant.now(), AssetCategory.BONUS);
        given(repository.save(assetEntity)).willReturn(assetEntity);

        //when
        AssetDto savedAssetDto = service.addAsset(assetDto);

        //then
        then(repository).should().save(assetEntity);

        assertThat(savedAssetDto).isEqualTo(assetDto);
    }

    @Test
    @DisplayName("Should fail when Asset amount is null")
    void addAsset_invalidAmountShouldFailValidation() {
        //given
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setCategory(AssetCategory.OTHER);
        assetEntity.setIncomeDate(Instant.now());

        AssetDto assetDto = new AssetDto();
        assetDto.setCategory(AssetCategory.OTHER);
        assetDto.setIncomeDate(assetEntity.getIncomeDate());

        given(repository.save(assetEntity)).willThrow(ConstraintViolationException.class);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> service.addAsset(assetDto));
        Set<ConstraintViolation<AssetEntity>> validate = validator.validate(assetEntity);
        assertFalse(validate.isEmpty());
        then(repository).should().save(assetEntity);
    }

    @Test
    @DisplayName("Should fail when Asset incomeDate is null")
    void addAsset_invalidIncomeDateShouldFailValidation() {
        //given
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setCategory(AssetCategory.OTHER);
        assetEntity.setAmount(BigDecimal.ONE);

        AssetDto assetDto = new AssetDto();
        assetDto.setCategory(AssetCategory.OTHER);
        assetDto.setAmount(BigDecimal.ONE);

        given(repository.save(assetEntity)).willThrow(ConstraintViolationException.class);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> service.addAsset(assetDto));
        Set<ConstraintViolation<AssetEntity>> validate = validator.validate(assetEntity);
        assertFalse(validate.isEmpty());
        then(repository).should().save(assetEntity);
    }

    @Test
    void deleteAsset() {
    }

    @Test
    void deleteAssetById() {
    }

    @Test
    void updateAsset() {
    }
}