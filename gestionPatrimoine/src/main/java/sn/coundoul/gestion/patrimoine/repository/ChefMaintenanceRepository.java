package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.ChefMaintenance;

/**
 * Spring Data SQL reactive repository for the ChefMaintenance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChefMaintenanceRepository extends R2dbcRepository<ChefMaintenance, Long>, ChefMaintenanceRepositoryInternal {
    Flux<ChefMaintenance> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<ChefMaintenance> findAll();

    @Override
    Mono<ChefMaintenance> findById(Long id);

    @Override
    <S extends ChefMaintenance> Mono<S> save(S entity);
}

interface ChefMaintenanceRepositoryInternal {
    <S extends ChefMaintenance> Mono<S> insert(S entity);
    <S extends ChefMaintenance> Mono<S> save(S entity);
    Mono<Integer> update(ChefMaintenance entity);

    Flux<ChefMaintenance> findAll();
    Mono<ChefMaintenance> findById(Long id);
    Flux<ChefMaintenance> findAllBy(Pageable pageable);
    Flux<ChefMaintenance> findAllBy(Pageable pageable, Criteria criteria);
}
