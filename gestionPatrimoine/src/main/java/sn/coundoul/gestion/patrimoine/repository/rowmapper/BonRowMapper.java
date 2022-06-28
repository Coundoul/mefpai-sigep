package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.Bon;
import sn.coundoul.gestion.patrimoine.domain.enumeration.TypeBon;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link Bon}, with proper type conversions.
 */
@Service
public class BonRowMapper implements BiFunction<Row, String, Bon> {

    private final ColumnConverter converter;

    public BonRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Bon} stored in the database.
     */
    @Override
    public Bon apply(Row row, String prefix) {
        Bon entity = new Bon();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTypeBon(converter.fromRow(row, prefix + "_type_bon", TypeBon.class));
        entity.setQuantiteLivre(converter.fromRow(row, prefix + "_quantite_livre", Integer.class));
        entity.setQuantiteCommande(converter.fromRow(row, prefix + "_quantite_commande", Integer.class));
        entity.setDateCreation(converter.fromRow(row, prefix + "_date_creation", Instant.class));
        entity.setEtat(converter.fromRow(row, prefix + "_etat", Boolean.class));
        return entity;
    }
}
