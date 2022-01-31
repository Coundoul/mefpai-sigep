package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Attribution;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Attribution}, with proper type conversions.
 */
@Service
public class AttributionRowMapper implements BiFunction<Row, String, Attribution> {

    private final ColumnConverter converter;

    public AttributionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Attribution} stored in the database.
     */
    @Override
    public Attribution apply(Row row, String prefix) {
        Attribution entity = new Attribution();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setQuantiteAffecter(converter.fromRow(row, prefix + "_quantite_affecter", Integer.class));
        entity.setIdPers(converter.fromRow(row, prefix + "_id_pers", Integer.class));
        entity.setDateAffectation(converter.fromRow(row, prefix + "_date_affectation", Instant.class));
        entity.setNomUtilisateurId(converter.fromRow(row, prefix + "_nom_utilisateur_id", Long.class));
        entity.setAffectationsId(converter.fromRow(row, prefix + "_affectations_id", Long.class));
        return entity;
    }
}
