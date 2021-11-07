package pl.java.homebudget.service;

import pl.java.homebudget.dto.PropertyDto;

import java.util.List;

public interface PropertyService {

    PropertyDto addProperty(PropertyDto propertyDto);

    List<PropertyDto> getAllProperties(boolean sold);

    PropertyDto updateProperty(PropertyDto propertyDto);

    void setSoldProperty(Long id);
}
