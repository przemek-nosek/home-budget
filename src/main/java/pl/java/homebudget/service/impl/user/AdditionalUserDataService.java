package pl.java.homebudget.service.impl.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.java.homebudget.dto.AdditionalUserDataDto;
import pl.java.homebudget.entity.AdditionalUserData;
import pl.java.homebudget.entity.AppUser;
import pl.java.homebudget.mapper.AdditionalUserDataMapper;
import pl.java.homebudget.repository.AdditionalUserDataRepository;

@Service
@AllArgsConstructor
@Slf4j
public class AdditionalUserDataService {

    private final AdditionalUserDataRepository additionalUserDataRepository;
    private final UserLoggedInfoService userLoggedInfoService;
    private final AdditionalUserDataMapper additionalUserDataMapper;

    @Transactional
    public AdditionalUserDataDto addAdditionalUserData(AdditionalUserDataDto additionalUserDataDto) {
        log.info("addAdditionalUserData");
        log.debug("addAdditionalUserData -> {}", additionalUserDataDto);

        AppUser loggedAppUser = userLoggedInfoService.getLoggedAppUser();

        AdditionalUserData additionalUserData = additionalUserDataMapper.fromDtoToAdditionalUserData(additionalUserDataDto, loggedAppUser);

        AdditionalUserData savedAdditionalUserData = additionalUserDataRepository.save(additionalUserData);

        return additionalUserDataMapper.fromAdditionalUserDataToDto(savedAdditionalUserData);
    }

    public AdditionalUserDataDto getAdditionalUserData() {
        log.info("getAdditionalUserData");

        AppUser loggedAppUser = userLoggedInfoService.getLoggedAppUser();

        AdditionalUserData allByAppUser = additionalUserDataRepository.findAllByAppUser(loggedAppUser);

        return additionalUserDataMapper.fromAdditionalUserDataToDto(allByAppUser);
    }

    public AdditionalUserDataDto getAdditionalUserData(AppUser appUser) {
        log.info("getAdditionalUserData");

        AdditionalUserData allByAppUser = additionalUserDataRepository.findAllByAppUser(appUser);

        return additionalUserDataMapper.fromAdditionalUserDataToDto(allByAppUser);
    }
}
