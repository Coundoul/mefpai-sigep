package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Responsable;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Responsable}, with proper type conversions.
 */
@Service
public class ResponsableRowMapper implements BiFunction<Row, String, Responsable> {

    private final ColumnConverter converter;

    public ResponsableRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Responsable} stored in the database.
     */
    @Override
    public Responsable apply(Row row, String prefix) {
        Responsable entity = new Responsable();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomResponsable(converter.fromRow(row, prefix + "_nom_responsable", String.class));
        entity.setPrenomResponsable(converter.fromRow(row, prefix + "_prenom_responsable", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setSpecialite(converter.fromRow(row, prefix + "_specialite", String.class));
        entity.setNumb1(converter.fromRow(row, prefix + "_numb_1", String.class));
        entity.setNumb2(converter.fromRow(row, prefix + "_numb_2", String.class));
        entity.setRaisonSocial(converter.fromRow(row, prefix + "_raison_social", String.class));
        return entity;
    }
}
