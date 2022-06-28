package sn.coundoul.gestion.patrimoine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.coundoul.gestion.patrimoine.domain.CategorieMatiere;
import sn.coundoul.gestion.patrimoine.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link CategorieMatiere}, with proper type conversions.
 */
@Service
public class CategorieMatiereRowMapper implements BiFunction<Row, String, CategorieMatiere> {

    private final ColumnConverter converter;

    public CategorieMatiereRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CategorieMatiere} stored in the database.
     */
    @Override
    public CategorieMatiere apply(Row row, String prefix) {
        CategorieMatiere entity = new CategorieMatiere();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCategorie(converter.fromRow(row, prefix + "_categorie", String.class));
        return entity;
    }
}
