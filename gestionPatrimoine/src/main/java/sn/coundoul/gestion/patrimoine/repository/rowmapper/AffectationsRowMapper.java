package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Affectations;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Type;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Affectations}, with proper type conversions.
 */
@Service
public class AffectationsRowMapper implements BiFunction<Row, String, Affectations> {

    private final ColumnConverter converter;

    public AffectationsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Affectations} stored in the database.
     */
    @Override
    public Affectations apply(Row row, String prefix) {
        Affectations entity = new Affectations();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setQuantiteAffecter(converter.fromRow(row, prefix + "_quantite_affecter", Integer.class));
        entity.setTypeAttribution(converter.fromRow(row, prefix + "_type_attribution", Type.class));
        entity.setIdPers(converter.fromRow(row, prefix + "_id_pers", Integer.class));
        entity.setDateAttribution(converter.fromRow(row, prefix + "_date_attribution", Instant.class));
        entity.setEquipementId(converter.fromRow(row, prefix + "_equipement_id", Long.class));
        return entity;
    }
}
