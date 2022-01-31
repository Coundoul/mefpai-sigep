package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.FicheTechnique;

/**
 * Spring Data SQL reactive repository for the FicheTechnique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FicheTechniqueRepository extends R2dbcRepository<FicheTechnique, Long>, FicheTechniqueRepositoryInternal {
    Flux<FicheTechnique> findAllBy(Pageable pageable);

    @Query("SELECT * FROM fiche_technique entity WHERE entity.nom_responsable_id = :id")
    Flux<FicheTechnique> findByNomResponsable(Long id);

    @Query("SELECT * FROM fiche_technique entity WHERE entity.nom_responsable_id IS NULL")
    Flux<FicheTechnique> findAllWhereNomResponsableIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<FicheTechnique> findAll();

    @Override
    Mono<FicheTechnique> findById(Long id);

    @Override
    <S extends FicheTechnique> Mono<S> save(S entity);
}

interface FicheTechniqueRepositoryInternal {
    <S extends FicheTechnique> Mono<S> insert(S entity);
    <S extends FicheTechnique> Mono<S> save(S entity);
    Mono<Integer> update(FicheTechnique entity);

    Flux<FicheTechnique> findAll();
    Mono<FicheTechnique> findById(Long id);
    Flux<FicheTechnique> findAllBy(Pageable pageable);
    Flux<FicheTechnique> findAllBy(Pageable pageable, Criteria criteria);
}
