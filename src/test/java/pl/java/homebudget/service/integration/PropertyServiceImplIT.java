package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.java.homebudget.dto.PropertyDto;
import pl.java.homebudget.service.PropertyService;
import pl.java.homebudget.service.init.InitDataForIT;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyServiceImplIT extends InitDataForIT {

    @Autowired
    private PropertyService propertyService;

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
        assertThat(savedProperty.getId()).isNotNull();

    }
}
