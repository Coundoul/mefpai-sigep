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
import sn.coundoul.gestion.patrimoine.domain.Batiment;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.BatimentRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.CorpsEtatRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.EtablissementRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Batiment entity.
 */
@SuppressWarnings("unused")
class BatimentRepositoryInternalImpl implements BatimentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EtablissementRowMapper etablissementMapper;
    private final CorpsEtatRowMapper corpsetatMapper;
    private final BatimentRowMapper batimentMapper;

    private static final Table entityTable = Table.aliased("batiment", EntityManager.ENTITY_ALIAS);
    private static final Table nomEtablissementTable = Table.aliased("etablissement", "nomEtablissement");
    private static final Table nomCorpsTable = Table.aliased("corps_etat", "nomCorps");

    public BatimentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EtablissementRowMapper etablissementMapper,
        CorpsEtatRowMapper corpsetatMapper,
        BatimentRowMapper batimentMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.etablissementMapper = etablissementMapper;
        this.corpsetatMapper = corpsetatMapper;
        this.batimentMapper = batimentMapper;
    }

    @Override
    public Flux<Batiment> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Batiment> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Batiment> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = BatimentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EtablissementSqlHelper.getColumns(nomEtablissementTable, "nomEtablissement"));
        columns.addAll(CorpsEtatSqlHelper.getColumns(nomCorpsTable, "nomCorps"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomEtablissementTable)
            .on(Column.create("nom_etablissement_id", entityTable))
            .equals(Column.create("id", nomEtablissementTable))
            .leftOuterJoin(nomCorpsTable)
            .on(Column.create("nom_corps_id", entityTable))
            .equals(Column.create("id", nomCorpsTable));

        String select = entityManager.createSelect(selectFrom, Batiment.class, pageable, criteria);
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
    public Flux<Batiment> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Batiment> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Batiment process(Row row, RowMetadata metadata) {
        Batiment entity = batimentMapper.apply(row, "e");
        entity.setNomEtablissement(etablissementMapper.apply(row, "nomEtablissement"));
        entity.setNomCorps(corpsetatMapper.apply(row, "nomCorps"));
        return entity;
    }

    @Override
    public <S extends Batiment> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Batiment> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Batiment with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Batiment entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class BatimentSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_batiment", table, columnPrefix + "_nom_batiment"));
        columns.add(Column.aliased("nbr_piece", table, columnPrefix + "_nbr_piece"));
        columns.add(Column.aliased("designation", table, columnPrefix + "_designation"));
        columns.add(Column.aliased("surface", table, columnPrefix + "_surface"));
        columns.add(Column.aliased("etat_general", table, columnPrefix + "_etat_general"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("nombre_salle", table, columnPrefix + "_nombre_salle"));

        columns.add(Column.aliased("nom_etablissement_id", table, columnPrefix + "_nom_etablissement_id"));
        columns.add(Column.aliased("nom_corps_id", table, columnPrefix + "_nom_corps_id"));
        return columns;
    }
}
