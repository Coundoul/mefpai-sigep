package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.FiliereStabilise;

/**
 * Spring Data SQL reactive repository for the FiliereStabilise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FiliereStabiliseRepository extends R2dbcRepository<FiliereStabilise, Long>, FiliereStabiliseRepositoryInternal {
    Flux<FiliereStabilise> findAllBy(Pageable pageable);

    @Query("SELECT * FROM filiere_stabilise entity WHERE entity.nom_formateur_id = :id")
    Flux<FiliereStabilise> findByNomFormateur(Long id);

    @Query("SELECT * FROM filiere_stabilise entity WHERE entity.nom_formateur_id IS NULL")
    Flux<FiliereStabilise> findAllWhereNomFormateurIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<FiliereStabilise> findAll();

    @Override
    Mono<FiliereStabilise> findById(Long id);

    @Override
    <S extends FiliereStabilise> Mono<S> save(S entity);
}

interface FiliereStabiliseRepositoryInternal {
    <S extends FiliereStabilise> Mono<S> insert(S entity);
    <S extends FiliereStabilise> Mono<S> save(S entity);
    Mono<Integer> update(FiliereStabilise entity);

    Flux<FiliereStabilise> findAll();
    Mono<FiliereStabilise> findById(Long id);
    Flux<FiliereStabilise> findAllBy(Pageable pageable);
    Flux<FiliereStabilise> findAllBy(Pageable pageable, Criteria criteria);
}
