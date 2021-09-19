package pl.java.homebudget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.java.homebudget.dto.AssetDto;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.security.config.AuthenticationBudgetConfiguration;
import pl.java.homebudget.service.AssetService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(value = AssetController.class)
@SpringBootTest
@AutoConfigureMockMvc
class AssetControllerTest {

    public static final String ASSETS_URI = "/api/v1/assets";
    @MockBean
    AssetService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
            AuthenticationBudgetConfiguration authenticationBudgetConfiguration;

    AssetDto assetDto;

    @BeforeEach
    void setUp() {
        assetDto = new AssetDto(1L, BigDecimal.TEN, Instant.now(), AssetCategory.OTHER);
    }

    @AfterEach
    void tearDown() {
        reset(service);
    }

    @Test
    void getAssets() throws Exception {
        //given
        given(service.getAssets()).willReturn(List.of(assetDto));

        //when
        MvcResult mvcResult = mockMvc.perform(get(ASSETS_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //then
        String actualResponse = mvcResult.getResponse().getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(List.of(assetDto));

        assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedResponse);

        then(service).should().getAssets();
    }

    @Test
    void addAsset() throws Exception {
        //given
        given(service.addAsset(assetDto)).willReturn(assetDto);
        String expectedResponse = objectMapper.writeValueAsString(assetDto);


        //when
        MvcResult mvcResult = mockMvc.perform(post(ASSETS_URI).contentType(MediaType.APPLICATION_JSON).content(expectedResponse))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //then
        String actualResponse = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedResponse);

        then(service).should().addAsset(assetDto);
    }

    @Test
    void deleteAsset() throws Exception {
        //given
        String jsonAssetToDelete = objectMapper.writeValueAsString(assetDto);
        doNothing().when(service).deleteAsset(any());

        //when
        mockMvc.perform(delete(ASSETS_URI).contentType(MediaType.APPLICATION_JSON).content(jsonAssetToDelete))
                .andExpect(status().isNoContent());

        //then
        then(service).should().deleteAsset(any());
    }

    @Test
    void deleteAssetById() throws Exception {
        //given
        String id = "1";
        doNothing().when(service).deleteAssetById(anyLong());

        //when
        mockMvc.perform(delete(ASSETS_URI + "/" + id))
                .andExpect(status().isNoContent());

        //then
        then(service).should().deleteAssetById(anyLong());
    }

    @Test
    void updateAsset() throws Exception {
        //given
        AssetDto assetToUpdate = new AssetDto(1L, BigDecimal.ONE, Instant.now(), AssetCategory.BONUS);
        given(service.updateAsset(any())).willReturn(assetToUpdate);

        //when
        String jsonAsset = objectMapper.writeValueAsString(assetToUpdate);
        MvcResult mvcResult = mockMvc.perform(
                put(ASSETS_URI).contentType(MediaType.APPLICATION_JSON).content(jsonAsset)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();

        //then
        then(service).should().updateAsset(any());
        assertThat(actualResponse).isEqualToIgnoringWhitespace(jsonAsset);
    }

    @Test
    void getAssetsByCategory() throws Exception {
        //given
        String category = "other";
        List<AssetDto> assetDtoList = List.of(new AssetDto(BigDecimal.ZERO, Instant.now(), AssetCategory.OTHER));
        given(service.getAssetsByCategory(AssetCategory.valueOf(category.toUpperCase()))).willReturn(assetDtoList);

        String jsonAsset = objectMapper.writeValueAsString(assetDtoList);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/assets/find?category={category}", category))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();

        //then
        then(service).should().getAssetsByCategory(any());
        assertThat(actualResponse).isEqualToIgnoringWhitespace(jsonAsset);
    }
}