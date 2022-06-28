package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Commune;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Commune}, with proper type conversions.
 */
@Service
public class CommuneRowMapper implements BiFunction<Row, String, Commune> {

    private final ColumnConverter converter;

    public CommuneRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Commune} stored in the database.
     */
    @Override
    public Commune apply(Row row, String prefix) {
        Commune entity = new Commune();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomCommune(converter.fromRow(row, prefix + "_nom_commune", String.class));
        entity.setNomDepartementId(converter.fromRow(row, prefix + "_nom_departement_id", Long.class));
        return entity;
    }
}
