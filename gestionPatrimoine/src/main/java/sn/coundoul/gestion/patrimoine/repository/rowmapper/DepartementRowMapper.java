package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Departement;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Departement}, with proper type conversions.
 */
@Service
public class DepartementRowMapper implements BiFunction<Row, String, Departement> {

    private final ColumnConverter converter;

    public DepartementRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Departement} stored in the database.
     */
    @Override
    public Departement apply(Row row, String prefix) {
        Departement entity = new Departement();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomDepartement(converter.fromRow(row, prefix + "_nom_departement", String.class));
        entity.setNomRegionId(converter.fromRow(row, prefix + "_nom_region_id", Long.class));
        return entity;
    }
}
