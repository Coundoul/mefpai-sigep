package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Magazin;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Magazin}, with proper type conversions.
 */
@Service
public class MagazinRowMapper implements BiFunction<Row, String, Magazin> {

    private final ColumnConverter converter;

    public MagazinRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Magazin} stored in the database.
     */
    @Override
    public Magazin apply(Row row, String prefix) {
        Magazin entity = new Magazin();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomMagazin(converter.fromRow(row, prefix + "_nom_magazin", String.class));
        entity.setSurfaceBatie(converter.fromRow(row, prefix + "_surface_batie", Double.class));
        entity.setSuperficie(converter.fromRow(row, prefix + "_superficie", Double.class));
        entity.setIdPers(converter.fromRow(row, prefix + "_id_pers", Integer.class));
        entity.setNomQuartierId(converter.fromRow(row, prefix + "_nom_quartier_id", Long.class));
        return entity;
    }
}
