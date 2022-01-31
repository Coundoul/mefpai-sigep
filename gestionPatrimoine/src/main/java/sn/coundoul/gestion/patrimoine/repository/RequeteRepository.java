package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Requete;

/**
 * Spring Data SQL reactive repository for the Requete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequeteRepository extends R2dbcRepository<Requete, Long>, RequeteRepositoryInternal {
    Flux<Requete> findAllBy(Pageable pageable);

    @Query("SELECT * FROM requete entity WHERE entity.nom_structure_id = :id")
    Flux<Requete> findByNomStructure(Long id);

    @Query("SELECT * FROM requete entity WHERE entity.nom_structure_id IS NULL")
    Flux<Requete> findAllWhereNomStructureIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Requete> findAll();

    @Override
    Mono<Requete> findById(Long id);

    @Override
    <S extends Requete> Mono<S> save(S entity);
}

interface RequeteRepositoryInternal {
    <S extends Requete> Mono<S> insert(S entity);
    <S extends Requete> Mono<S> save(S entity);
    Mono<Integer> update(Requete entity);

    Flux<Requete> findAll();
    Mono<Requete> findById(Long id);
    Flux<Requete> findAllBy(Pageable pageable);
    Flux<Requete> findAllBy(Pageable pageable, Criteria criteria);
}
