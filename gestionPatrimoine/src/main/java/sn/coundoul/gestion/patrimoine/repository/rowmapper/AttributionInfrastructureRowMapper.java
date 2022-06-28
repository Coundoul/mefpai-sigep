package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.AttributionInfrastructure;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link AttributionInfrastructure}, with proper type conversions.
 */
@Service
public class AttributionInfrastructureRowMapper implements BiFunction<Row, String, AttributionInfrastructure> {

    private final ColumnConverter converter;

    public AttributionInfrastructureRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AttributionInfrastructure} stored in the database.
     */
    @Override
    public AttributionInfrastructure apply(Row row, String prefix) {
        AttributionInfrastructure entity = new AttributionInfrastructure();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDateAttribution(converter.fromRow(row, prefix + "_date_attribution", Instant.class));
        entity.setQuantite(converter.fromRow(row, prefix + "_quantite", Integer.class));
        entity.setIdEquipement(converter.fromRow(row, prefix + "_id_equipement", Integer.class));
        entity.setIdPers(converter.fromRow(row, prefix + "_id_pers", Integer.class));
        entity.setNomEtablissementId(converter.fromRow(row, prefix + "_nom_etablissement_id", Long.class));
        return entity;
    }
}
