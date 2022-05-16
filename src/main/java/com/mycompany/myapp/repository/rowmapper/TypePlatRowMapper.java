package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.TypePlat;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TypePlat}, with proper type conversions.
 */
@Service
public class TypePlatRowMapper implements BiFunction<Row, String, TypePlat> {

    private final ColumnConverter converter;

    public TypePlatRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TypePlat} stored in the database.
     */
    @Override
    public TypePlat apply(Row row, String prefix) {
        TypePlat entity = new TypePlat();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setImagePath(converter.fromRow(row, prefix + "_image_path", String.class));
        return entity;
    }
}
