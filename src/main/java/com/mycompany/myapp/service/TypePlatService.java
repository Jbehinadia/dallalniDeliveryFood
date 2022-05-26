package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.TypePlatDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.TypePlat}.
 */
public interface TypePlatService {
    /**
     * Save a typePlat.
     *
     * @param typePlatDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TypePlatDTO> save(TypePlatDTO typePlatDTO);

    /**
     * Updates a typePlat.
     *
     * @param typePlatDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TypePlatDTO> update(TypePlatDTO typePlatDTO);

    /**
     * Partially updates a typePlat.
     *
     * @param typePlatDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TypePlatDTO> partialUpdate(TypePlatDTO typePlatDTO);

    /**
     * Get all the typePlats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TypePlatDTO> findAll(Pageable pageable);

    /**
     * Returns the number of typePlats available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" typePlat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TypePlatDTO> findOne(Long id);

    /**
     * Delete the "id" typePlat.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
