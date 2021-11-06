package pl.java.homebudget.service;

import pl.java.homebudget.dto.PropertyDto;

import java.util.List;

public interface PropertyService {

    PropertyDto addProperty(PropertyDto propertyDto);

    List<PropertyDto> getUnsoldProperties();

    void deleteProperty(PropertyDto propertyDto);

    PropertyDto updateProperty(PropertyDto propertyDto);

    void setSoldProperty(Long id);

    List<PropertyDto> getSoldProperties();
}