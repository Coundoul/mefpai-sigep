package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Affectations;

/**
 * Spring Data SQL reactive repository for the Affectations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffectationsRepository extends R2dbcRepository<Affectations, Long>, AffectationsRepositoryInternal {
    Flux<Affectations> findAllBy(Pageable pageable);

    @Query("SELECT * FROM affectations entity WHERE entity.equipement_id = :id")
    Flux<Affectations> findByEquipement(Long id);

    @Query("SELECT * FROM affectations entity WHERE entity.equipement_id IS NULL")
    Flux<Affectations> findAllWhereEquipementIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Affectations> findAll();

    @Override
    Mono<Affectations> findById(Long id);

    @Override
    <S extends Affectations> Mono<S> save(S entity);
}

interface AffectationsRepositoryInternal {
    <S extends Affectations> Mono<S> insert(S entity);
    <S extends Affectations> Mono<S> save(S entity);
    Mono<Integer> update(Affectations entity);

    Flux<Affectations> findAll();
    Mono<Affectations> findById(Long id);
    Flux<Affectations> findAllBy(Pageable pageable);
    Flux<Affectations> findAllBy(Pageable pageable, Criteria criteria);
}
