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
import sn.coundoul.gestion.patrimoine.domain.Requete;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.BureauRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.RequeteRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Requete entity.
 */
@SuppressWarnings("unused")
class RequeteRepositoryInternalImpl implements RequeteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final BureauRowMapper bureauMapper;
    private final RequeteRowMapper requeteMapper;

    private static final Table entityTable = Table.aliased("requete", EntityManager.ENTITY_ALIAS);
    private static final Table nomStructureTable = Table.aliased("bureau", "nomStructure");

    public RequeteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        BureauRowMapper bureauMapper,
        RequeteRowMapper requeteMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.bureauMapper = bureauMapper;
        this.requeteMapper = requeteMapper;
    }

    @Override
    public Flux<Requete> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Requete> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Requete> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = RequeteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(BureauSqlHelper.getColumns(nomStructureTable, "nomStructure"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomStructureTable)
            .on(Column.create("nom_structure_id", entityTable))
            .equals(Column.create("id", nomStructureTable));

        String select = entityManager.createSelect(selectFrom, Requete.class, pageable, criteria);
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
    public Flux<Requete> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Requete> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Requete process(Row row, RowMetadata metadata) {
        Requete entity = requeteMapper.apply(row, "e");
        entity.setNomStructure(bureauMapper.apply(row, "nomStructure"));
        return entity;
    }

    @Override
    public <S extends Requete> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Requete> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Requete with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Requete entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class RequeteSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("type_panne", table, columnPrefix + "_type_panne"));
        columns.add(Column.aliased("date_post", table, columnPrefix + "_date_post"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("etat_traite", table, columnPrefix + "_etat_traite"));
        columns.add(Column.aliased("date_lancement", table, columnPrefix + "_date_lancement"));
        columns.add(Column.aliased("id_pers", table, columnPrefix + "_id_pers"));

        columns.add(Column.aliased("nom_structure_id", table, columnPrefix + "_nom_structure_id"));
        return columns;
    }
}
