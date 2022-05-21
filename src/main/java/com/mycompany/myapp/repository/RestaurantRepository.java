package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Restaurant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantRepository extends ReactiveCrudRepository<Restaurant, Long>, RestaurantRepositoryInternal {
    Flux<Restaurant> findAllBy(Pageable pageable);

    @Override
    Mono<Restaurant> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Restaurant> findAllWithEagerRelationships();

    @Override
    Flux<Restaurant> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM restaurant entity WHERE entity.responsable_restaurant_id = :id")
    Flux<Restaurant> findByResponsableRestaurant(Long id);

    @Query("SELECT * FROM restaurant entity WHERE entity.responsable_restaurant_id IS NULL")
    Flux<Restaurant> findAllWhereResponsableRestaurantIsNull();

    @Override
    <S extends Restaurant> Mono<S> save(S entity);

    @Override
    Flux<Restaurant> findAll();

    @Override
    Mono<Restaurant> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface RestaurantRepositoryInternal {
    <S extends Restaurant> Mono<S> save(S entity);

    Flux<Restaurant> findAllBy(Pageable pageable);

    Flux<Restaurant> findAll();

    Mono<Restaurant> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Restaurant> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Restaurant> findOneWithEagerRelationships(Long id);

    Flux<Restaurant> findAllWithEagerRelationships();

    Flux<Restaurant> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
