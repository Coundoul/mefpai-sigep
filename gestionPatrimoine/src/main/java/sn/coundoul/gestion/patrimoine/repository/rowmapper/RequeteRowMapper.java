package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Requete;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Requete}, with proper type conversions.
 */
@Service
public class RequeteRowMapper implements BiFunction<Row, String, Requete> {

    private final ColumnConverter converter;

    public RequeteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Requete} stored in the database.
     */
    @Override
    public Requete apply(Row row, String prefix) {
        Requete entity = new Requete();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setTypePanne(converter.fromRow(row, prefix + "_type_panne", Double.class));
        entity.setDatePost(converter.fromRow(row, prefix + "_date_post", Double.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setEtatTraite(converter.fromRow(row, prefix + "_etat_traite", Boolean.class));
        entity.setDateLancement(converter.fromRow(row, prefix + "_date_lancement", Instant.class));
        entity.setIdPers(converter.fromRow(row, prefix + "_id_pers", Integer.class));
        entity.setNomStructureId(converter.fromRow(row, prefix + "_nom_structure_id", Long.class));
        return entity;
    }
}
