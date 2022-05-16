package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Menu;
import com.mycompany.myapp.domain.Plat;
import com.mycompany.myapp.domain.TypePlat;
import com.mycompany.myapp.service.dto.MenuDTO;
import com.mycompany.myapp.service.dto.PlatDTO;
import com.mycompany.myapp.service.dto.TypePlatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plat} and its DTO {@link PlatDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlatMapper extends EntityMapper<PlatDTO, Plat> {
    @Mapping(target = "menu", source = "menu", qualifiedByName = "menuId")
    @Mapping(target = "typePlat", source = "typePlat", qualifiedByName = "typePlatId")
    PlatDTO toDto(Plat s);

    @Named("menuId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuDTO toDtoMenuId(Menu menu);

    @Named("typePlatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypePlatDTO toDtoTypePlatId(TypePlat typePlat);
}
