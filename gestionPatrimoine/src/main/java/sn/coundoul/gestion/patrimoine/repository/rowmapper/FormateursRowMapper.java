package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Formateurs;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Formateurs}, with proper type conversions.
 */
@Service
public class FormateursRowMapper implements BiFunction<Row, String, Formateurs> {

    private final ColumnConverter converter;

    public FormateursRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Formateurs} stored in the database.
     */
    @Override
    public Formateurs apply(Row row, String prefix) {
        Formateurs entity = new Formateurs();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomFormateur(converter.fromRow(row, prefix + "_nom_formateur", String.class));
        entity.setPrenomFormateur(converter.fromRow(row, prefix + "_prenom_formateur", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setNumb1(converter.fromRow(row, prefix + "_numb_1", String.class));
        entity.setNumb2(converter.fromRow(row, prefix + "_numb_2", String.class));
        entity.setAdresse(converter.fromRow(row, prefix + "_adresse", String.class));
        entity.setVille(converter.fromRow(row, prefix + "_ville", String.class));
        entity.setSpecialite(converter.fromRow(row, prefix + "_specialite", String.class));
        entity.setNomEtablissementId(converter.fromRow(row, prefix + "_nom_etablissement_id", Long.class));
        return entity;
    }
}
