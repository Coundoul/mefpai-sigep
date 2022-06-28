package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.UtilisateurFinal;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link UtilisateurFinal}, with proper type conversions.
 */
@Service
public class UtilisateurFinalRowMapper implements BiFunction<Row, String, UtilisateurFinal> {

    private final ColumnConverter converter;

    public UtilisateurFinalRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UtilisateurFinal} stored in the database.
     */
    @Override
    public UtilisateurFinal apply(Row row, String prefix) {
        UtilisateurFinal entity = new UtilisateurFinal();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomUtilisateur(converter.fromRow(row, prefix + "_nom_utilisateur", String.class));
        entity.setPrenomUtilisateur(converter.fromRow(row, prefix + "_prenom_utilisateur", String.class));
        entity.setEmailInstitutionnel(converter.fromRow(row, prefix + "_email_institutionnel", String.class));
        entity.setMobile(converter.fromRow(row, prefix + "_mobile", String.class));
        entity.setSexe(converter.fromRow(row, prefix + "_sexe", String.class));
        entity.setDepartement(converter.fromRow(row, prefix + "_departement", String.class));
        entity.setServiceDep(converter.fromRow(row, prefix + "_service_dep", String.class));
        return entity;
    }
}
