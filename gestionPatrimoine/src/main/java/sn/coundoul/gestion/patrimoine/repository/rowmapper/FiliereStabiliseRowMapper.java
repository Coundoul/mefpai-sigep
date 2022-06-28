package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.FiliereStabilise;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link FiliereStabilise}, with proper type conversions.
 */
@Service
public class FiliereStabiliseRowMapper implements BiFunction<Row, String, FiliereStabilise> {

    private final ColumnConverter converter;

    public FiliereStabiliseRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FiliereStabilise} stored in the database.
     */
    @Override
    public FiliereStabilise apply(Row row, String prefix) {
        FiliereStabilise entity = new FiliereStabilise();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomFiliere(converter.fromRow(row, prefix + "_nom_filiere", String.class));
        entity.setNomFormateurId(converter.fromRow(row, prefix + "_nom_formateur_id", Long.class));
        return entity;
    }
}
