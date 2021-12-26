package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.java.homebudget.dto.AdditionalUserDataDto;
import pl.java.homebudget.service.impl.user.AdditionalUserDataService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/additional")
public class AdditionalUserDataController {

    private final AdditionalUserDataService additionalUserDataService;

    @GetMapping
    public AdditionalUserDataDto getAdditionalUserData() {
        return additionalUserDataService.getAdditionalUserData();
    }

    @PostMapping
    public AdditionalUserDataDto addAdditionalUserDataDto(AdditionalUserDataDto additionalUserDataDto) {
        return additionalUserDataService.addAdditionalUserData(additionalUserDataDto);
    }
}
