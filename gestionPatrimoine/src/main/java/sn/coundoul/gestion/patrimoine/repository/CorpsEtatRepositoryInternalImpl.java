package sn.coundoul.gestion.patrimoine.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
import sn.coundoul.gestion.patrimoine.domain.CorpsEtat;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.CorpsEtatRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ResponsableRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the CorpsEtat entity.
 */
@SuppressWarnings("unused")
class CorpsEtatRepositoryInternalImpl implements CorpsEtatRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ResponsableRowMapper responsableMapper;
    private final CorpsEtatRowMapper corpsetatMapper;

    private static final Table entityTable = Table.aliased("corps_etat", EntityManager.ENTITY_ALIAS);
    private static final Table nomResponsableTable = Table.aliased("responsable", "nomResponsable");

    public CorpsEtatRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ResponsableRowMapper responsableMapper,
        CorpsEtatRowMapper corpsetatMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.responsableMapper = responsableMapper;
        this.corpsetatMapper = corpsetatMapper;
    }

    @Override
    public Flux<CorpsEtat> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<CorpsEtat> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<CorpsEtat> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = CorpsEtatSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ResponsableSqlHelper.getColumns(nomResponsableTable, "nomResponsable"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomResponsableTable)
            .on(Column.create("nom_responsable_id", entityTable))
            .equals(Column.create("id", nomResponsableTable));

        String select = entityManager.createSelect(selectFrom, CorpsEtat.class, pageable, criteria);
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
    public Flux<CorpsEtat> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<CorpsEtat> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private CorpsEtat process(Row row, RowMetadata metadata) {
        CorpsEtat entity = corpsetatMapper.apply(row, "e");
        entity.setNomResponsable(responsableMapper.apply(row, "nomResponsable"));
        return entity;
    }

    @Override
    public <S extends CorpsEtat> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends CorpsEtat> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update CorpsEtat with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(CorpsEtat entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class CorpsEtatSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_corps", table, columnPrefix + "_nom_corps"));
        columns.add(Column.aliased("gros_oeuvre", table, columnPrefix + "_gros_oeuvre"));
        columns.add(Column.aliased("description_gros_oeuvre", table, columnPrefix + "_description_gros_oeuvre"));
        columns.add(Column.aliased("second_oeuvre", table, columnPrefix + "_second_oeuvre"));
        columns.add(Column.aliased("description_second_oeuvre", table, columnPrefix + "_description_second_oeuvre"));
        columns.add(Column.aliased("oservation", table, columnPrefix + "_oservation"));
        columns.add(Column.aliased("etat_corps", table, columnPrefix + "_etat_corps"));

        columns.add(Column.aliased("nom_responsable_id", table, columnPrefix + "_nom_responsable_id"));
        return columns;
    }
}
