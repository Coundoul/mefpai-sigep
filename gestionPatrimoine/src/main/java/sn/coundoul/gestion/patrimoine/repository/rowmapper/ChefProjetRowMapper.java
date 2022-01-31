package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.ChefProjet;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link ChefProjet}, with proper type conversions.
 */
@Service
public class ChefProjetRowMapper implements BiFunction<Row, String, ChefProjet> {

    private final ColumnConverter converter;

    public ChefProjetRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ChefProjet} stored in the database.
     */
    @Override
    public ChefProjet apply(Row row, String prefix) {
        ChefProjet entity = new ChefProjet();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomPers(converter.fromRow(row, prefix + "_nom_pers", String.class));
        entity.setPrenomPers(converter.fromRow(row, prefix + "_prenom_pers", String.class));
        entity.setSexe(converter.fromRow(row, prefix + "_sexe", Sexe.class));
        entity.setMobile(converter.fromRow(row, prefix + "_mobile", String.class));
        entity.setAdresse(converter.fromRow(row, prefix + "_adresse", String.class));
        entity.setDirection(converter.fromRow(row, prefix + "_direction", Direction.class));
        return entity;
    }
}
