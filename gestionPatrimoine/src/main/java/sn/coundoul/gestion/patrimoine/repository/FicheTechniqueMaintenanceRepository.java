package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.FicheTechniqueMaintenance;

/**
 * Spring Data SQL reactive repository for the FicheTechniqueMaintenance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FicheTechniqueMaintenanceRepository
    extends R2dbcRepository<FicheTechniqueMaintenance, Long>, FicheTechniqueMaintenanceRepositoryInternal {
    Flux<FicheTechniqueMaintenance> findAllBy(Pageable pageable);

    @Query("SELECT * FROM fiche_technique_maintenance entity WHERE entity.type_id = :id")
    Flux<FicheTechniqueMaintenance> findByType(Long id);

    @Query("SELECT * FROM fiche_technique_maintenance entity WHERE entity.type_id IS NULL")
    Flux<FicheTechniqueMaintenance> findAllWhereTypeIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<FicheTechniqueMaintenance> findAll();

    @Override
    Mono<FicheTechniqueMaintenance> findById(Long id);

    @Override
    <S extends FicheTechniqueMaintenance> Mono<S> save(S entity);
}

interface FicheTechniqueMaintenanceRepositoryInternal {
    <S extends FicheTechniqueMaintenance> Mono<S> insert(S entity);
    <S extends FicheTechniqueMaintenance> Mono<S> save(S entity);
    Mono<Integer> update(FicheTechniqueMaintenance entity);

    Flux<FicheTechniqueMaintenance> findAll();
    Mono<FicheTechniqueMaintenance> findById(Long id);
    Flux<FicheTechniqueMaintenance> findAllBy(Pageable pageable);
    Flux<FicheTechniqueMaintenance> findAllBy(Pageable pageable, Criteria criteria);
}
