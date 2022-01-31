package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.ContratProjet;

/**
 * Spring Data SQL reactive repository for the ContratProjet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContratProjetRepository extends R2dbcRepository<ContratProjet, Long>, ContratProjetRepositoryInternal {
    Flux<ContratProjet> findAllBy(Pageable pageable);

    @Query("SELECT * FROM contrat_projet entity WHERE entity.id not in (select nom_id from projets)")
    Flux<ContratProjet> findAllWhereNomProjetIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<ContratProjet> findAll();

    @Override
    Mono<ContratProjet> findById(Long id);

    @Override
    <S extends ContratProjet> Mono<S> save(S entity);
}

interface ContratProjetRepositoryInternal {
    <S extends ContratProjet> Mono<S> insert(S entity);
    <S extends ContratProjet> Mono<S> save(S entity);
    Mono<Integer> update(ContratProjet entity);

    Flux<ContratProjet> findAll();
    Mono<ContratProjet> findById(Long id);
    Flux<ContratProjet> findAllBy(Pageable pageable);
    Flux<ContratProjet> findAllBy(Pageable pageable, Criteria criteria);
}
