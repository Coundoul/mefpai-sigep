package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.ComptableSecondaire;

/**
 * Spring Data SQL reactive repository for the ComptableSecondaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComptableSecondaireRepository extends R2dbcRepository<ComptableSecondaire, Long>, ComptableSecondaireRepositoryInternal {
    Flux<ComptableSecondaire> findAllBy(Pageable pageable);

    @Query("SELECT * FROM comptable_secondaire entity WHERE entity.comptable_principale_id = :id")
    Flux<ComptableSecondaire> findByComptablePrincipale(Long id);

    @Query("SELECT * FROM comptable_secondaire entity WHERE entity.comptable_principale_id IS NULL")
    Flux<ComptableSecondaire> findAllWhereComptablePrincipaleIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<ComptableSecondaire> findAll();

    @Override
    Mono<ComptableSecondaire> findById(Long id);

    @Override
    <S extends ComptableSecondaire> Mono<S> save(S entity);
}

interface ComptableSecondaireRepositoryInternal {
    <S extends ComptableSecondaire> Mono<S> insert(S entity);
    <S extends ComptableSecondaire> Mono<S> save(S entity);
    Mono<Integer> update(ComptableSecondaire entity);

    Flux<ComptableSecondaire> findAll();
    Mono<ComptableSecondaire> findById(Long id);
    Flux<ComptableSecondaire> findAllBy(Pageable pageable);
    Flux<ComptableSecondaire> findAllBy(Pageable pageable, Criteria criteria);
}
