package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommandeDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the CommandeDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandeDetailsRepository extends ReactiveCrudRepository<CommandeDetails, Long>, CommandeDetailsRepositoryInternal {
    Flux<CommandeDetails> findAllBy(Pageable pageable);

    @Query("SELECT * FROM commande_details entity WHERE entity.commande_id = :id")
    Flux<CommandeDetails> findByCommande(Long id);

    @Query("SELECT * FROM commande_details entity WHERE entity.commande_id IS NULL")
    Flux<CommandeDetails> findAllWhereCommandeIsNull();

    @Query("SELECT * FROM commande_details entity WHERE entity.plat_id = :id")
    Flux<CommandeDetails> findByPlat(Long id);

    @Query("SELECT * FROM commande_details entity WHERE entity.plat_id IS NULL")
    Flux<CommandeDetails> findAllWherePlatIsNull();

    @Override
    <S extends CommandeDetails> Mono<S> save(S entity);

    @Override
    Flux<CommandeDetails> findAll();

    @Override
    Mono<CommandeDetails> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CommandeDetailsRepositoryInternal {
    <S extends CommandeDetails> Mono<S> save(S entity);

    Flux<CommandeDetails> findAllBy(Pageable pageable);

    Flux<CommandeDetails> findAll();

    Mono<CommandeDetails> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CommandeDetails> findAllBy(Pageable pageable, Criteria criteria);

}
