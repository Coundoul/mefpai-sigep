package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Bureau;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Direction;
import sn.coundoul.gestion.patrimoine.domain.enumeration.NomS;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Bureau}, with proper type conversions.
 */
@Service
public class BureauRowMapper implements BiFunction<Row, String, Bureau> {

    private final ColumnConverter converter;

    public BureauRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Bureau} stored in the database.
     */
    @Override
    public Bureau apply(Row row, String prefix) {
        Bureau entity = new Bureau();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomStructure(converter.fromRow(row, prefix + "_nom_structure", NomS.class));
        entity.setDirection(converter.fromRow(row, prefix + "_direction", Direction.class));
        entity.setNomEtablissement(converter.fromRow(row, prefix + "_nom_etablissement", String.class));
        return entity;
    }
}
