package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.DetailSortie;

/**
 * Spring Data SQL reactive repository for the DetailSortie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailSortieRepository extends R2dbcRepository<DetailSortie, Long>, DetailSortieRepositoryInternal {
    Flux<DetailSortie> findAllBy(Pageable pageable);

    @Query("SELECT * FROM detail_sortie entity WHERE entity.type_bon_id = :id")
    Flux<DetailSortie> findByTypeBon(Long id);

    @Query("SELECT * FROM detail_sortie entity WHERE entity.type_bon_id IS NULL")
    Flux<DetailSortie> findAllWhereTypeBonIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<DetailSortie> findAll();

    @Override
    Mono<DetailSortie> findById(Long id);

    @Override
    <S extends DetailSortie> Mono<S> save(S entity);
}

interface DetailSortieRepositoryInternal {
    <S extends DetailSortie> Mono<S> insert(S entity);
    <S extends DetailSortie> Mono<S> save(S entity);
    Mono<Integer> update(DetailSortie entity);

    Flux<DetailSortie> findAll();
    Mono<DetailSortie> findById(Long id);
    Flux<DetailSortie> findAllBy(Pageable pageable);
    Flux<DetailSortie> findAllBy(Pageable pageable, Criteria criteria);
}
