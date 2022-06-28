package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Salles;

/**
 * Spring Data SQL reactive repository for the Salles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SallesRepository extends R2dbcRepository<Salles, Long>, SallesRepositoryInternal {
    Flux<Salles> findAllBy(Pageable pageable);

    @Query("SELECT * FROM salles entity WHERE entity.nom_batiment_id = :id")
    Flux<Salles> findByNomBatiment(Long id);

    @Query("SELECT * FROM salles entity WHERE entity.nom_batiment_id IS NULL")
    Flux<Salles> findAllWhereNomBatimentIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Salles> findAll();

    @Override
    Mono<Salles> findById(Long id);

    @Override
    <S extends Salles> Mono<S> save(S entity);
}

interface SallesRepositoryInternal {
    <S extends Salles> Mono<S> insert(S entity);
    <S extends Salles> Mono<S> save(S entity);
    Mono<Integer> update(Salles entity);

    Flux<Salles> findAll();
    Mono<Salles> findById(Long id);
    Flux<Salles> findAllBy(Pageable pageable);
    Flux<Salles> findAllBy(Pageable pageable, Criteria criteria);
}
