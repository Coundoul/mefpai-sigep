package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Attribution;

/**
 * Spring Data SQL reactive repository for the Attribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributionRepository extends R2dbcRepository<Attribution, Long>, AttributionRepositoryInternal {
    Flux<Attribution> findAllBy(Pageable pageable);

    @Query("SELECT * FROM attribution entity WHERE entity.nom_utilisateur_id = :id")
    Flux<Attribution> findByNomUtilisateur(Long id);

    @Query("SELECT * FROM attribution entity WHERE entity.nom_utilisateur_id IS NULL")
    Flux<Attribution> findAllWhereNomUtilisateurIsNull();

    @Query("SELECT * FROM attribution entity WHERE entity.affectations_id = :id")
    Flux<Attribution> findByAffectations(Long id);

    @Query("SELECT * FROM attribution entity WHERE entity.affectations_id IS NULL")
    Flux<Attribution> findAllWhereAffectationsIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Attribution> findAll();

    @Override
    Mono<Attribution> findById(Long id);

    @Override
    <S extends Attribution> Mono<S> save(S entity);
}

interface AttributionRepositoryInternal {
    <S extends Attribution> Mono<S> insert(S entity);
    <S extends Attribution> Mono<S> save(S entity);
    Mono<Integer> update(Attribution entity);

    Flux<Attribution> findAll();
    Mono<Attribution> findById(Long id);
    Flux<Attribution> findAllBy(Pageable pageable);
    Flux<Attribution> findAllBy(Pageable pageable, Criteria criteria);
}
