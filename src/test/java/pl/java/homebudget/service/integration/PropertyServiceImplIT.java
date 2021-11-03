package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.java.homebudget.dto.PropertyDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Property;
import pl.java.homebudget.service.PropertyService;
import pl.java.homebudget.service.init.InitDataForIT;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyServiceImplIT extends InitDataForIT {

    @Autowired
    private PropertyService propertyService;


    @Test
    void shouldGetAllProperties() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        Property property = Property.builder()
                .rooms(2)
                .single(true)
                .city("szczecin")
                .postCode("11-111")
                .street("nowa")
                .house("15")
                .appUser(appUser)
                .build();

        initDatabaseWithPropertyAndUser(property, appUser);
        //when
        List<PropertyDto> properties = propertyService.getProperties();

        //then
        assertThat(properties).isNotEmpty();
        PropertyDto propertyDto = properties.get(0);
        assertThat(propertyDto.getId()).isNotNull();
        assertThat(propertyDto.getRooms()).isEqualTo(2);
        assertThat(propertyDto.getSingle()).isTrue();
        assertThat(propertyDto.getPostCode()).isEqualTo("11-111");
        assertThat(propertyDto.getStreet()).isEqualTo("nowa");
        assertThat(propertyDto.getHouse()).isEqualTo("15");

    }

    @Test
    void shouldAddProperty() {
        //given
        initDatabaseWithFirstUser();
        PropertyDto propertyDto = PropertyDto.builder()
                .rooms(1)
                .single(true)
                .city("Police")
                .postCode("62-121")
                .rooms(3)
                .street("Matyjaka")
                .house("6")
                .build();

        //when
        PropertyDto savedProperty = propertyService.addProperty(propertyDto);

        //then
        long repositoryCount = propertyRepository.count();
        assertThat(repositoryCount).isEqualTo(1);
        assertThat(savedProperty).isNotNull();
        assertThat(savedProperty.getId()).isNotNull();
    }
}
