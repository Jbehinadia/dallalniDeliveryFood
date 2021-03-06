package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Restaurant;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Restaurant}, with proper type conversions.
 */
@Service
public class RestaurantRowMapper implements BiFunction<Row, String, Restaurant> {

    private final ColumnConverter converter;

    public RestaurantRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Restaurant} stored in the database.
     */
    @Override
    public Restaurant apply(Row row, String prefix) {
        Restaurant entity = new Restaurant();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomRestaurant(converter.fromRow(row, prefix + "_nom_restaurant", String.class));
        entity.setAdresseRestaurant(converter.fromRow(row, prefix + "_adresse_restaurant", String.class));
        entity.setNumRestaurant(converter.fromRow(row, prefix + "_num_restaurant", String.class));
        entity.setDateOuverture(converter.fromRow(row, prefix + "_date_ouverture", Instant.class));
        entity.setDateFermiture(converter.fromRow(row, prefix + "_date_fermiture", Instant.class));
        entity.setResponsableRestaurantId(converter.fromRow(row, prefix + "_responsable_restaurant_id", Long.class));
        return entity;
    }
}
