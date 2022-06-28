package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.DetailSortie;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link DetailSortie}, with proper type conversions.
 */
@Service
public class DetailSortieRowMapper implements BiFunction<Row, String, DetailSortie> {

    private final ColumnConverter converter;

    public DetailSortieRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DetailSortie} stored in the database.
     */
    @Override
    public DetailSortie apply(Row row, String prefix) {
        DetailSortie entity = new DetailSortie();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPieceJointe(converter.fromRow(row, prefix + "_piece_jointe", String.class));
        entity.setIdPers(converter.fromRow(row, prefix + "_id_pers", Integer.class));
        entity.setTypeBonId(converter.fromRow(row, prefix + "_type_bon_id", Long.class));
        return entity;
    }
}
