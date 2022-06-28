package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Projets;
import sn.coundoul.gestion.patrimoine.domain.enumeration.TypeProjet;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Projets}, with proper type conversions.
 */
@Service
public class ProjetsRowMapper implements BiFunction<Row, String, Projets> {

    private final ColumnConverter converter;

    public ProjetsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Projets} stored in the database.
     */
    @Override
    public Projets apply(Row row, String prefix) {
        Projets entity = new Projets();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTypeProjet(converter.fromRow(row, prefix + "_type_projet", TypeProjet.class));
        entity.setNomProjet(converter.fromRow(row, prefix + "_nom_projet", String.class));
        entity.setDateDebut(converter.fromRow(row, prefix + "_date_debut", ZonedDateTime.class));
        entity.setDateFin(converter.fromRow(row, prefix + "_date_fin", ZonedDateTime.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setExtension(converter.fromRow(row, prefix + "_extension", Boolean.class));
        entity.setNomId(converter.fromRow(row, prefix + "_nom_id", Long.class));
        entity.setNomEtablissementId(converter.fromRow(row, prefix + "_nom_etablissement_id", Long.class));
        entity.setNomBatimentId(converter.fromRow(row, prefix + "_nom_batiment_id", Long.class));
        return entity;
    }
}
