package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.ProjetAttribution;

/**
 * Spring Data SQL reactive repository for the ProjetAttribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjetAttributionRepository extends R2dbcRepository<ProjetAttribution, Long>, ProjetAttributionRepositoryInternal {
    Flux<ProjetAttribution> findAllBy(Pageable pageable);

    @Query("SELECT * FROM projet_attribution entity WHERE entity.nom_projet_id = :id")
    Flux<ProjetAttribution> findByNomProjet(Long id);

    @Query("SELECT * FROM projet_attribution entity WHERE entity.nom_projet_id IS NULL")
    Flux<ProjetAttribution> findAllWhereNomProjetIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<ProjetAttribution> findAll();

    @Override
    Mono<ProjetAttribution> findById(Long id);

    @Override
    <S extends ProjetAttribution> Mono<S> save(S entity);
}

interface ProjetAttributionRepositoryInternal {
    <S extends ProjetAttribution> Mono<S> insert(S entity);
    <S extends ProjetAttribution> Mono<S> save(S entity);
    Mono<Integer> update(ProjetAttribution entity);

    Flux<ProjetAttribution> findAll();
    Mono<ProjetAttribution> findById(Long id);
    Flux<ProjetAttribution> findAllBy(Pageable pageable);
    Flux<ProjetAttribution> findAllBy(Pageable pageable, Criteria criteria);
}
