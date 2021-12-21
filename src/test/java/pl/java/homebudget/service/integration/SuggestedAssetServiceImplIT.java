package pl.java.homebudget.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.java.homebudget.dto.SuggestedAssetDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Property;
import pl.java.homebudget.entity.Room;
import pl.java.homebudget.enums.RoomSize;
import pl.java.homebudget.service.SuggestedAssetService;
import pl.java.homebudget.service.init.InitDataForIT;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class SuggestedAssetServiceImplIT extends InitDataForIT {

    @Autowired
    private SuggestedAssetService suggestedAssetService;

    @Test
    void shouldGetAllSuggestedAssets_notRent() {
        //given
        AppUser appUser = initDatabaseWithFirstUser();
        Room room = new Room(appUser, RoomSize.ROOM_XL, BigDecimal.TEN, false);
        Room room2 = new Room(appUser, RoomSize.ROOM_XXL, BigDecimal.ZERO, false);
        Room room3 = new Room(appUser, RoomSize.ROOM_L, BigDecimal.ONE, true);
        List<Room> rooms = initDatabaseWithRoomsAndUser(asList(room, room2, room3));
        Property property = new Property(appUser, rooms, true, "city", "postCode", "street", "house", false);

        Property savedProperty = initDatabaseWithPropertyAndUser(property);
        assertThat(roomRepository.count()).isEqualTo(3L);

        //when
        List<SuggestedAssetDto> allSuggestedAssets = suggestedAssetService.getAllSuggestedAssets(false);

        //then
        assertThat(allSuggestedAssets).hasSize(2);

    }
}
