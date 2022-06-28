package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.ChefEtablissement;

/**
 * Spring Data SQL reactive repository for the ChefEtablissement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChefEtablissementRepository extends R2dbcRepository<ChefEtablissement, Long>, ChefEtablissementRepositoryInternal {
    Flux<ChefEtablissement> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<ChefEtablissement> findAll();

    @Override
    Mono<ChefEtablissement> findById(Long id);

    @Override
    <S extends ChefEtablissement> Mono<S> save(S entity);
}

interface ChefEtablissementRepositoryInternal {
    <S extends ChefEtablissement> Mono<S> insert(S entity);
    <S extends ChefEtablissement> Mono<S> save(S entity);
    Mono<Integer> update(ChefEtablissement entity);

    Flux<ChefEtablissement> findAll();
    Mono<ChefEtablissement> findById(Long id);
    Flux<ChefEtablissement> findAllBy(Pageable pageable);
    Flux<ChefEtablissement> findAllBy(Pageable pageable, Criteria criteria);
}
