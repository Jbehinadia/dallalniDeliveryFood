package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Menu;
import com.mycompany.myapp.domain.Restaurant;
import com.mycompany.myapp.service.dto.MenuDTO;
import com.mycompany.myapp.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Menu} and its DTO {@link MenuDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuMapper extends EntityMapper<MenuDTO, Menu> {
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantNomRestaurant")
    MenuDTO toDto(Menu s);

    @Named("restaurantNomRestaurant")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomRestaurant", source = "nomRestaurant")
    RestaurantDTO toDtoRestaurantNomRestaurant(Restaurant restaurant);
}
