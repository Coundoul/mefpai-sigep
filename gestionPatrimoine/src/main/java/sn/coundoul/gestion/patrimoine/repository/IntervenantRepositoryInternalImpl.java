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
import sn.coundoul.gestion.patrimoine.domain.Intervenant;
import sn.coundoul.gestion.patrimoine.domain.enumeration.TypeMaitre;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.IntervenantRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.ProjetsRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Intervenant entity.
 */
@SuppressWarnings("unused")
class IntervenantRepositoryInternalImpl implements IntervenantRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProjetsRowMapper projetsMapper;
    private final IntervenantRowMapper intervenantMapper;

    private static final Table entityTable = Table.aliased("intervenant", EntityManager.ENTITY_ALIAS);
    private static final Table nomProjetTable = Table.aliased("projets", "nomProjet");

    public IntervenantRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProjetsRowMapper projetsMapper,
        IntervenantRowMapper intervenantMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.projetsMapper = projetsMapper;
        this.intervenantMapper = intervenantMapper;
    }

    @Override
    public Flux<Intervenant> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Intervenant> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Intervenant> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = IntervenantSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProjetsSqlHelper.getColumns(nomProjetTable, "nomProjet"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomProjetTable)
            .on(Column.create("nom_projet_id", entityTable))
            .equals(Column.create("id", nomProjetTable));

        String select = entityManager.createSelect(selectFrom, Intervenant.class, pageable, criteria);
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
    public Flux<Intervenant> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Intervenant> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Intervenant process(Row row, RowMetadata metadata) {
        Intervenant entity = intervenantMapper.apply(row, "e");
        entity.setNomProjet(projetsMapper.apply(row, "nomProjet"));
        return entity;
    }

    @Override
    public <S extends Intervenant> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Intervenant> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Intervenant with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Intervenant entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class IntervenantSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_intervenant", table, columnPrefix + "_nom_intervenant"));
        columns.add(Column.aliased("prenom_intervenant", table, columnPrefix + "_prenom_intervenant"));
        columns.add(Column.aliased("email_professionnel", table, columnPrefix + "_email_professionnel"));
        columns.add(Column.aliased("raison_social", table, columnPrefix + "_raison_social"));
        columns.add(Column.aliased("maitre", table, columnPrefix + "_maitre"));
        columns.add(Column.aliased("role", table, columnPrefix + "_role"));

        columns.add(Column.aliased("nom_projet_id", table, columnPrefix + "_nom_projet_id"));
        return columns;
    }
}
