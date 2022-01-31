package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Atelier;

/**
 * Spring Data SQL reactive repository for the Atelier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtelierRepository extends R2dbcRepository<Atelier, Long>, AtelierRepositoryInternal {
    Flux<Atelier> findAllBy(Pageable pageable);

    @Query("SELECT * FROM atelier entity WHERE entity.nom_filiere_id = :id")
    Flux<Atelier> findByNomFiliere(Long id);

    @Query("SELECT * FROM atelier entity WHERE entity.nom_filiere_id IS NULL")
    Flux<Atelier> findAllWhereNomFiliereIsNull();

    @Query("SELECT * FROM atelier entity WHERE entity.nom_batiment_id = :id")
    Flux<Atelier> findByNomBatiment(Long id);

    @Query("SELECT * FROM atelier entity WHERE entity.nom_batiment_id IS NULL")
    Flux<Atelier> findAllWhereNomBatimentIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Atelier> findAll();

    @Override
    Mono<Atelier> findById(Long id);

    @Override
    <S extends Atelier> Mono<S> save(S entity);
}

interface AtelierRepositoryInternal {
    <S extends Atelier> Mono<S> insert(S entity);
    <S extends Atelier> Mono<S> save(S entity);
    Mono<Integer> update(Atelier entity);

    Flux<Atelier> findAll();
    Mono<Atelier> findById(Long id);
    Flux<Atelier> findAllBy(Pageable pageable);
    Flux<Atelier> findAllBy(Pageable pageable, Criteria criteria);
}
