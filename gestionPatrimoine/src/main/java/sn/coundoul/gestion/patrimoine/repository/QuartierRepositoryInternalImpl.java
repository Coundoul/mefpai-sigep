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
import sn.coundoul.gestion.patrimoine.domain.Quartier;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.CommuneRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.QuartierRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Quartier entity.
 */
@SuppressWarnings("unused")
class QuartierRepositoryInternalImpl implements QuartierRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CommuneRowMapper communeMapper;
    private final QuartierRowMapper quartierMapper;

    private static final Table entityTable = Table.aliased("quartier", EntityManager.ENTITY_ALIAS);
    private static final Table nomCommuneTable = Table.aliased("commune", "nomCommune");

    public QuartierRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CommuneRowMapper communeMapper,
        QuartierRowMapper quartierMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.communeMapper = communeMapper;
        this.quartierMapper = quartierMapper;
    }

    @Override
    public Flux<Quartier> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Quartier> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Quartier> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = QuartierSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CommuneSqlHelper.getColumns(nomCommuneTable, "nomCommune"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomCommuneTable)
            .on(Column.create("nom_commune_id", entityTable))
            .equals(Column.create("id", nomCommuneTable));

        String select = entityManager.createSelect(selectFrom, Quartier.class, pageable, criteria);
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
    public Flux<Quartier> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Quartier> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Quartier process(Row row, RowMetadata metadata) {
        Quartier entity = quartierMapper.apply(row, "e");
        entity.setNomCommune(communeMapper.apply(row, "nomCommune"));
        return entity;
    }

    @Override
    public <S extends Quartier> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Quartier> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Quartier with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Quartier entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class QuartierSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_quartier", table, columnPrefix + "_nom_quartier"));

        columns.add(Column.aliased("nom_commune_id", table, columnPrefix + "_nom_commune_id"));
        return columns;
    }
}
