package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Etablissement;

/**
 * Spring Data SQL reactive repository for the Etablissement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtablissementRepository extends R2dbcRepository<Etablissement, Long>, EtablissementRepositoryInternal {
    Flux<Etablissement> findAllBy(Pageable pageable);

    @Query("SELECT * FROM etablissement entity WHERE entity.nom_quartier_id = :id")
    Flux<Etablissement> findByNomQuartier(Long id);

    @Query("SELECT * FROM etablissement entity WHERE entity.nom_quartier_id IS NULL")
    Flux<Etablissement> findAllWhereNomQuartierIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Etablissement> findAll();

    @Override
    Mono<Etablissement> findById(Long id);

    @Override
    <S extends Etablissement> Mono<S> save(S entity);
}

interface EtablissementRepositoryInternal {
    <S extends Etablissement> Mono<S> insert(S entity);
    <S extends Etablissement> Mono<S> save(S entity);
    Mono<Integer> update(Etablissement entity);

    Flux<Etablissement> findAll();
    Mono<Etablissement> findById(Long id);
    Flux<Etablissement> findAllBy(Pageable pageable);
    Flux<Etablissement> findAllBy(Pageable pageable, Criteria criteria);
}
