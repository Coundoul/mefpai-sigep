package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.CorpsEtat;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link CorpsEtat}, with proper type conversions.
 */
@Service
public class CorpsEtatRowMapper implements BiFunction<Row, String, CorpsEtat> {

    private final ColumnConverter converter;

    public CorpsEtatRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CorpsEtat} stored in the database.
     */
    @Override
    public CorpsEtat apply(Row row, String prefix) {
        CorpsEtat entity = new CorpsEtat();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomCorps(converter.fromRow(row, prefix + "_nom_corps", String.class));
        entity.setGrosOeuvre(converter.fromRow(row, prefix + "_gros_oeuvre", String.class));
        entity.setDescriptionGrosOeuvre(converter.fromRow(row, prefix + "_description_gros_oeuvre", String.class));
        entity.setSecondOeuvre(converter.fromRow(row, prefix + "_second_oeuvre", String.class));
        entity.setDescriptionSecondOeuvre(converter.fromRow(row, prefix + "_description_second_oeuvre", String.class));
        entity.setOservation(converter.fromRow(row, prefix + "_oservation", Boolean.class));
        entity.setEtatCorps(converter.fromRow(row, prefix + "_etat_corps", Boolean.class));
        entity.setNomResponsableId(converter.fromRow(row, prefix + "_nom_responsable_id", Long.class));
        return entity;
    }
}
