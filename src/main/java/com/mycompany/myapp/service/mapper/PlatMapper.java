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
    @Mapping(target = "menu", source = "menu", qualifiedByName = "menuNomMenu")
    @Mapping(target = "typePlat", source = "typePlat", qualifiedByName = "typePlatType")
    PlatDTO toDto(Plat s);

    @Named("menuNomMenu")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomMenu", source = "nomMenu")
    MenuDTO toDtoMenuNomMenu(Menu menu);

    @Named("typePlatType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    TypePlatDTO toDtoTypePlatType(TypePlat typePlat);
}
