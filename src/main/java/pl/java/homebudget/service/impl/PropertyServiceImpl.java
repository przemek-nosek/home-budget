package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.PropertyDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Property;
import pl.java.homebudget.mapper.PropertyMapper;
import pl.java.homebudget.repository.PropertyRepository;
import pl.java.homebudget.service.PropertyService;

@Service
@AllArgsConstructor
@Slf4j
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper = Mappers.getMapper(PropertyMapper.class);
    private final UserLoggedInfo userLoggedInfo;


    @Override
    public PropertyDto addProperty(PropertyDto propertyDto) {
        log.info("addProperty");
        log.debug("propertyDto {}", propertyDto);

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        Property property = propertyMapper.fromDtoToProperty(propertyDto, loggedAppUser);

        Property savedProperty = propertyRepository.save(property);
        log.info("Property added");

        return propertyMapper.fromPropertyToDto(savedProperty);
    }
}
