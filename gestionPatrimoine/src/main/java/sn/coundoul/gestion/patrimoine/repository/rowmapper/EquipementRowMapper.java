package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Equipement;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Equipement}, with proper type conversions.
 */
@Service
public class EquipementRowMapper implements BiFunction<Row, String, Equipement> {

    private final ColumnConverter converter;

    public EquipementRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Equipement} stored in the database.
     */
    @Override
    public Equipement apply(Row row, String prefix) {
        Equipement entity = new Equipement();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setReference(converter.fromRow(row, prefix + "_reference", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setPrixUnitaire(converter.fromRow(row, prefix + "_prix_unitaire", Integer.class));
        entity.setTypeMatiere(converter.fromRow(row, prefix + "_type_matiere", String.class));
        entity.setQuantite(converter.fromRow(row, prefix + "_quantite", Integer.class));
        entity.setEtatMatiere(converter.fromRow(row, prefix + "_etat_matiere", String.class));
        entity.setGroup(converter.fromRow(row, prefix + "_jhi_group", Boolean.class));
        entity.setPhotoContentType(converter.fromRow(row, prefix + "_photo_content_type", String.class));
        entity.setPhoto(converter.fromRow(row, prefix + "_photo", byte[].class));
        entity.setNomMagazinId(converter.fromRow(row, prefix + "_nom_magazin_id", Long.class));
        entity.setNomFournisseurId(converter.fromRow(row, prefix + "_nom_fournisseur_id", Long.class));
        entity.setBonId(converter.fromRow(row, prefix + "_bon_id", Long.class));
        entity.setCategorieId(converter.fromRow(row, prefix + "_categorie_id", Long.class));
        return entity;
    }
}
