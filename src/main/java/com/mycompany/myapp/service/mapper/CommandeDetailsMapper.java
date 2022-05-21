package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Commande;
import com.mycompany.myapp.domain.CommandeDetails;
import com.mycompany.myapp.domain.Plat;
import com.mycompany.myapp.service.dto.CommandeDTO;
import com.mycompany.myapp.service.dto.CommandeDetailsDTO;
import com.mycompany.myapp.service.dto.PlatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommandeDetails} and its DTO {@link CommandeDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommandeDetailsMapper extends EntityMapper<CommandeDetailsDTO, CommandeDetails> {
    @Mapping(target = "commande", source = "commande", qualifiedByName = "commandeId")
    @Mapping(target = "plat", source = "plat", qualifiedByName = "platNomPlat")
    CommandeDetailsDTO toDto(CommandeDetails s);

    @Named("commandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommandeDTO toDtoCommandeId(Commande commande);

    @Named("platNomPlat")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomPlat", source = "nomPlat")
    PlatDTO toDtoPlatNomPlat(Plat plat);
}
