package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.mapper.AssetMapper;
import pl.java.homebudget.repository.AppUserRepository;
import pl.java.homebudget.repository.AssetRepository;
import pl.java.homebudget.service.impl.AssetServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@WithMockUser(username = "username", password = "password")
public class AssetServiceImplIT {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetServiceImpl assetService;

    @Autowired
    private AppUserRepository appUserRepository;

    private final AssetMapper assetMapper = Mappers.getMapper(AssetMapper.class);


    @Test
    void shouldGetAllAssets_AssociatedWithFirstUser() {
        //given
        initDatabase();

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
        initFirstUser();
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
        initDatabase();
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
        initDatabase();
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
        initDatabase();
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
        Long notExistsId = -52L;
        AssetDto assetDto = new AssetDto(notExistsId, BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER);

        //when
        //then
        assertThrows(AssetNotFoundException.class, () -> assetService.updateAsset(assetDto));
    }

    @Test
    void shouldGetAssetsByCategory() {
        //given
        initDatabase();
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
        initDatabase();
        AppUser appUser = appUserRepository.findByUsername("username").get();
        //when
        List<Asset> assetList = assetRepository.getAssetsByAppUser(appUser);
        assertThat(assetList).isNotEmpty();

        assetService.deleteAssetsByAppUser();

        //then
        List<Asset> afterDeletion = assetRepository.getAssetsByAppUser(appUser);
        assertThat(afterDeletion).isEmpty();

        List<Asset> allAssets = assetRepository.findAll();
        assertThat(allAssets).isNotEmpty();
    }

    private AppUser initFirstUser() {
        AppUser appUser = new AppUser("username", "password");
        return appUserRepository.save(appUser);
    }

    private AppUser initSecondUser() {
        AppUser appUser = new AppUser("secondUser", "secondPassword");
        return appUserRepository.save(appUser);
    }

    private void initDatabase() {
        AppUser firstUser = initFirstUser();
        List<Asset> assets = new ArrayList<>();
        assets.add(new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, firstUser));
        assets.add(new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.SALARY, firstUser));
        assets.add(new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.BONUS, firstUser));
        assets.add(new Asset(BigDecimal.valueOf(105L), Instant.now(), AssetCategory.BONUS, firstUser));

        AppUser secondUser = initSecondUser();
        assets.add(new Asset(BigDecimal.valueOf(150L), Instant.now(), AssetCategory.BONUS, secondUser));
        assets.add(new Asset(BigDecimal.valueOf(300L), Instant.now(), AssetCategory.LOAN_RETURNED, secondUser));

        assetRepository.saveAll(assets);
    }
}
