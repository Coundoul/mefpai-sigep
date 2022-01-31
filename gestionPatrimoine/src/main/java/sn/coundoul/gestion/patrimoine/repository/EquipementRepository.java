package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Equipement;

/**
 * Spring Data SQL reactive repository for the Equipement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipementRepository extends R2dbcRepository<Equipement, Long>, EquipementRepositoryInternal {
    Flux<Equipement> findAllBy(Pageable pageable);

    @Query("SELECT * FROM equipement entity WHERE entity.nom_magazin_id = :id")
    Flux<Equipement> findByNomMagazin(Long id);

    @Query("SELECT * FROM equipement entity WHERE entity.nom_magazin_id IS NULL")
    Flux<Equipement> findAllWhereNomMagazinIsNull();

    @Query("SELECT * FROM equipement entity WHERE entity.nom_fournisseur_id = :id")
    Flux<Equipement> findByNomFournisseur(Long id);

    @Query("SELECT * FROM equipement entity WHERE entity.nom_fournisseur_id IS NULL")
    Flux<Equipement> findAllWhereNomFournisseurIsNull();

    @Query("SELECT * FROM equipement entity WHERE entity.bon_id = :id")
    Flux<Equipement> findByBon(Long id);

    @Query("SELECT * FROM equipement entity WHERE entity.bon_id IS NULL")
    Flux<Equipement> findAllWhereBonIsNull();

    @Query("SELECT * FROM equipement entity WHERE entity.categorie_id = :id")
    Flux<Equipement> findByCategorie(Long id);

    @Query("SELECT * FROM equipement entity WHERE entity.categorie_id IS NULL")
    Flux<Equipement> findAllWhereCategorieIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Equipement> findAll();

    @Override
    Mono<Equipement> findById(Long id);

    @Override
    <S extends Equipement> Mono<S> save(S entity);
}

interface EquipementRepositoryInternal {
    <S extends Equipement> Mono<S> insert(S entity);
    <S extends Equipement> Mono<S> save(S entity);
    Mono<Integer> update(Equipement entity);

    Flux<Equipement> findAll();
    Mono<Equipement> findById(Long id);
    Flux<Equipement> findAllBy(Pageable pageable);
    Flux<Equipement> findAllBy(Pageable pageable, Criteria criteria);
}
