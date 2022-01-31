package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Intervenant;

/**
 * Spring Data SQL reactive repository for the Intervenant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntervenantRepository extends R2dbcRepository<Intervenant, Long>, IntervenantRepositoryInternal {
    Flux<Intervenant> findAllBy(Pageable pageable);

    @Query("SELECT * FROM intervenant entity WHERE entity.nom_projet_id = :id")
    Flux<Intervenant> findByNomProjet(Long id);

    @Query("SELECT * FROM intervenant entity WHERE entity.nom_projet_id IS NULL")
    Flux<Intervenant> findAllWhereNomProjetIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Intervenant> findAll();

    @Override
    Mono<Intervenant> findById(Long id);

    @Override
    <S extends Intervenant> Mono<S> save(S entity);
}

interface IntervenantRepositoryInternal {
    <S extends Intervenant> Mono<S> insert(S entity);
    <S extends Intervenant> Mono<S> save(S entity);
    Mono<Integer> update(Intervenant entity);

    Flux<Intervenant> findAll();
    Mono<Intervenant> findById(Long id);
    Flux<Intervenant> findAllBy(Pageable pageable);
    Flux<Intervenant> findAllBy(Pageable pageable, Criteria criteria);
}
