package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Salles;
import sn.coundoul.gestion.patrimoine.domain.enumeration.Classe;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Salles}, with proper type conversions.
 */
@Service
public class SallesRowMapper implements BiFunction<Row, String, Salles> {

    private final ColumnConverter converter;

    public SallesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Salles} stored in the database.
     */
    @Override
    public Salles apply(Row row, String prefix) {
        Salles entity = new Salles();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomSalle(converter.fromRow(row, prefix + "_nom_salle", String.class));
        entity.setClasse(converter.fromRow(row, prefix + "_classe", Classe.class));
        entity.setNomBatimentId(converter.fromRow(row, prefix + "_nom_batiment_id", Long.class));
        return entity;
    }
}
