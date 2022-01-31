package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.ProjetAttribution;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link ProjetAttribution}, with proper type conversions.
 */
@Service
public class ProjetAttributionRowMapper implements BiFunction<Row, String, ProjetAttribution> {

    private final ColumnConverter converter;

    public ProjetAttributionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProjetAttribution} stored in the database.
     */
    @Override
    public ProjetAttribution apply(Row row, String prefix) {
        ProjetAttribution entity = new ProjetAttribution();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDateAttribution(converter.fromRow(row, prefix + "_date_attribution", Instant.class));
        entity.setQuantite(converter.fromRow(row, prefix + "_quantite", Integer.class));
        entity.setIdEquipement(converter.fromRow(row, prefix + "_id_equipement", Integer.class));
        entity.setIdPers(converter.fromRow(row, prefix + "_id_pers", Integer.class));
        entity.setNomProjetId(converter.fromRow(row, prefix + "_nom_projet_id", Long.class));
        return entity;
    }
}
