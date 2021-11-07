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
    public List<PropertyDto> getAllProperties(boolean sold) {
        log.info("getUnsoldProperties");

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        return propertyRepository.findAllByAppUserAndSold(loggedAppUser, sold).stream()
                .map(propertyMapper::fromPropertyToDto)
                .toList();
    }


    @Override
    public PropertyDto addProperty(PropertyDto propertyDto) {
        log.info("addProperty");
        log.debug("propertyDto {}", propertyDto);

        AppUser loggedAppUser = userLoggedInfo.getLoggedAppUser();

        Property property = propertyMapper.fromDtoToProperty(propertyDto, loggedAppUser);
        property.getRooms().forEach(room -> room.setAppUser(loggedAppUser));
        Property savedProperty = propertyRepository.save(property);
        log.info("Property added");

        return propertyMapper.fromPropertyToDto(savedProperty);
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
        updatedRooms.forEach(room -> room.setAppUser(loggedAppUser));

        roomRepository.saveAll(updatedRooms);

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
