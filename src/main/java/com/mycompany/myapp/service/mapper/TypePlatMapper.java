package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.TypePlat;
import com.mycompany.myapp.service.dto.TypePlatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypePlat} and its DTO {@link TypePlatDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypePlatMapper extends EntityMapper<TypePlatDTO, TypePlat> {}
