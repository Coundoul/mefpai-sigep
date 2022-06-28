package sn.coundoul.gestion.patrimoine.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
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
import sn.coundoul.gestion.patrimoine.domain.AttributionInfrastructure;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.AttributionInfrastructureRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.EtablissementRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the AttributionInfrastructure entity.
 */
@SuppressWarnings("unused")
class AttributionInfrastructureRepositoryInternalImpl implements AttributionInfrastructureRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EtablissementRowMapper etablissementMapper;
    private final AttributionInfrastructureRowMapper attributioninfrastructureMapper;

    private static final Table entityTable = Table.aliased("attribution_infrastructure", EntityManager.ENTITY_ALIAS);
    private static final Table nomEtablissementTable = Table.aliased("etablissement", "nomEtablissement");

    public AttributionInfrastructureRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EtablissementRowMapper etablissementMapper,
        AttributionInfrastructureRowMapper attributioninfrastructureMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.etablissementMapper = etablissementMapper;
        this.attributioninfrastructureMapper = attributioninfrastructureMapper;
    }

    @Override
    public Flux<AttributionInfrastructure> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<AttributionInfrastructure> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<AttributionInfrastructure> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = AttributionInfrastructureSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EtablissementSqlHelper.getColumns(nomEtablissementTable, "nomEtablissement"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomEtablissementTable)
            .on(Column.create("nom_etablissement_id", entityTable))
            .equals(Column.create("id", nomEtablissementTable));

        String select = entityManager.createSelect(selectFrom, AttributionInfrastructure.class, pageable, criteria);
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
    public Flux<AttributionInfrastructure> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<AttributionInfrastructure> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private AttributionInfrastructure process(Row row, RowMetadata metadata) {
        AttributionInfrastructure entity = attributioninfrastructureMapper.apply(row, "e");
        entity.setNomEtablissement(etablissementMapper.apply(row, "nomEtablissement"));
        return entity;
    }

    @Override
    public <S extends AttributionInfrastructure> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends AttributionInfrastructure> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update AttributionInfrastructure with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(AttributionInfrastructure entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class AttributionInfrastructureSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("date_attribution", table, columnPrefix + "_date_attribution"));
        columns.add(Column.aliased("quantite", table, columnPrefix + "_quantite"));
        columns.add(Column.aliased("id_equipement", table, columnPrefix + "_id_equipement"));
        columns.add(Column.aliased("id_pers", table, columnPrefix + "_id_pers"));

        columns.add(Column.aliased("nom_etablissement_id", table, columnPrefix + "_nom_etablissement_id"));
        return columns;
    }
}
