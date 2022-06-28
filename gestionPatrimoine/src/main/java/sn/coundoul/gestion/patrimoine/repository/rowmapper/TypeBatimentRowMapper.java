package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.TypeBatiment;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link TypeBatiment}, with proper type conversions.
 */
@Service
public class TypeBatimentRowMapper implements BiFunction<Row, String, TypeBatiment> {

    private final ColumnConverter converter;

    public TypeBatimentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TypeBatiment} stored in the database.
     */
    @Override
    public TypeBatiment apply(Row row, String prefix) {
        TypeBatiment entity = new TypeBatiment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTypeBa(converter.fromRow(row, prefix + "_type_ba", String.class));
        entity.setNomBatimentId(converter.fromRow(row, prefix + "_nom_batiment_id", Long.class));
        return entity;
    }
}
