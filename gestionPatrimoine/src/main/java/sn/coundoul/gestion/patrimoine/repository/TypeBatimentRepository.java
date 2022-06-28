package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.TypeBatiment;

/**
 * Spring Data SQL reactive repository for the TypeBatiment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeBatimentRepository extends R2dbcRepository<TypeBatiment, Long>, TypeBatimentRepositoryInternal {
    Flux<TypeBatiment> findAllBy(Pageable pageable);

    @Query("SELECT * FROM type_batiment entity WHERE entity.nom_batiment_id = :id")
    Flux<TypeBatiment> findByNomBatiment(Long id);

    @Query("SELECT * FROM type_batiment entity WHERE entity.nom_batiment_id IS NULL")
    Flux<TypeBatiment> findAllWhereNomBatimentIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<TypeBatiment> findAll();

    @Override
    Mono<TypeBatiment> findById(Long id);

    @Override
    <S extends TypeBatiment> Mono<S> save(S entity);
}

interface TypeBatimentRepositoryInternal {
    <S extends TypeBatiment> Mono<S> insert(S entity);
    <S extends TypeBatiment> Mono<S> save(S entity);
    Mono<Integer> update(TypeBatiment entity);

    Flux<TypeBatiment> findAll();
    Mono<TypeBatiment> findById(Long id);
    Flux<TypeBatiment> findAllBy(Pageable pageable);
    Flux<TypeBatiment> findAllBy(Pageable pageable, Criteria criteria);
}
