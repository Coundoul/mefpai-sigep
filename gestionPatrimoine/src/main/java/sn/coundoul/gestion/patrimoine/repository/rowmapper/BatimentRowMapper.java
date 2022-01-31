package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Batiment;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Batiment}, with proper type conversions.
 */
@Service
public class BatimentRowMapper implements BiFunction<Row, String, Batiment> {

    private final ColumnConverter converter;

    public BatimentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Batiment} stored in the database.
     */
    @Override
    public Batiment apply(Row row, String prefix) {
        Batiment entity = new Batiment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomBatiment(converter.fromRow(row, prefix + "_nom_batiment", String.class));
        entity.setNbrPiece(converter.fromRow(row, prefix + "_nbr_piece", String.class));
        entity.setDesignation(converter.fromRow(row, prefix + "_designation", String.class));
        entity.setSurface(converter.fromRow(row, prefix + "_surface", Double.class));
        entity.setEtatGeneral(converter.fromRow(row, prefix + "_etat_general", Boolean.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setNombreSalle(converter.fromRow(row, prefix + "_nombre_salle", Integer.class));
        entity.setNomEtablissementId(converter.fromRow(row, prefix + "_nom_etablissement_id", Long.class));
        entity.setNomCorpsId(converter.fromRow(row, prefix + "_nom_corps_id", Long.class));
        return entity;
    }
}
