package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.java.homebudget.dto.PropertyDto;
import pl.java.homebudget.service.PropertyService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
@AllArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PropertyDto>> getProperties() {
        List<PropertyDto> propertyDtoList = propertyService.getProperties();

        return new ResponseEntity<>(propertyDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PropertyDto> addProperty(@RequestBody @Valid PropertyDto propertyDto) {
        PropertyDto savedProperty = propertyService.addProperty(propertyDto);

        return new ResponseEntity<>(savedProperty, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<PropertyDto> updateProperty(@RequestBody PropertyDto propertyDto) {
        PropertyDto updatedProperty = propertyService.updateProperty(propertyDto);

        return new ResponseEntity<>(updatedProperty, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProperty(@RequestBody PropertyDto propertyDto) {
        propertyService.deleteProperty(propertyDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
