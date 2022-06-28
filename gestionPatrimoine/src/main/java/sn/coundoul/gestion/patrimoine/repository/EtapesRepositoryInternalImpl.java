package sn.coundoul.gestion.patrimoine.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Duration;
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
import sn.coundoul.gestion.patrimoine.domain.Etapes;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.EtapesRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ProjetsRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Etapes entity.
 */
@SuppressWarnings("unused")
class EtapesRepositoryInternalImpl implements EtapesRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProjetsRowMapper projetsMapper;
    private final EtapesRowMapper etapesMapper;

    private static final Table entityTable = Table.aliased("etapes", EntityManager.ENTITY_ALIAS);
    private static final Table nomProjetTable = Table.aliased("projets", "nomProjet");

    public EtapesRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProjetsRowMapper projetsMapper,
        EtapesRowMapper etapesMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.projetsMapper = projetsMapper;
        this.etapesMapper = etapesMapper;
    }

    @Override
    public Flux<Etapes> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Etapes> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Etapes> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = EtapesSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProjetsSqlHelper.getColumns(nomProjetTable, "nomProjet"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomProjetTable)
            .on(Column.create("nom_projet_id", entityTable))
            .equals(Column.create("id", nomProjetTable));

        String select = entityManager.createSelect(selectFrom, Etapes.class, pageable, criteria);
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
    public Flux<Etapes> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Etapes> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Etapes process(Row row, RowMetadata metadata) {
        Etapes entity = etapesMapper.apply(row, "e");
        entity.setNomProjet(projetsMapper.apply(row, "nomProjet"));
        return entity;
    }

    @Override
    public <S extends Etapes> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Etapes> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Etapes with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Etapes entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class EtapesSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("date_debut", table, columnPrefix + "_date_debut"));
        columns.add(Column.aliased("date_fin", table, columnPrefix + "_date_fin"));
        columns.add(Column.aliased("nom_tache", table, columnPrefix + "_nom_tache"));
        columns.add(Column.aliased("duration", table, columnPrefix + "_duration"));

        columns.add(Column.aliased("nom_projet_id", table, columnPrefix + "_nom_projet_id"));
        return columns;
    }
}
