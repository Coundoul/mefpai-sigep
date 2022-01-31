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
import sn.coundoul.gestion.patrimoine.domain.Attribution;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.AffectationsRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.AttributionRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.UtilisateurFinalRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Attribution entity.
 */
@SuppressWarnings("unused")
class AttributionRepositoryInternalImpl implements AttributionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UtilisateurFinalRowMapper utilisateurfinalMapper;
    private final AffectationsRowMapper affectationsMapper;
    private final AttributionRowMapper attributionMapper;

    private static final Table entityTable = Table.aliased("attribution", EntityManager.ENTITY_ALIAS);
    private static final Table nomUtilisateurTable = Table.aliased("utilisateur_final", "nomUtilisateur");
    private static final Table affectationsTable = Table.aliased("affectations", "affectations");

    public AttributionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UtilisateurFinalRowMapper utilisateurfinalMapper,
        AffectationsRowMapper affectationsMapper,
        AttributionRowMapper attributionMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.utilisateurfinalMapper = utilisateurfinalMapper;
        this.affectationsMapper = affectationsMapper;
        this.attributionMapper = attributionMapper;
    }

    @Override
    public Flux<Attribution> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Attribution> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Attribution> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = AttributionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UtilisateurFinalSqlHelper.getColumns(nomUtilisateurTable, "nomUtilisateur"));
        columns.addAll(AffectationsSqlHelper.getColumns(affectationsTable, "affectations"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomUtilisateurTable)
            .on(Column.create("nom_utilisateur_id", entityTable))
            .equals(Column.create("id", nomUtilisateurTable))
            .leftOuterJoin(affectationsTable)
            .on(Column.create("affectations_id", entityTable))
            .equals(Column.create("id", affectationsTable));

        String select = entityManager.createSelect(selectFrom, Attribution.class, pageable, criteria);
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
    public Flux<Attribution> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Attribution> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Attribution process(Row row, RowMetadata metadata) {
        Attribution entity = attributionMapper.apply(row, "e");
        entity.setNomUtilisateur(utilisateurfinalMapper.apply(row, "nomUtilisateur"));
        entity.setAffectations(affectationsMapper.apply(row, "affectations"));
        return entity;
    }

    @Override
    public <S extends Attribution> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Attribution> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Attribution with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Attribution entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class AttributionSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("quantite_affecter", table, columnPrefix + "_quantite_affecter"));
        columns.add(Column.aliased("id_pers", table, columnPrefix + "_id_pers"));
        columns.add(Column.aliased("date_affectation", table, columnPrefix + "_date_affectation"));

        columns.add(Column.aliased("nom_utilisateur_id", table, columnPrefix + "_nom_utilisateur_id"));
        columns.add(Column.aliased("affectations_id", table, columnPrefix + "_affectations_id"));
        return columns;
    }
}
