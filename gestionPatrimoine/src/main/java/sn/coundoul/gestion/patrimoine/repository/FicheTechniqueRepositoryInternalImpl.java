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
import sn.coundoul.gestion.patrimoine.domain.FicheTechnique;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.FicheTechniqueRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ResponsableRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the FicheTechnique entity.
 */
@SuppressWarnings("unused")
class FicheTechniqueRepositoryInternalImpl implements FicheTechniqueRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ResponsableRowMapper responsableMapper;
    private final FicheTechniqueRowMapper fichetechniqueMapper;

    private static final Table entityTable = Table.aliased("fiche_technique", EntityManager.ENTITY_ALIAS);
    private static final Table nomResponsableTable = Table.aliased("responsable", "nomResponsable");

    public FicheTechniqueRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ResponsableRowMapper responsableMapper,
        FicheTechniqueRowMapper fichetechniqueMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.responsableMapper = responsableMapper;
        this.fichetechniqueMapper = fichetechniqueMapper;
    }

    @Override
    public Flux<FicheTechnique> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<FicheTechnique> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<FicheTechnique> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = FicheTechniqueSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ResponsableSqlHelper.getColumns(nomResponsableTable, "nomResponsable"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomResponsableTable)
            .on(Column.create("nom_responsable_id", entityTable))
            .equals(Column.create("id", nomResponsableTable));

        String select = entityManager.createSelect(selectFrom, FicheTechnique.class, pageable, criteria);
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
    public Flux<FicheTechnique> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<FicheTechnique> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private FicheTechnique process(Row row, RowMetadata metadata) {
        FicheTechnique entity = fichetechniqueMapper.apply(row, "e");
        entity.setNomResponsable(responsableMapper.apply(row, "nomResponsable"));
        return entity;
    }

    @Override
    public <S extends FicheTechnique> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends FicheTechnique> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update FicheTechnique with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(FicheTechnique entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class FicheTechniqueSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("piece_jointe", table, columnPrefix + "_piece_jointe"));
        columns.add(Column.aliased("date_depot", table, columnPrefix + "_date_depot"));

        columns.add(Column.aliased("nom_responsable_id", table, columnPrefix + "_nom_responsable_id"));
        return columns;
    }
}
