package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.ChefMaintenance;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Sexe;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link ChefMaintenance}, with proper type conversions.
 */
@Service
public class ChefMaintenanceRowMapper implements BiFunction<Row, String, ChefMaintenance> {

    private final ColumnConverter converter;

    public ChefMaintenanceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ChefMaintenance} stored in the database.
     */
    @Override
    public ChefMaintenance apply(Row row, String prefix) {
        ChefMaintenance entity = new ChefMaintenance();
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
