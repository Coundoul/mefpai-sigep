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
import sn.coundoul.gestion.patrimoine.domain.Technicien;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ChefMaintenanceRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.TechnicienRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Technicien entity.
 */
@SuppressWarnings("unused")
class TechnicienRepositoryInternalImpl implements TechnicienRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ChefMaintenanceRowMapper chefmaintenanceMapper;
    private final TechnicienRowMapper technicienMapper;

    private static final Table entityTable = Table.aliased("technicien", EntityManager.ENTITY_ALIAS);
    private static final Table chefMaintenanceTable = Table.aliased("chef_maintenance", "chefMaintenance");

    public TechnicienRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ChefMaintenanceRowMapper chefmaintenanceMapper,
        TechnicienRowMapper technicienMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.chefmaintenanceMapper = chefmaintenanceMapper;
        this.technicienMapper = technicienMapper;
    }

    @Override
    public Flux<Technicien> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Technicien> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Technicien> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = TechnicienSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ChefMaintenanceSqlHelper.getColumns(chefMaintenanceTable, "chefMaintenance"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(chefMaintenanceTable)
            .on(Column.create("chef_maintenance_id", entityTable))
            .equals(Column.create("id", chefMaintenanceTable));

        String select = entityManager.createSelect(selectFrom, Technicien.class, pageable, criteria);
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
    public Flux<Technicien> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Technicien> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Technicien process(Row row, RowMetadata metadata) {
        Technicien entity = technicienMapper.apply(row, "e");
        entity.setChefMaintenance(chefmaintenanceMapper.apply(row, "chefMaintenance"));
        return entity;
    }

    @Override
    public <S extends Technicien> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Technicien> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Technicien with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Technicien entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class TechnicienSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_pers", table, columnPrefix + "_nom_pers"));
        columns.add(Column.aliased("prenom_pers", table, columnPrefix + "_prenom_pers"));
        columns.add(Column.aliased("sexe", table, columnPrefix + "_sexe"));
        columns.add(Column.aliased("mobile", table, columnPrefix + "_mobile"));
        columns.add(Column.aliased("adresse", table, columnPrefix + "_adresse"));
        columns.add(Column.aliased("direction", table, columnPrefix + "_direction"));

        columns.add(Column.aliased("chef_maintenance_id", table, columnPrefix + "_chef_maintenance_id"));
        return columns;
    }
}
