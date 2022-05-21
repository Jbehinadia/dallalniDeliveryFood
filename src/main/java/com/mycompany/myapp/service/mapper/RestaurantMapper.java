package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ResponsableRestaurant;
import com.mycompany.myapp.domain.Restaurant;
import com.mycompany.myapp.service.dto.ResponsableRestaurantDTO;
import com.mycompany.myapp.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restaurant} and its DTO {@link RestaurantDTO}.
 */
@Mapper(componentModel = "spring")
public interface RestaurantMapper extends EntityMapper<RestaurantDTO, Restaurant> {
    @Mapping(target = "responsableRestaurant", source = "responsableRestaurant", qualifiedByName = "responsableRestaurantNomResponsable")
    RestaurantDTO toDto(Restaurant s);

    @Named("responsableRestaurantNomResponsable")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomResponsable", source = "nomResponsable")
    ResponsableRestaurantDTO toDtoResponsableRestaurantNomResponsable(ResponsableRestaurant responsableRestaurant);
}
