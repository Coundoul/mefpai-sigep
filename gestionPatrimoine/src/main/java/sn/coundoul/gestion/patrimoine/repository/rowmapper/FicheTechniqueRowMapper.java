package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.FicheTechnique;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link FicheTechnique}, with proper type conversions.
 */
@Service
public class FicheTechniqueRowMapper implements BiFunction<Row, String, FicheTechnique> {

    private final ColumnConverter converter;

    public FicheTechniqueRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FicheTechnique} stored in the database.
     */
    @Override
    public FicheTechnique apply(Row row, String prefix) {
        FicheTechnique entity = new FicheTechnique();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPieceJointe(converter.fromRow(row, prefix + "_piece_jointe", String.class));
        entity.setDateDepot(converter.fromRow(row, prefix + "_date_depot", Instant.class));
        entity.setNomResponsableId(converter.fromRow(row, prefix + "_nom_responsable_id", Long.class));
        return entity;
    }
}
