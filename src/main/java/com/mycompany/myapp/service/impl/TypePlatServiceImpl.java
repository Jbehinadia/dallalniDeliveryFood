package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.TypePlat;
import com.mycompany.myapp.repository.TypePlatRepository;
import com.mycompany.myapp.service.TypePlatService;
import com.mycompany.myapp.service.dto.TypePlatDTO;
import com.mycompany.myapp.service.mapper.TypePlatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TypePlat}.
 */
@Service
@Transactional
public class TypePlatServiceImpl implements TypePlatService {

    private final Logger log = LoggerFactory.getLogger(TypePlatServiceImpl.class);

    private final TypePlatRepository typePlatRepository;

    private final TypePlatMapper typePlatMapper;

    public TypePlatServiceImpl(TypePlatRepository typePlatRepository, TypePlatMapper typePlatMapper) {
        this.typePlatRepository = typePlatRepository;
        this.typePlatMapper = typePlatMapper;
    }

    @Override
    public Mono<TypePlatDTO> save(TypePlatDTO typePlatDTO) {
        log.debug("Request to save TypePlat : {}", typePlatDTO);
        return typePlatRepository.save(typePlatMapper.toEntity(typePlatDTO)).map(typePlatMapper::toDto);
    }

    @Override
    public Mono<TypePlatDTO> update(TypePlatDTO typePlatDTO) {
        log.debug("Request to save TypePlat : {}", typePlatDTO);
        return typePlatRepository.save(typePlatMapper.toEntity(typePlatDTO)).map(typePlatMapper::toDto);
    }

    @Override
    public Mono<TypePlatDTO> partialUpdate(TypePlatDTO typePlatDTO) {
        log.debug("Request to partially update TypePlat : {}", typePlatDTO);

        return typePlatRepository
            .findById(typePlatDTO.getId())
            .map(existingTypePlat -> {
                typePlatMapper.partialUpdate(existingTypePlat, typePlatDTO);

                return existingTypePlat;
            })
            .flatMap(typePlatRepository::save)
            .map(typePlatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TypePlatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypePlats");
        return typePlatRepository.findAllBy(pageable).map(typePlatMapper::toDto);
    }

    public Mono<Long> countAll() {
        return typePlatRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TypePlatDTO> findOne(Long id) {
        log.debug("Request to get TypePlat : {}", id);
        return typePlatRepository.findById(id).map(typePlatMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TypePlat : {}", id);
        return typePlatRepository.deleteById(id);
    }
}
