package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.NatureFoncier;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link NatureFoncier}, with proper type conversions.
 */
@Service
public class NatureFoncierRowMapper implements BiFunction<Row, String, NatureFoncier> {

    private final ColumnConverter converter;

    public NatureFoncierRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link NatureFoncier} stored in the database.
     */
    @Override
    public NatureFoncier apply(Row row, String prefix) {
        NatureFoncier entity = new NatureFoncier();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTypeFoncier(converter.fromRow(row, prefix + "_type_foncier", String.class));
        entity.setPieceJointe(converter.fromRow(row, prefix + "_piece_jointe", String.class));
        entity.setNomCorpsId(converter.fromRow(row, prefix + "_nom_corps_id", Long.class));
        return entity;
    }
}
