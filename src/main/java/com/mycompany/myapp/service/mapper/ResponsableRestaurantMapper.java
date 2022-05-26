package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ResponsableRestaurant;
import com.mycompany.myapp.service.dto.ResponsableRestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResponsableRestaurant} and its DTO {@link ResponsableRestaurantDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResponsableRestaurantMapper extends EntityMapper<ResponsableRestaurantDTO, ResponsableRestaurant> {}
