package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.FicheTechniqueMaintenance;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link FicheTechniqueMaintenance}, with proper type conversions.
 */
@Service
public class FicheTechniqueMaintenanceRowMapper implements BiFunction<Row, String, FicheTechniqueMaintenance> {

    private final ColumnConverter converter;

    public FicheTechniqueMaintenanceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FicheTechniqueMaintenance} stored in the database.
     */
    @Override
    public FicheTechniqueMaintenance apply(Row row, String prefix) {
        FicheTechniqueMaintenance entity = new FicheTechniqueMaintenance();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPieceJointe(converter.fromRow(row, prefix + "_piece_jointe", String.class));
        entity.setIdPers(converter.fromRow(row, prefix + "_id_pers", Integer.class));
        entity.setDateDepot(converter.fromRow(row, prefix + "_date_depot", Instant.class));
        entity.setTypeId(converter.fromRow(row, prefix + "_type_id", Long.class));
        return entity;
    }
}
