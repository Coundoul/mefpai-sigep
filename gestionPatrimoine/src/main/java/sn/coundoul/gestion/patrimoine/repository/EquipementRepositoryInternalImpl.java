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
import sn.coundoul.gestion.patrimoine.domain.Equipement;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.BonRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.CategorieMatiereRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.EquipementRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.FournisseurRowMapper;
import sn.coundoul.gestion.patrimoine.repository.rowmapper.MagazinRowMapper;
import sn.coundoul.gestion.patrimoine.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the Equipement entity.
 */
@SuppressWarnings("unused")
class EquipementRepositoryInternalImpl implements EquipementRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MagazinRowMapper magazinMapper;
    private final FournisseurRowMapper fournisseurMapper;
    private final BonRowMapper bonMapper;
    private final CategorieMatiereRowMapper categoriematiereMapper;
    private final EquipementRowMapper equipementMapper;

    private static final Table entityTable = Table.aliased("equipement", EntityManager.ENTITY_ALIAS);
    private static final Table nomMagazinTable = Table.aliased("magazin", "nomMagazin");
    private static final Table nomFournisseurTable = Table.aliased("fournisseur", "nomFournisseur");
    private static final Table bonTable = Table.aliased("bon", "bon");
    private static final Table categorieTable = Table.aliased("categorie_matiere", "categorie");

    public EquipementRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MagazinRowMapper magazinMapper,
        FournisseurRowMapper fournisseurMapper,
        BonRowMapper bonMapper,
        CategorieMatiereRowMapper categoriematiereMapper,
        EquipementRowMapper equipementMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.magazinMapper = magazinMapper;
        this.fournisseurMapper = fournisseurMapper;
        this.bonMapper = bonMapper;
        this.categoriematiereMapper = categoriematiereMapper;
        this.equipementMapper = equipementMapper;
    }

    @Override
    public Flux<Equipement> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Equipement> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Equipement> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = EquipementSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MagazinSqlHelper.getColumns(nomMagazinTable, "nomMagazin"));
        columns.addAll(FournisseurSqlHelper.getColumns(nomFournisseurTable, "nomFournisseur"));
        columns.addAll(BonSqlHelper.getColumns(bonTable, "bon"));
        columns.addAll(CategorieMatiereSqlHelper.getColumns(categorieTable, "categorie"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nomMagazinTable)
            .on(Column.create("nom_magazin_id", entityTable))
            .equals(Column.create("id", nomMagazinTable))
            .leftOuterJoin(nomFournisseurTable)
            .on(Column.create("nom_fournisseur_id", entityTable))
            .equals(Column.create("id", nomFournisseurTable))
            .leftOuterJoin(bonTable)
            .on(Column.create("bon_id", entityTable))
            .equals(Column.create("id", bonTable))
            .leftOuterJoin(categorieTable)
            .on(Column.create("categorie_id", entityTable))
            .equals(Column.create("id", categorieTable));

        String select = entityManager.createSelect(selectFrom, Equipement.class, pageable, criteria);
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
    public Flux<Equipement> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Equipement> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Equipement process(Row row, RowMetadata metadata) {
        Equipement entity = equipementMapper.apply(row, "e");
        entity.setNomMagazin(magazinMapper.apply(row, "nomMagazin"));
        entity.setNomFournisseur(fournisseurMapper.apply(row, "nomFournisseur"));
        entity.setBon(bonMapper.apply(row, "bon"));
        entity.setCategorie(categoriematiereMapper.apply(row, "categorie"));
        return entity;
    }

    @Override
    public <S extends Equipement> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Equipement> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Equipement with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Equipement entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class EquipementSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("reference", table, columnPrefix + "_reference"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("prix_unitaire", table, columnPrefix + "_prix_unitaire"));
        columns.add(Column.aliased("type_matiere", table, columnPrefix + "_type_matiere"));
        columns.add(Column.aliased("quantite", table, columnPrefix + "_quantite"));
        columns.add(Column.aliased("etat_matiere", table, columnPrefix + "_etat_matiere"));
        columns.add(Column.aliased("jhi_group", table, columnPrefix + "_jhi_group"));
        columns.add(Column.aliased("photo", table, columnPrefix + "_photo"));
        columns.add(Column.aliased("photo_content_type", table, columnPrefix + "_photo_content_type"));

        columns.add(Column.aliased("nom_magazin_id", table, columnPrefix + "_nom_magazin_id"));
        columns.add(Column.aliased("nom_fournisseur_id", table, columnPrefix + "_nom_fournisseur_id"));
        columns.add(Column.aliased("bon_id", table, columnPrefix + "_bon_id"));
        columns.add(Column.aliased("categorie_id", table, columnPrefix + "_categorie_id"));
        return columns;
    }
}
