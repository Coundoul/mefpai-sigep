package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.CategorieMatiere;

/**
 * Spring Data SQL reactive repository for the CategorieMatiere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieMatiereRepository extends R2dbcRepository<CategorieMatiere, Long>, CategorieMatiereRepositoryInternal {
    Flux<CategorieMatiere> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<CategorieMatiere> findAll();

    @Override
    Mono<CategorieMatiere> findById(Long id);

    @Override
    <S extends CategorieMatiere> Mono<S> save(S entity);
}

interface CategorieMatiereRepositoryInternal {
    <S extends CategorieMatiere> Mono<S> insert(S entity);
    <S extends CategorieMatiere> Mono<S> save(S entity);
    Mono<Integer> update(CategorieMatiere entity);

    Flux<CategorieMatiere> findAll();
    Mono<CategorieMatiere> findById(Long id);
    Flux<CategorieMatiere> findAllBy(Pageable pageable);
    Flux<CategorieMatiere> findAllBy(Pageable pageable, Criteria criteria);
}
