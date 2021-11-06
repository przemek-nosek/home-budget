package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.java.homebudget.dto.PropertyDto;
import pl.java.homebudget.dto.RoomDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Property;
import pl.java.homebudget.entity.Room;
import pl.java.homebudget.enums.RoomSize;
import pl.java.homebudget.service.PropertyService;
import pl.java.homebudget.service.RoomService;
import pl.java.homebudget.service.init.InitDataForIT;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class PropertyServiceImplIT extends InitDataForIT {

    @Autowired
    private PropertyService propertyService;

    @Test
    void shouldGetAllUnsoldProperties() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        initDatabaseWithPropertyAndUser(appUser, false);
        initDatabaseWithPropertyAndUser(appUser, false);
        initDatabaseWithPropertyAndUser(appUser, false);
        initDatabaseWithPropertyAndUser(appUser, true);

        //when
        List<PropertyDto> unsoldProperties = propertyService.getUnsoldProperties();

        //then
        assertThat(unsoldProperties).hasSize(3);
    }

    @Test
    void shouldGetAllSoldProperties() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        initDatabaseWithPropertyAndUser(appUser, false);
        initDatabaseWithPropertyAndUser(appUser, false);
        initDatabaseWithPropertyAndUser(appUser, true);
        initDatabaseWithPropertyAndUser(appUser, true);

        //when
        List<PropertyDto> soldProperties = propertyService.getSoldProperties();

        //then
        assertThat(soldProperties).hasSize(2);
    }

    @Test
    void shouldAddProperty() {
        //given
        initDatabaseWithFirstUser();
        PropertyDto propertyDto = new PropertyDto(1L, new ArrayList<>(), true, "szczecin", "15", "ulica", "house", false);

        //when
        PropertyDto savedProperty = propertyService.addProperty(propertyDto);

        //then
        assertThat(savedProperty.getPostCode()).isEqualTo(propertyDto.getPostCode());
        assertThat(savedProperty.getSingle()).isEqualTo(propertyDto.getSingle());
        assertThat(savedProperty.getHouse()).isEqualTo(propertyDto.getHouse());
        assertThat(savedProperty.getPostCode()).isEqualTo(propertyDto.getPostCode());
        assertThat(savedProperty.getCity()).isEqualTo(propertyDto.getCity());
    }

    @Test
    void shouldDeleteProperty() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        initDatabaseWithPropertyAndUser(appUser, false);
        assertThat(propertyRepository.count()).isEqualTo(1L);
        Property property = propertyRepository.findAllByAppUser(appUser).get(0);

        PropertyDto propertyDto = new PropertyDto(
                property.getId(),
                new ArrayList<>(),
                property.getSingle(),
                property.getCity(),
                property.getPostCode(),
                property.getStreet(),
                property.getHouse(),
                false
        );


        //when
        propertyService.deleteProperty(propertyDto);

        //then
        assertThat(propertyRepository.count()).isEqualTo(0L);
    }

    @Test
    void shouldUpdateProperty() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        initDatabaseWithPropertyAndUser(appUser, false);
        Room room = initDatabaseWithRoomAndUser(appUser);
        assertThat(propertyRepository.count()).isEqualTo(1L);
        Property property = propertyRepository.findAllByAppUser(appUser).get(0);

        PropertyDto propertyDto = new PropertyDto(
                property.getId(),
                List.of(new RoomDto(room.getId(), RoomSize.ROOM_S, BigDecimal.TEN)),
                property.getSingle(),
                "szczecin miasto",
                property.getPostCode(),
                property.getStreet(),
                "house house",
                false
        );

        //when
        PropertyDto updatedProperty = propertyService.updateProperty(propertyDto);

        //then
        assertThat(updatedProperty.getId()).isEqualTo(propertyDto.getId());
        assertThat(updatedProperty.getCity()).isEqualTo(propertyDto.getCity());
        assertThat(updatedProperty.getHouse()).isEqualTo(propertyDto.getHouse());
        assertThat(updatedProperty.getRooms().size()).isEqualTo(1);

    }


    @Test
    void name() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();

        Room room = new Room(appUser, RoomSize.ROOM_XS, BigDecimal.ZERO);
        Room room2 = new Room(appUser, RoomSize.ROOM_L, BigDecimal.ONE);
        Room room3 = new Room(appUser, RoomSize.ROOM_XL, BigDecimal.TEN);
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        rooms.add(room2);
        rooms.add(room3);

        Property property = new Property(appUser, rooms, true, "city", "postCode", "street", "house", false);

        initDatabaseWithPropertyAndUser(property);

        Room room4 = roomRepository.findAllByAppUser(appUser)
                .stream().filter(room1 -> room1.getCost().equals(BigDecimal.ZERO))
                .collect(Collectors.toList()).get(0);

        Room room5 = roomRepository.findAllByAppUser(appUser)
                .stream().filter(room1 -> room1.getCost().equals(BigDecimal.ONE))
                .collect(Collectors.toList()).get(0);


        RoomDto roomDto = new RoomDto(room4.getId(), room4.getRoomSize(), BigDecimal.valueOf(1000000));
        RoomDto roomDto2 = new RoomDto(room5.getId(), room5.getRoomSize(), BigDecimal.valueOf(9990000));
        List<RoomDto> roomDtoList = new ArrayList<>();
        roomDtoList.add(roomDto);
        roomDtoList.add(roomDto2);

        PropertyDto propertyDto = new PropertyDto(property.getId(), roomDtoList, property.getSingle(),
                property.getCity(), property.getPostCode(), property.getStreet(), property.getHouse(), property.getSold());

        //when
        PropertyDto updateProperty = propertyService.updateProperty(propertyDto);
    }

}
