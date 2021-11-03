package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.PropertyDto;
import pl.java.homebudget.dto.UserLoggedInfo;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Property;
import pl.java.homebudget.exception.PropertyNotFoundException;
import pl.java.homebudget.mapper.PropertyMapper;
import pl.java.homebudget.repository.PropertyRepository;
import pl.java.homebudget.service.PropertyService;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper = Mappers.getMapper(PropertyMapper.class);
    private final UserLoggedInfo userLoggedInfo;


    @Override
    public List<PropertyDto> getProperties() {
        log.info("getProperties");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return propertyRepository.findAllByAppUser(loggedAppUser).stream()
                .map(propertyMapper::fromPropertyToDto)
                .toList();
    }

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

    @Override
    @Transactional
    public void deleteProperty(PropertyDto propertyDto) {
        log.info("deleteProperty");
        log.debug("propertyDto {}", propertyDto);

        Long id = propertyDto.getId();
        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        boolean existsById = propertyRepository.existsByIdAndAppUser(id, loggedAppUser);

        if (!existsById) {
            throw new PropertyNotFoundException(String.format("Property with given id: %d not found", id));
        }

        Property property = propertyMapper.fromDtoToProperty(propertyDto, loggedAppUser);

        propertyRepository.delete(property);
    }

}
