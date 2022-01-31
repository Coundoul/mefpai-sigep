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
import sn.coundoul.gestion.patrimoine.domain.DetailSortie;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.BonRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.DetailSortieRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the DetailSortie entity.
 */
@SuppressWarnings("unused")
class DetailSortieRepositoryInternalImpl implements DetailSortieRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final BonRowMapper bonMapper;
    private final DetailSortieRowMapper detailsortieMapper;

    private static final Table entityTable = Table.aliased("detail_sortie", EntityManager.ENTITY_ALIAS);
    private static final Table typeBonTable = Table.aliased("bon", "typeBon");

    public DetailSortieRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        BonRowMapper bonMapper,
        DetailSortieRowMapper detailsortieMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.bonMapper = bonMapper;
        this.detailsortieMapper = detailsortieMapper;
    }

    @Override
    public Flux<DetailSortie> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<DetailSortie> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<DetailSortie> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = DetailSortieSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(BonSqlHelper.getColumns(typeBonTable, "typeBon"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(typeBonTable)
            .on(Column.create("type_bon_id", entityTable))
            .equals(Column.create("id", typeBonTable));

        String select = entityManager.createSelect(selectFrom, DetailSortie.class, pageable, criteria);
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
    public Flux<DetailSortie> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<DetailSortie> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private DetailSortie process(Row row, RowMetadata metadata) {
        DetailSortie entity = detailsortieMapper.apply(row, "e");
        entity.setTypeBon(bonMapper.apply(row, "typeBon"));
        return entity;
    }

    @Override
    public <S extends DetailSortie> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends DetailSortie> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update DetailSortie with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(DetailSortie entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class DetailSortieSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("piece_jointe", table, columnPrefix + "_piece_jointe"));
        columns.add(Column.aliased("id_pers", table, columnPrefix + "_id_pers"));

        columns.add(Column.aliased("type_bon_id", table, columnPrefix + "_type_bon_id"));
        return columns;
    }
}
