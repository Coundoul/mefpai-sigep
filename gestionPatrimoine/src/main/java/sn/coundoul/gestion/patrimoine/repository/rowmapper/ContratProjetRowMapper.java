package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.ContratProjet;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link ContratProjet}, with proper type conversions.
 */
@Service
public class ContratProjetRowMapper implements BiFunction<Row, String, ContratProjet> {

    private final ColumnConverter converter;

    public ContratProjetRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ContratProjet} stored in the database.
     */
    @Override
    public ContratProjet apply(Row row, String prefix) {
        ContratProjet entity = new ContratProjet();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        return entity;
    }
}
