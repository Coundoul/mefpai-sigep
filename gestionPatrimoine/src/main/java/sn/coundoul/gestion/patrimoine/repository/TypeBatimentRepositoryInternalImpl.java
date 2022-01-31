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
import sn.coundoul.gestion.patrimoine.domain.TypeBatiment;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.BatimentRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.TypeBatimentRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the TypeBatiment entity.
 */
@SuppressWarnings("unused")
class TypeBatimentRepositoryInternalImpl implements TypeBatimentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final BatimentRowMapper batimentMapper;
    private final TypeBatimentRowMapper typebatimentMapper;

    private static final Table entityTable = Table.aliased("type_batiment", EntityManager.ENTITY_ALIAS);
    private static final Table nomBatimentTable = Table.aliased("batiment", "nomBatiment");

    public TypeBatimentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        BatimentRowMapper batimentMapper,
        TypeBatimentRowMapper typebatimentMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.batimentMapper = batimentMapper;
        this.typebatimentMapper = typebatimentMapper;
    }

    @Override
    public Flux<TypeBatiment> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<TypeBatiment> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<TypeBatiment> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = TypeBatimentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(BatimentSqlHelper.getColumns(nomBatimentTable, "nomBatiment"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomBatimentTable)
            .on(Column.create("nom_batiment_id", entityTable))
            .equals(Column.create("id", nomBatimentTable));

        String select = entityManager.createSelect(selectFrom, TypeBatiment.class, pageable, criteria);
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
    public Flux<TypeBatiment> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<TypeBatiment> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private TypeBatiment process(Row row, RowMetadata metadata) {
        TypeBatiment entity = typebatimentMapper.apply(row, "e");
        entity.setNomBatiment(batimentMapper.apply(row, "nomBatiment"));
        return entity;
    }

    @Override
    public <S extends TypeBatiment> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends TypeBatiment> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update TypeBatiment with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(TypeBatiment entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class TypeBatimentSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("type_ba", table, columnPrefix + "_type_ba"));

        columns.add(Column.aliased("nom_batiment_id", table, columnPrefix + "_nom_batiment_id"));
        return columns;
    }
}
