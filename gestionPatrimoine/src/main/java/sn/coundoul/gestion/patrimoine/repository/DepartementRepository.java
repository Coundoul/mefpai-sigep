package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Departement;

/**
 * Spring Data SQL reactive repository for the Departement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartementRepository extends R2dbcRepository<Departement, Long>, DepartementRepositoryInternal {
    Flux<Departement> findAllBy(Pageable pageable);

    @Query("SELECT * FROM departement entity WHERE entity.nom_region_id = :id")
    Flux<Departement> findByNomRegion(Long id);

    @Query("SELECT * FROM departement entity WHERE entity.nom_region_id IS NULL")
    Flux<Departement> findAllWhereNomRegionIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Departement> findAll();

    @Override
    Mono<Departement> findById(Long id);

    @Override
    <S extends Departement> Mono<S> save(S entity);
}

interface DepartementRepositoryInternal {
    <S extends Departement> Mono<S> insert(S entity);
    <S extends Departement> Mono<S> save(S entity);
    Mono<Integer> update(Departement entity);

    Flux<Departement> findAll();
    Mono<Departement> findById(Long id);
    Flux<Departement> findAllBy(Pageable pageable);
    Flux<Departement> findAllBy(Pageable pageable, Criteria criteria);
}
