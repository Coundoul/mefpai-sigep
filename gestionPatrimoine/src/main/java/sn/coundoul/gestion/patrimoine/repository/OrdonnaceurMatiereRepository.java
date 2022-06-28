package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.OrdonnaceurMatiere;

/**
 * Spring Data SQL reactive repository for the OrdonnaceurMatiere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdonnaceurMatiereRepository extends R2dbcRepository<OrdonnaceurMatiere, Long>, OrdonnaceurMatiereRepositoryInternal {
    Flux<OrdonnaceurMatiere> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<OrdonnaceurMatiere> findAll();

    @Override
    Mono<OrdonnaceurMatiere> findById(Long id);

    @Override
    <S extends OrdonnaceurMatiere> Mono<S> save(S entity);
}

interface OrdonnaceurMatiereRepositoryInternal {
    <S extends OrdonnaceurMatiere> Mono<S> insert(S entity);
    <S extends OrdonnaceurMatiere> Mono<S> save(S entity);
    Mono<Integer> update(OrdonnaceurMatiere entity);

    Flux<OrdonnaceurMatiere> findAll();
    Mono<OrdonnaceurMatiere> findById(Long id);
    Flux<OrdonnaceurMatiere> findAllBy(Pageable pageable);
    Flux<OrdonnaceurMatiere> findAllBy(Pageable pageable, Criteria criteria);
}
