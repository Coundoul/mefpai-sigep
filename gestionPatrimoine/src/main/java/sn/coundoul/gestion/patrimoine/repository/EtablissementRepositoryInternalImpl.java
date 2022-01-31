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
import sn.coundoul.gestion.patrimoine.domain.Etablissement;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.EtablissementRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.QuartierRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Etablissement entity.
 */
@SuppressWarnings("unused")
class EtablissementRepositoryInternalImpl implements EtablissementRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final QuartierRowMapper quartierMapper;
    private final EtablissementRowMapper etablissementMapper;

    private static final Table entityTable = Table.aliased("etablissement", EntityManager.ENTITY_ALIAS);
    private static final Table nomQuartierTable = Table.aliased("quartier", "nomQuartier");

    public EtablissementRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        QuartierRowMapper quartierMapper,
        EtablissementRowMapper etablissementMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.quartierMapper = quartierMapper;
        this.etablissementMapper = etablissementMapper;
    }

    @Override
    public Flux<Etablissement> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Etablissement> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Etablissement> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = EtablissementSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(QuartierSqlHelper.getColumns(nomQuartierTable, "nomQuartier"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomQuartierTable)
            .on(Column.create("nom_quartier_id", entityTable))
            .equals(Column.create("id", nomQuartierTable));

        String select = entityManager.createSelect(selectFrom, Etablissement.class, pageable, criteria);
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
    public Flux<Etablissement> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Etablissement> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Etablissement process(Row row, RowMetadata metadata) {
        Etablissement entity = etablissementMapper.apply(row, "e");
        entity.setNomQuartier(quartierMapper.apply(row, "nomQuartier"));
        return entity;
    }

    @Override
    public <S extends Etablissement> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Etablissement> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Etablissement with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Etablissement entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class EtablissementSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_etablissement", table, columnPrefix + "_nom_etablissement"));
        columns.add(Column.aliased("surface_batie", table, columnPrefix + "_surface_batie"));
        columns.add(Column.aliased("superficie", table, columnPrefix + "_superficie"));
        columns.add(Column.aliased("id_pers", table, columnPrefix + "_id_pers"));

        columns.add(Column.aliased("nom_quartier_id", table, columnPrefix + "_nom_quartier_id"));
        return columns;
    }
}
