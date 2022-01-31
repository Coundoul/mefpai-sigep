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
import sn.coundoul.gestion.patrimoine.domain.ComptablePrincipale;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ComptablePrincipaleRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the ComptablePrincipale entity.
 */
@SuppressWarnings("unused")
class ComptablePrincipaleRepositoryInternalImpl implements ComptablePrincipaleRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ComptablePrincipaleRowMapper comptableprincipaleMapper;

    private static final Table entityTable = Table.aliased("comptable_principale", EntityManager.ENTITY_ALIAS);

    public ComptablePrincipaleRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ComptablePrincipaleRowMapper comptableprincipaleMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.comptableprincipaleMapper = comptableprincipaleMapper;
    }

    @Override
    public Flux<ComptablePrincipale> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<ComptablePrincipale> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<ComptablePrincipale> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = ComptablePrincipaleSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, ComptablePrincipale.class, pageable, criteria);
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
    public Flux<ComptablePrincipale> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<ComptablePrincipale> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private ComptablePrincipale process(Row row, RowMetadata metadata) {
        ComptablePrincipale entity = comptableprincipaleMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ComptablePrincipale> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends ComptablePrincipale> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update ComptablePrincipale with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(ComptablePrincipale entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class ComptablePrincipaleSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_pers", table, columnPrefix + "_nom_pers"));
        columns.add(Column.aliased("prenom_pers", table, columnPrefix + "_prenom_pers"));
        columns.add(Column.aliased("sexe", table, columnPrefix + "_sexe"));
        columns.add(Column.aliased("mobile", table, columnPrefix + "_mobile"));
        columns.add(Column.aliased("adresse", table, columnPrefix + "_adresse"));
        columns.add(Column.aliased("direction", table, columnPrefix + "_direction"));

        return columns;
    }
}
