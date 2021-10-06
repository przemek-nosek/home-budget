package pl.java.xxx.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Asset;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.repository.AppUserRepository;
import pl.java.homebudget.repository.AssetRepository;
import pl.java.homebudget.service.AssetService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@WithMockUser(username = "admin", password = "admin")
public class AssetServiceIT {

    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private AssetService assetService;
    @Autowired
    private AppUserRepository appUserRepository;


    @Test
    void shouldReturnListWithThreeElements() {
        //given
        initDataBase();

        //when
        List<AssetDto> allAssets = assetService.getAssets();

        //then
        assertThat(allAssets).hasSize(3);
    }

    @Test
    void shouldAddAssetToTheDb() {
        //given
        initAppUserDataBase();
        AssetDto assetDto = new AssetDto(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER);

        //when
        assetService.addAsset(assetDto);

        //then
        List<Asset> allAssets = assetRepository.findAll();
        assertThat(allAssets).hasSize(1);
        Asset asset = allAssets.get(0);
        assertThat(asset.getAmount()).isEqualTo(assetDto.getAmount());
        assertThat(asset.getIncomeDate()).isEqualTo(assetDto.getIncomeDate());
        assertThat(asset.getCategory()).isEqualTo(assetDto.getCategory());
    }

    @Test
    void shouldReturnAssetsByCategory() {
        //given
        initDataBase();
        AssetCategory assetCategory = AssetCategory.SALARY;

        //when
        List<AssetDto> assetsByCategory = assetService.getAssetsByCategory(assetCategory);

        //then
        AssetDto assetDto = assetsByCategory.get(0);
        assertThat(assetsByCategory).hasSize(1);
        assertThat(assetDto.getCategory()).isEqualTo(assetCategory);
    }

    private AppUser initAppUserDataBase() {
        AppUser appUser = new AppUser( "admin", "admin");

        return appUserRepository.save(appUser);
    }

    private void initDataBase() {
        AppUser appUser = initAppUserDataBase();

        Asset entity1 = new Asset(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER, appUser);
        Asset entity2 = new Asset(BigDecimal.ONE, Instant.now(), AssetCategory.LOAN_RETURNED, appUser);
        Asset entity3 = new Asset(BigDecimal.TEN, Instant.now(), AssetCategory.SALARY, appUser);

        assetRepository.saveAll(List.of(entity1, entity2, entity3));
    }
}
