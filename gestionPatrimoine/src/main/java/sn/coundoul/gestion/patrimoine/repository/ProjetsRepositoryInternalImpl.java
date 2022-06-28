package sn.coundoul.gestion.patrimoine.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.Projets;
import sn.coundoul.gestion.patrimoine.domain.enumeration.TypeProjet;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ContratProjetRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.EtablissementRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ProjetsRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Projets entity.
 */
@SuppressWarnings("unused")
class ProjetsRepositoryInternalImpl implements ProjetsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ContratProjetRowMapper contratprojetMapper;
    private final EtablissementRowMapper etablissementMapper;
    private final ProjetsRowMapper projetsMapper;

    private static final Table entityTable = Table.aliased("projets", EntityManager.ENTITY_ALIAS);
    private static final Table nomTable = Table.aliased("contrat_projet", "nom");
    private static final Table nomEtablissementTable = Table.aliased("etablissement", "nomEtablissement");
    private static final Table nomBatimentTable = Table.aliased("etablissement", "nomBatiment");

    public ProjetsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ContratProjetRowMapper contratprojetMapper,
        EtablissementRowMapper etablissementMapper,
        ProjetsRowMapper projetsMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.contratprojetMapper = contratprojetMapper;
        this.etablissementMapper = etablissementMapper;
        this.projetsMapper = projetsMapper;
    }

    @Override
    public Flux<Projets> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Projets> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Projets> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = ProjetsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ContratProjetSqlHelper.getColumns(nomTable, "nom"));
        columns.addAll(EtablissementSqlHelper.getColumns(nomEtablissementTable, "nomEtablissement"));
        columns.addAll(EtablissementSqlHelper.getColumns(nomBatimentTable, "nomBatiment"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomTable)
            .on(Column.create("nom_id", entityTable))
            .equals(Column.create("id", nomTable))
            .leftOuterJoin(nomEtablissementTable)
            .on(Column.create("nom_etablissement_id", entityTable))
            .equals(Column.create("id", nomEtablissementTable))
            .leftOuterJoin(nomBatimentTable)
            .on(Column.create("nom_batiment_id", entityTable))
            .equals(Column.create("id", nomBatimentTable));

        String select = entityManager.createSelect(selectFrom, Projets.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(
                crit ->
                    new StringBuilder(select)
                        .append(" ")
                        .append("WHERE")
                        .append(" ")
                        .append(alias)
                        .append(".")
                        .append(crit.toString())
                        .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<Projets> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Projets> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Projets process(Row row, RowMetadata metadata) {
        Projets entity = projetsMapper.apply(row, "e");
        entity.setNom(contratprojetMapper.apply(row, "nom"));
        entity.setNomEtablissement(etablissementMapper.apply(row, "nomEtablissement"));
        entity.setNomBatiment(etablissementMapper.apply(row, "nomBatiment"));
        return entity;
    }

    @Override
    public <S extends Projets> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Projets> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Projets with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Projets entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class ProjetsSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("type_projet", table, columnPrefix + "_type_projet"));
        columns.add(Column.aliased("nom_projet", table, columnPrefix + "_nom_projet"));
        columns.add(Column.aliased("date_debut", table, columnPrefix + "_date_debut"));
        columns.add(Column.aliased("date_fin", table, columnPrefix + "_date_fin"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("extension", table, columnPrefix + "_extension"));

        columns.add(Column.aliased("nom_id", table, columnPrefix + "_nom_id"));
        columns.add(Column.aliased("nom_etablissement_id", table, columnPrefix + "_nom_etablissement_id"));
        columns.add(Column.aliased("nom_batiment_id", table, columnPrefix + "_nom_batiment_id"));
        return columns;
    }
}
