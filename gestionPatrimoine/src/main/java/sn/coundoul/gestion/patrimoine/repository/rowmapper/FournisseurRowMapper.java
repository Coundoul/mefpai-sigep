package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Fournisseur;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Fournisseur}, with proper type conversions.
 */
@Service
public class FournisseurRowMapper implements BiFunction<Row, String, Fournisseur> {

    private final ColumnConverter converter;

    public FournisseurRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Fournisseur} stored in the database.
     */
    @Override
    public Fournisseur apply(Row row, String prefix) {
        Fournisseur entity = new Fournisseur();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodeFournisseuer(converter.fromRow(row, prefix + "_code_fournisseuer", String.class));
        entity.setNomFournisseur(converter.fromRow(row, prefix + "_nom_fournisseur", String.class));
        entity.setPrenomFournisseur(converter.fromRow(row, prefix + "_prenom_fournisseur", String.class));
        entity.setSexe(converter.fromRow(row, prefix + "_sexe", Sexe.class));
        entity.setRaisonSocial(converter.fromRow(row, prefix + "_raison_social", String.class));
        entity.setAdresse(converter.fromRow(row, prefix + "_adresse", String.class));
        entity.setNum1(converter.fromRow(row, prefix + "_num_1", String.class));
        entity.setNum2(converter.fromRow(row, prefix + "_num_2", String.class));
        entity.setVille(converter.fromRow(row, prefix + "_ville", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        return entity;
    }
}
