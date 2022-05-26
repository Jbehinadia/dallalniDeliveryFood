package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RestaurantSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_restaurant", table, columnPrefix + "_nom_restaurant"));
        columns.add(Column.aliased("adresse_restaurant", table, columnPrefix + "_adresse_restaurant"));
        columns.add(Column.aliased("num_restaurant", table, columnPrefix + "_num_restaurant"));
        columns.add(Column.aliased("date_ouverture", table, columnPrefix + "_date_ouverture"));
        columns.add(Column.aliased("date_fermiture", table, columnPrefix + "_date_fermiture"));

        columns.add(Column.aliased("responsable_restaurant_id", table, columnPrefix + "_responsable_restaurant_id"));
        return columns;
    }
}
