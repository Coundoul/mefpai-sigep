package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Etablissement;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Etablissement}, with proper type conversions.
 */
@Service
public class EtablissementRowMapper implements BiFunction<Row, String, Etablissement> {

    private final ColumnConverter converter;

    public EtablissementRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Etablissement} stored in the database.
     */
    @Override
    public Etablissement apply(Row row, String prefix) {
        Etablissement entity = new Etablissement();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomEtablissement(converter.fromRow(row, prefix + "_nom_etablissement", String.class));
        entity.setSurfaceBatie(converter.fromRow(row, prefix + "_surface_batie", Double.class));
        entity.setSuperficie(converter.fromRow(row, prefix + "_superficie", Double.class));
        entity.setIdPers(converter.fromRow(row, prefix + "_id_pers", Integer.class));
        entity.setNomQuartierId(converter.fromRow(row, prefix + "_nom_quartier_id", Long.class));
        return entity;
    }
}
