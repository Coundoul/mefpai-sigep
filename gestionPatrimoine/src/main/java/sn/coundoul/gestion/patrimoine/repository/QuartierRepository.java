package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Quartier;

/**
 * Spring Data SQL reactive repository for the Quartier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuartierRepository extends R2dbcRepository<Quartier, Long>, QuartierRepositoryInternal {
    Flux<Quartier> findAllBy(Pageable pageable);

    @Query("SELECT * FROM quartier entity WHERE entity.nom_commune_id = :id")
    Flux<Quartier> findByNomCommune(Long id);

    @Query("SELECT * FROM quartier entity WHERE entity.nom_commune_id IS NULL")
    Flux<Quartier> findAllWhereNomCommuneIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Quartier> findAll();

    @Override
    Mono<Quartier> findById(Long id);

    @Override
    <S extends Quartier> Mono<S> save(S entity);
}

interface QuartierRepositoryInternal {
    <S extends Quartier> Mono<S> insert(S entity);
    <S extends Quartier> Mono<S> save(S entity);
    Mono<Integer> update(Quartier entity);

    Flux<Quartier> findAll();
    Mono<Quartier> findById(Long id);
    Flux<Quartier> findAllBy(Pageable pageable);
    Flux<Quartier> findAllBy(Pageable pageable, Criteria criteria);
}
