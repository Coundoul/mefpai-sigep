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
import sn.coundoul.gestion.patrimoine.domain.Affectations;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Type;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.AffectationsRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.EquipementRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Affectations entity.
 */
@SuppressWarnings("unused")
class AffectationsRepositoryInternalImpl implements AffectationsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EquipementRowMapper equipementMapper;
    private final AffectationsRowMapper affectationsMapper;

    private static final Table entityTable = Table.aliased("affectations", EntityManager.ENTITY_ALIAS);
    private static final Table equipementTable = Table.aliased("equipement", "equipement");

    public AffectationsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EquipementRowMapper equipementMapper,
        AffectationsRowMapper affectationsMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.equipementMapper = equipementMapper;
        this.affectationsMapper = affectationsMapper;
    }

    @Override
    public Flux<Affectations> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Affectations> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Affectations> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = AffectationsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EquipementSqlHelper.getColumns(equipementTable, "equipement"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(equipementTable)
            .on(Column.create("equipement_id", entityTable))
            .equals(Column.create("id", equipementTable));

        String select = entityManager.createSelect(selectFrom, Affectations.class, pageable, criteria);
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
    public Flux<Affectations> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Affectations> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Affectations process(Row row, RowMetadata metadata) {
        Affectations entity = affectationsMapper.apply(row, "e");
        entity.setEquipement(equipementMapper.apply(row, "equipement"));
        return entity;
    }

    @Override
    public <S extends Affectations> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Affectations> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Affectations with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Affectations entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class AffectationsSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("quantite_affecter", table, columnPrefix + "_quantite_affecter"));
        columns.add(Column.aliased("type_attribution", table, columnPrefix + "_type_attribution"));
        columns.add(Column.aliased("id_pers", table, columnPrefix + "_id_pers"));
        columns.add(Column.aliased("date_attribution", table, columnPrefix + "_date_attribution"));

        columns.add(Column.aliased("equipement_id", table, columnPrefix + "_equipement_id"));
        return columns;
    }
}
