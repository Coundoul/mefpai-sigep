package sn.coundoul.gestion.patrimoine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.MangasinierFichiste;

/**
 * Spring Data SQL reactive repository for the MangasinierFichiste entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MangasinierFichisteRepository extends R2dbcRepository<MangasinierFichiste, Long>, MangasinierFichisteRepositoryInternal {
    Flux<MangasinierFichiste> findAllBy(Pageable pageable);

    @Query("SELECT * FROM mangasinier_fichiste entity WHERE entity.comptable_principale_id = :id")
    Flux<MangasinierFichiste> findByComptablePrincipale(Long id);

    @Query("SELECT * FROM mangasinier_fichiste entity WHERE entity.comptable_principale_id IS NULL")
    Flux<MangasinierFichiste> findAllWhereComptablePrincipaleIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<MangasinierFichiste> findAll();

    @Override
    Mono<MangasinierFichiste> findById(Long id);

    @Override
    <S extends MangasinierFichiste> Mono<S> save(S entity);
}

interface MangasinierFichisteRepositoryInternal {
    <S extends MangasinierFichiste> Mono<S> insert(S entity);
    <S extends MangasinierFichiste> Mono<S> save(S entity);
    Mono<Integer> update(MangasinierFichiste entity);

    Flux<MangasinierFichiste> findAll();
    Mono<MangasinierFichiste> findById(Long id);
    Flux<MangasinierFichiste> findAllBy(Pageable pageable);
    Flux<MangasinierFichiste> findAllBy(Pageable pageable, Criteria criteria);
}
