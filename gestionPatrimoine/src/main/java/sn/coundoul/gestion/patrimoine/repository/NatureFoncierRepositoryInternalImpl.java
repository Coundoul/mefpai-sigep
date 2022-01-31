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
import sn.coundoul.gestion.patrimoine.domain.NatureFoncier;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.CorpsEtatRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.NatureFoncierRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the NatureFoncier entity.
 */
@SuppressWarnings("unused")
class NatureFoncierRepositoryInternalImpl implements NatureFoncierRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CorpsEtatRowMapper corpsetatMapper;
    private final NatureFoncierRowMapper naturefoncierMapper;

    private static final Table entityTable = Table.aliased("nature_foncier", EntityManager.ENTITY_ALIAS);
    private static final Table nomCorpsTable = Table.aliased("corps_etat", "nomCorps");

    public NatureFoncierRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CorpsEtatRowMapper corpsetatMapper,
        NatureFoncierRowMapper naturefoncierMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.corpsetatMapper = corpsetatMapper;
        this.naturefoncierMapper = naturefoncierMapper;
    }

    @Override
    public Flux<NatureFoncier> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<NatureFoncier> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<NatureFoncier> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = NatureFoncierSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CorpsEtatSqlHelper.getColumns(nomCorpsTable, "nomCorps"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomCorpsTable)
            .on(Column.create("nom_corps_id", entityTable))
            .equals(Column.create("id", nomCorpsTable));

        String select = entityManager.createSelect(selectFrom, NatureFoncier.class, pageable, criteria);
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
    public Flux<NatureFoncier> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<NatureFoncier> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private NatureFoncier process(Row row, RowMetadata metadata) {
        NatureFoncier entity = naturefoncierMapper.apply(row, "e");
        entity.setNomCorps(corpsetatMapper.apply(row, "nomCorps"));
        return entity;
    }

    @Override
    public <S extends NatureFoncier> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends NatureFoncier> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update NatureFoncier with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(NatureFoncier entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class NatureFoncierSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("type_foncier", table, columnPrefix + "_type_foncier"));
        columns.add(Column.aliased("piece_jointe", table, columnPrefix + "_piece_jointe"));

        columns.add(Column.aliased("nom_corps_id", table, columnPrefix + "_nom_corps_id"));
        return columns;
    }
}
