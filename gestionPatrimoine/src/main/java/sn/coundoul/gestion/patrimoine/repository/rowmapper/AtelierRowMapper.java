package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Atelier;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Atelier}, with proper type conversions.
 */
@Service
public class AtelierRowMapper implements BiFunction<Row, String, Atelier> {

    private final ColumnConverter converter;

    public AtelierRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Atelier} stored in the database.
     */
    @Override
    public Atelier apply(Row row, String prefix) {
        Atelier entity = new Atelier();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomAtelier(converter.fromRow(row, prefix + "_nom_atelier", String.class));
        entity.setSurface(converter.fromRow(row, prefix + "_surface", Double.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setNomFiliereId(converter.fromRow(row, prefix + "_nom_filiere_id", Long.class));
        entity.setNomBatimentId(converter.fromRow(row, prefix + "_nom_batiment_id", Long.class));
        return entity;
    }
}
