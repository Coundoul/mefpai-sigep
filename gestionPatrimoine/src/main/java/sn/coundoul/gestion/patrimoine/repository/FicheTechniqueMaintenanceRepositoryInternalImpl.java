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
import sn.coundoul.gestion.patrimoine.domain.FicheTechniqueMaintenance;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.FicheTechniqueMaintenanceRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.RequeteRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the FicheTechniqueMaintenance entity.
 */
@SuppressWarnings("unused")
class FicheTechniqueMaintenanceRepositoryInternalImpl implements FicheTechniqueMaintenanceRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RequeteRowMapper requeteMapper;
    private final FicheTechniqueMaintenanceRowMapper fichetechniquemaintenanceMapper;

    private static final Table entityTable = Table.aliased("fiche_technique_maintenance", EntityManager.ENTITY_ALIAS);
    private static final Table typeTable = Table.aliased("requete", "e_type");

    public FicheTechniqueMaintenanceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RequeteRowMapper requeteMapper,
        FicheTechniqueMaintenanceRowMapper fichetechniquemaintenanceMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.requeteMapper = requeteMapper;
        this.fichetechniquemaintenanceMapper = fichetechniquemaintenanceMapper;
    }

    @Override
    public Flux<FicheTechniqueMaintenance> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<FicheTechniqueMaintenance> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<FicheTechniqueMaintenance> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = FicheTechniqueMaintenanceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(RequeteSqlHelper.getColumns(typeTable, "type"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(typeTable)
            .on(Column.create("type_id", entityTable))
            .equals(Column.create("id", typeTable));

        String select = entityManager.createSelect(selectFrom, FicheTechniqueMaintenance.class, pageable, criteria);
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
    public Flux<FicheTechniqueMaintenance> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<FicheTechniqueMaintenance> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private FicheTechniqueMaintenance process(Row row, RowMetadata metadata) {
        FicheTechniqueMaintenance entity = fichetechniquemaintenanceMapper.apply(row, "e");
        entity.setType(requeteMapper.apply(row, "type"));
        return entity;
    }

    @Override
    public <S extends FicheTechniqueMaintenance> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends FicheTechniqueMaintenance> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update FicheTechniqueMaintenance with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(FicheTechniqueMaintenance entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class FicheTechniqueMaintenanceSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("piece_jointe", table, columnPrefix + "_piece_jointe"));
        columns.add(Column.aliased("id_pers", table, columnPrefix + "_id_pers"));
        columns.add(Column.aliased("date_depot", table, columnPrefix + "_date_depot"));

        columns.add(Column.aliased("type_id", table, columnPrefix + "_type_id"));
        return columns;
    }
}
