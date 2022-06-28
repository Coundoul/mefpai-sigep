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
import sn.coundoul.gestion.patrimoine.domain.ComptableSecondaire;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ComptablePrincipaleRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ComptableSecondaireRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the ComptableSecondaire entity.
 */
@SuppressWarnings("unused")
class ComptableSecondaireRepositoryInternalImpl implements ComptableSecondaireRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ComptablePrincipaleRowMapper comptableprincipaleMapper;
    private final ComptableSecondaireRowMapper comptablesecondaireMapper;

    private static final Table entityTable = Table.aliased("comptable_secondaire", EntityManager.ENTITY_ALIAS);
    private static final Table comptablePrincipaleTable = Table.aliased("comptable_principale", "comptablePrincipale");

    public ComptableSecondaireRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ComptablePrincipaleRowMapper comptableprincipaleMapper,
        ComptableSecondaireRowMapper comptablesecondaireMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.comptableprincipaleMapper = comptableprincipaleMapper;
        this.comptablesecondaireMapper = comptablesecondaireMapper;
    }

    @Override
    public Flux<ComptableSecondaire> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<ComptableSecondaire> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<ComptableSecondaire> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = ComptableSecondaireSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ComptablePrincipaleSqlHelper.getColumns(comptablePrincipaleTable, "comptablePrincipale"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(comptablePrincipaleTable)
            .on(Column.create("comptable_principale_id", entityTable))
            .equals(Column.create("id", comptablePrincipaleTable));

        String select = entityManager.createSelect(selectFrom, ComptableSecondaire.class, pageable, criteria);
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
    public Flux<ComptableSecondaire> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<ComptableSecondaire> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private ComptableSecondaire process(Row row, RowMetadata metadata) {
        ComptableSecondaire entity = comptablesecondaireMapper.apply(row, "e");
        entity.setComptablePrincipale(comptableprincipaleMapper.apply(row, "comptablePrincipale"));
        return entity;
    }

    @Override
    public <S extends ComptableSecondaire> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends ComptableSecondaire> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update ComptableSecondaire with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(ComptableSecondaire entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class ComptableSecondaireSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_pers", table, columnPrefix + "_nom_pers"));
        columns.add(Column.aliased("prenom_pers", table, columnPrefix + "_prenom_pers"));
        columns.add(Column.aliased("sexe", table, columnPrefix + "_sexe"));
        columns.add(Column.aliased("mobile", table, columnPrefix + "_mobile"));
        columns.add(Column.aliased("adresse", table, columnPrefix + "_adresse"));
        columns.add(Column.aliased("direction", table, columnPrefix + "_direction"));

        columns.add(Column.aliased("comptable_principale_id", table, columnPrefix + "_comptable_principale_id"));
        return columns;
    }
}
