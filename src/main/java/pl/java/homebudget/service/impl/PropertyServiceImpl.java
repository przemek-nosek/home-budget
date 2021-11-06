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
import pl.java.homebudget.entity.Room;
import pl.java.homebudget.exception.PropertyNotFoundException;
import pl.java.homebudget.mapper.PropertyMapper;
import pl.java.homebudget.mapper.RoomMapper;
import pl.java.homebudget.repository.PropertyRepository;
import pl.java.homebudget.repository.RoomRepository;
import pl.java.homebudget.service.PropertyService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper = Mappers.getMapper(PropertyMapper.class);
    private final UserLoggedInfo userLoggedInfo;


    private final RoomMapper roomMapper;
    private final RoomRepository roomRepository;


    @Override
    public List<PropertyDto> getUnsoldProperties() {
        log.info("getUnsoldProperties");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return propertyRepository.findAllByAppUserAndSold(loggedAppUser, false).stream()
                .map(propertyMapper::fromPropertyToDto)
                .toList();
    }

    @Override
    public List<PropertyDto> getSoldProperties() {
        log.info("getSoldProperties");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return propertyRepository.findAllByAppUserAndSold(loggedAppUser, true).stream()
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
    public void deleteProperty(PropertyDto propertyDto) { // TODO: WRITE IT TESTS
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

    @Override
    @Transactional
    public PropertyDto updateProperty(PropertyDto propertyDto) {
        log.info("updateProperty");
        log.info("propertyDto {}", propertyDto);

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        Long id = propertyDto.getId();
        Property property = propertyRepository.findByIdAndAppUser(id, loggedAppUser)
                .orElseThrow(() -> new PropertyNotFoundException(String.format("Property with given id: %d not found", id)));


        List<Room> rooms = propertyDto.getRooms().stream()
                .map(roomDto -> roomRepository.findByIdAndAppUser(roomDto.getId(), loggedAppUser).get())
                .collect(Collectors.toList());


        property.getRooms().removeAll(rooms);

        List<Room> updatedRooms = roomMapper.updateRoomFromRoomDto(propertyDto.getRooms(), rooms);

        Property fromDto = propertyMapper.updatePropertyFromDto(propertyDto, updatedRooms, property);

        return propertyMapper.fromPropertyToDto(fromDto);
    }

    @Override
    @Transactional
    public void setSoldProperty(Long id) {
        log.info("setSoldProperty");
        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        Property property = propertyRepository.findByIdAndAppUser(id, loggedAppUser)
                .orElseThrow(() -> new PropertyNotFoundException(String.format("Property with given id: %d not found", id)));

        property.setSold(true);
    }
}
