package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Etapes;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Etapes}, with proper type conversions.
 */
@Service
public class EtapesRowMapper implements BiFunction<Row, String, Etapes> {

    private final ColumnConverter converter;

    public EtapesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Etapes} stored in the database.
     */
    @Override
    public Etapes apply(Row row, String prefix) {
        Etapes entity = new Etapes();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDateDebut(converter.fromRow(row, prefix + "_date_debut", ZonedDateTime.class));
        entity.setDateFin(converter.fromRow(row, prefix + "_date_fin", ZonedDateTime.class));
        entity.setNomTache(converter.fromRow(row, prefix + "_nom_tache", String.class));
        entity.setDuration(converter.fromRow(row, prefix + "_duration", Duration.class));
        entity.setNomProjetId(converter.fromRow(row, prefix + "_nom_projet_id", Long.class));
        return entity;
    }
}
