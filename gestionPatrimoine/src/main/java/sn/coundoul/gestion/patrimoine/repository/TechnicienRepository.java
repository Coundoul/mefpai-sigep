package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Technicien;

/**
 * Spring Data SQL reactive repository for the Technicien entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechnicienRepository extends R2dbcRepository<Technicien, Long>, TechnicienRepositoryInternal {
    Flux<Technicien> findAllBy(Pageable pageable);

    @Query("SELECT * FROM technicien entity WHERE entity.chef_maintenance_id = :id")
    Flux<Technicien> findByChefMaintenance(Long id);

    @Query("SELECT * FROM technicien entity WHERE entity.chef_maintenance_id IS NULL")
    Flux<Technicien> findAllWhereChefMaintenanceIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Technicien> findAll();

    @Override
    Mono<Technicien> findById(Long id);

    @Override
    <S extends Technicien> Mono<S> save(S entity);
}

interface TechnicienRepositoryInternal {
    <S extends Technicien> Mono<S> insert(S entity);
    <S extends Technicien> Mono<S> save(S entity);
    Mono<Integer> update(Technicien entity);

    Flux<Technicien> findAll();
    Mono<Technicien> findById(Long id);
    Flux<Technicien> findAllBy(Pageable pageable);
    Flux<Technicien> findAllBy(Pageable pageable, Criteria criteria);
}
