package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TypePlatRepository;
import com.mycompany.myapp.service.TypePlatService;
import com.mycompany.myapp.service.dto.TypePlatDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.TypePlat}.
 */
@RestController
@RequestMapping("/api")
public class TypePlatResource {

    private final Logger log = LoggerFactory.getLogger(TypePlatResource.class);

    private static final String ENTITY_NAME = "typePlat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypePlatService typePlatService;

    private final TypePlatRepository typePlatRepository;

    public TypePlatResource(TypePlatService typePlatService, TypePlatRepository typePlatRepository) {
        this.typePlatService = typePlatService;
        this.typePlatRepository = typePlatRepository;
    }

    /**
     * {@code POST  /type-plats} : Create a new typePlat.
     *
     * @param typePlatDTO the typePlatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typePlatDTO, or with status {@code 400 (Bad Request)} if the typePlat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-plats")
    public Mono<ResponseEntity<TypePlatDTO>> createTypePlat(@RequestBody TypePlatDTO typePlatDTO) throws URISyntaxException {
        log.debug("REST request to save TypePlat : {}", typePlatDTO);
        if (typePlatDTO.getId() != null) {
            throw new BadRequestAlertException("A new typePlat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return typePlatService
            .save(typePlatDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/type-plats/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /type-plats/:id} : Updates an existing typePlat.
     *
     * @param id the id of the typePlatDTO to save.
     * @param typePlatDTO the typePlatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePlatDTO,
     * or with status {@code 400 (Bad Request)} if the typePlatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typePlatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-plats/{id}")
    public Mono<ResponseEntity<TypePlatDTO>> updateTypePlat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypePlatDTO typePlatDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypePlat : {}, {}", id, typePlatDTO);
        if (typePlatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePlatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typePlatRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return typePlatService
                    .update(typePlatDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /type-plats/:id} : Partial updates given fields of an existing typePlat, field will ignore if it is null
     *
     * @param id the id of the typePlatDTO to save.
     * @param typePlatDTO the typePlatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePlatDTO,
     * or with status {@code 400 (Bad Request)} if the typePlatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typePlatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typePlatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-plats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TypePlatDTO>> partialUpdateTypePlat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypePlatDTO typePlatDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypePlat partially : {}, {}", id, typePlatDTO);
        if (typePlatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePlatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typePlatRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TypePlatDTO> result = typePlatService.partialUpdate(typePlatDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /type-plats} : get all the typePlats.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typePlats in body.
     */
    @GetMapping("/type-plats")
    public Mono<ResponseEntity<List<TypePlatDTO>>> getAllTypePlats(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of TypePlats");
        return typePlatService
            .countAll()
            .zipWith(typePlatService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /type-plats/:id} : get the "id" typePlat.
     *
     * @param id the id of the typePlatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typePlatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-plats/{id}")
    public Mono<ResponseEntity<TypePlatDTO>> getTypePlat(@PathVariable Long id) {
        log.debug("REST request to get TypePlat : {}", id);
        Mono<TypePlatDTO> typePlatDTO = typePlatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typePlatDTO);
    }

    /**
     * {@code DELETE  /type-plats/:id} : delete the "id" typePlat.
     *
     * @param id the id of the typePlatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-plats/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTypePlat(@PathVariable Long id) {
        log.debug("REST request to delete TypePlat : {}", id);
        return typePlatService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
