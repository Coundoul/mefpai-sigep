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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.coundoul.gestion.patrimoine.domain.UtilisateurFinal;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.UtilisateurFinalRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the UtilisateurFinal entity.
 */
@SuppressWarnings("unused")
class UtilisateurFinalRepositoryInternalImpl implements UtilisateurFinalRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UtilisateurFinalRowMapper utilisateurfinalMapper;

    private static final Table entityTable = Table.aliased("utilisateur_final", EntityManager.ENTITY_ALIAS);

    public UtilisateurFinalRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UtilisateurFinalRowMapper utilisateurfinalMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.utilisateurfinalMapper = utilisateurfinalMapper;
    }

    @Override
    public Flux<UtilisateurFinal> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<UtilisateurFinal> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<UtilisateurFinal> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = UtilisateurFinalSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, UtilisateurFinal.class, pageable, criteria);
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
    public Flux<UtilisateurFinal> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<UtilisateurFinal> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private UtilisateurFinal process(Row row, RowMetadata metadata) {
        UtilisateurFinal entity = utilisateurfinalMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends UtilisateurFinal> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends UtilisateurFinal> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update UtilisateurFinal with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(UtilisateurFinal entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class UtilisateurFinalSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_utilisateur", table, columnPrefix + "_nom_utilisateur"));
        columns.add(Column.aliased("prenom_utilisateur", table, columnPrefix + "_prenom_utilisateur"));
        columns.add(Column.aliased("email_institutionnel", table, columnPrefix + "_email_institutionnel"));
        columns.add(Column.aliased("mobile", table, columnPrefix + "_mobile"));
        columns.add(Column.aliased("sexe", table, columnPrefix + "_sexe"));
        columns.add(Column.aliased("departement", table, columnPrefix + "_departement"));
        columns.add(Column.aliased("service_dep", table, columnPrefix + "_service_dep"));

        return columns;
    }
}
