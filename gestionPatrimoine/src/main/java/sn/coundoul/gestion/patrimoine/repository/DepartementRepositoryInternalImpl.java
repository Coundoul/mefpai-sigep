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
import sn.coundoul.gestion.patrimoine.domain.Departement;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.DepartementRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.RegionRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Departement entity.
 */
@SuppressWarnings("unused")
class DepartementRepositoryInternalImpl implements DepartementRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RegionRowMapper regionMapper;
    private final DepartementRowMapper departementMapper;

    private static final Table entityTable = Table.aliased("departement", EntityManager.ENTITY_ALIAS);
    private static final Table nomRegionTable = Table.aliased("region", "nomRegion");

    public DepartementRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RegionRowMapper regionMapper,
        DepartementRowMapper departementMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.regionMapper = regionMapper;
        this.departementMapper = departementMapper;
    }

    @Override
    public Flux<Departement> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Departement> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Departement> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = DepartementSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(RegionSqlHelper.getColumns(nomRegionTable, "nomRegion"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomRegionTable)
            .on(Column.create("nom_region_id", entityTable))
            .equals(Column.create("id", nomRegionTable));

        String select = entityManager.createSelect(selectFrom, Departement.class, pageable, criteria);
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
    public Flux<Departement> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Departement> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Departement process(Row row, RowMetadata metadata) {
        Departement entity = departementMapper.apply(row, "e");
        entity.setNomRegion(regionMapper.apply(row, "nomRegion"));
        return entity;
    }

    @Override
    public <S extends Departement> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Departement> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Departement with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Departement entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class DepartementSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_departement", table, columnPrefix + "_nom_departement"));

        columns.add(Column.aliased("nom_region_id", table, columnPrefix + "_nom_region_id"));
        return columns;
    }
}
