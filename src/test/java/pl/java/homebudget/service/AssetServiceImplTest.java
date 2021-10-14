package pl.java.homebudget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.mapper.AssetMapper;
import pl.java.homebudget.repository.AssetRepository;
import pl.java.homebudget.service.impl.AssetServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private UserLoggedInfo userLoggedInfo;

    @InjectMocks
    private AssetServiceImpl assetService;

    private final AssetMapper assetMapper = Mappers.getMapper(AssetMapper.class);

    @Test
    void getAssets() {
        //given
        List<Asset> assets = new ArrayList<>();
        AppUser appUser = new AppUser("user", "password");
        assets.add(new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, appUser));
        assets.add(new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.SALARY, appUser));
        assets.add(new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.BONUS, appUser));

        given(assetRepository.getAssetsByAppUser(any())).willReturn(assets);
        given(userLoggedInfo.getLoggedAppUser()).willReturn(appUser);

        //when
        List<AssetDto> assetDtoList = assetService.getAssets();

        //then
        List<AssetDto> assetDtos = assets.stream()
                .map(assetMapper::fromAssetToDto)
                .collect(Collectors.toList());

        then(assetRepository).should().getAssetsByAppUser(any());
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

        given(assetRepository.findById(anyLong())).willReturn(Optional.of(asset));

        //when
        AssetDto updatedAsset = assetService.updateAsset(assetDto);

        //then
        assertThat(updatedAsset.getCategory()).isEqualTo(assetDto.getCategory());
        assertThat(updatedAsset.getAmount()).isEqualTo(assetDto.getAmount());
        then(assetRepository).should().findById(anyLong());
        then(assetRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateAsset_fails_andThrowsAssetNotFoundException() {
        //given
        AssetDto assetDto = new AssetDto(1L, BigDecimal.TEN, Instant.now(), AssetCategory.BONUS);
        given(assetRepository.findById(anyLong())).willThrow(AssetNotFoundException.class);

        //when
        //then
        assertThrows(AssetNotFoundException.class, () -> assetService.updateAsset(assetDto));
        then(assetRepository).should().findById(anyLong());

    }

    @Test
    void getAssetsByCategory() {
        //given
        AssetCategory assetCategory = AssetCategory.OTHER;
        AppUser appUser = new AppUser("user", "password");
        given(assetRepository.getAssetEntitiesByCategoryAndAppUser(assetCategory, appUser)).willReturn(
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
        then(assetRepository).should().getAssetEntitiesByCategoryAndAppUser(assetCategory, appUser);
    }
}