package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TypePlat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the TypePlat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypePlatRepository extends ReactiveCrudRepository<TypePlat, Long>, TypePlatRepositoryInternal {
    Flux<TypePlat> findAllBy(Pageable pageable);

    @Override
    <S extends TypePlat> Mono<S> save(S entity);

    @Override
    Flux<TypePlat> findAll();

    @Override
    Mono<TypePlat> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TypePlatRepositoryInternal {
    <S extends TypePlat> Mono<S> save(S entity);

    Flux<TypePlat> findAllBy(Pageable pageable);

    Flux<TypePlat> findAll();

    Mono<TypePlat> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TypePlat> findAllBy(Pageable pageable, Criteria criteria);

}
