package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Projets;

/**
 * Spring Data SQL reactive repository for the Projets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjetsRepository extends R2dbcRepository<Projets, Long>, ProjetsRepositoryInternal {
    Flux<Projets> findAllBy(Pageable pageable);

    @Query("SELECT * FROM projets entity WHERE entity.nom_id = :id")
    Flux<Projets> findByNom(Long id);

    @Query("SELECT * FROM projets entity WHERE entity.nom_id IS NULL")
    Flux<Projets> findAllWhereNomIsNull();

    @Query("SELECT * FROM projets entity WHERE entity.nom_etablissement_id = :id")
    Flux<Projets> findByNomEtablissement(Long id);

    @Query("SELECT * FROM projets entity WHERE entity.nom_etablissement_id IS NULL")
    Flux<Projets> findAllWhereNomEtablissementIsNull();

    @Query("SELECT * FROM projets entity WHERE entity.nom_batiment_id = :id")
    Flux<Projets> findByNomBatiment(Long id);

    @Query("SELECT * FROM projets entity WHERE entity.nom_batiment_id IS NULL")
    Flux<Projets> findAllWhereNomBatimentIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Projets> findAll();

    @Override
    Mono<Projets> findById(Long id);

    @Override
    <S extends Projets> Mono<S> save(S entity);
}

interface ProjetsRepositoryInternal {
    <S extends Projets> Mono<S> insert(S entity);
    <S extends Projets> Mono<S> save(S entity);
    Mono<Integer> update(Projets entity);

    Flux<Projets> findAll();
    Mono<Projets> findById(Long id);
    Flux<Projets> findAllBy(Pageable pageable);
    Flux<Projets> findAllBy(Pageable pageable, Criteria criteria);
}
