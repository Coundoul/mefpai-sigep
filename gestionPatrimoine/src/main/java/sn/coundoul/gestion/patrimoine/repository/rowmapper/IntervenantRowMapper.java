package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Intervenant;
import sn.coundoul.gestion.patrimoine.domain.enumeration.TypeMaitre;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Intervenant}, with proper type conversions.
 */
@Service
public class IntervenantRowMapper implements BiFunction<Row, String, Intervenant> {

    private final ColumnConverter converter;

    public IntervenantRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Intervenant} stored in the database.
     */
    @Override
    public Intervenant apply(Row row, String prefix) {
        Intervenant entity = new Intervenant();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomIntervenant(converter.fromRow(row, prefix + "_nom_intervenant", String.class));
        entity.setPrenomIntervenant(converter.fromRow(row, prefix + "_prenom_intervenant", String.class));
        entity.setEmailProfessionnel(converter.fromRow(row, prefix + "_email_professionnel", String.class));
        entity.setRaisonSocial(converter.fromRow(row, prefix + "_raison_social", String.class));
        entity.setMaitre(converter.fromRow(row, prefix + "_maitre", TypeMaitre.class));
        entity.setRole(converter.fromRow(row, prefix + "_role", String.class));
        entity.setNomProjetId(converter.fromRow(row, prefix + "_nom_projet_id", Long.class));
        return entity;
    }
}
