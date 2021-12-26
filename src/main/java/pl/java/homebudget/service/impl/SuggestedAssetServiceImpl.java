package pl.java.homebudget.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.java.homebudget.dto.SuggestedAssetDto;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.entity.Property;
import pl.java.homebudget.enums.AssetCategory;
import pl.java.homebudget.repository.PropertyRepository;
import pl.java.homebudget.service.SuggestedAssetService;
import pl.java.homebudget.service.impl.user.UserLoggedInfoService;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SuggestedAssetServiceImpl implements SuggestedAssetService {

    private final PropertyRepository propertyRepository;
    private final UserLoggedInfoService userLoggedInfoService;

    public List<SuggestedAssetDto> getAllSuggestedAssets(boolean rent) {
        log.info("getAllSuggestedAssets");
        AppUser loggedAppUser = userLoggedInfoService.getLoggedAppUser();

        List<Property> properties = propertyRepository.findAllByAppUser(loggedAppUser);

        return properties.stream()
                .map(property -> property.getRooms().stream()
                        .filter(room -> room.getRent().equals(rent))
                        .map(room -> new SuggestedAssetDto(
                                room.getCost(),
                                AssetCategory.RENT,
                                setDescription(property))
                        ).collect(toList())
                ).flatMap(Collection::stream)
                .toList();
    }

    private String setDescription(Property property) {

        return "City: " + property.getCity() + ", Street: " + property.getStreet() + ", House: " + property.getHouse();
    }
}
