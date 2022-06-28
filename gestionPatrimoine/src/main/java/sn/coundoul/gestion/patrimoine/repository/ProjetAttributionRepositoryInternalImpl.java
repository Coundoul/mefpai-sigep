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
import sn.coundoul.gestion.patrimoine.domain.ProjetAttribution;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ProjetAttributionRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ProjetsRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the ProjetAttribution entity.
 */
@SuppressWarnings("unused")
class ProjetAttributionRepositoryInternalImpl implements ProjetAttributionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProjetsRowMapper projetsMapper;
    private final ProjetAttributionRowMapper projetattributionMapper;

    private static final Table entityTable = Table.aliased("projet_attribution", EntityManager.ENTITY_ALIAS);
    private static final Table nomProjetTable = Table.aliased("projets", "nomProjet");

    public ProjetAttributionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProjetsRowMapper projetsMapper,
        ProjetAttributionRowMapper projetattributionMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.projetsMapper = projetsMapper;
        this.projetattributionMapper = projetattributionMapper;
    }

    @Override
    public Flux<ProjetAttribution> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<ProjetAttribution> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<ProjetAttribution> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = ProjetAttributionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProjetsSqlHelper.getColumns(nomProjetTable, "nomProjet"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomProjetTable)
            .on(Column.create("nom_projet_id", entityTable))
            .equals(Column.create("id", nomProjetTable));

        String select = entityManager.createSelect(selectFrom, ProjetAttribution.class, pageable, criteria);
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
    public Flux<ProjetAttribution> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<ProjetAttribution> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private ProjetAttribution process(Row row, RowMetadata metadata) {
        ProjetAttribution entity = projetattributionMapper.apply(row, "e");
        entity.setNomProjet(projetsMapper.apply(row, "nomProjet"));
        return entity;
    }

    @Override
    public <S extends ProjetAttribution> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends ProjetAttribution> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update ProjetAttribution with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(ProjetAttribution entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class ProjetAttributionSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("date_attribution", table, columnPrefix + "_date_attribution"));
        columns.add(Column.aliased("quantite", table, columnPrefix + "_quantite"));
        columns.add(Column.aliased("id_equipement", table, columnPrefix + "_id_equipement"));
        columns.add(Column.aliased("id_pers", table, columnPrefix + "_id_pers"));

        columns.add(Column.aliased("nom_projet_id", table, columnPrefix + "_nom_projet_id"));
        return columns;
    }
}
