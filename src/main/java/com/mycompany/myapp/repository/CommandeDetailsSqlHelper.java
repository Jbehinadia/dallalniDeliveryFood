package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CommandeDetailsSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("prix", table, columnPrefix + "_prix"));
        columns.add(Column.aliased("etat", table, columnPrefix + "_etat"));
        columns.add(Column.aliased("qte", table, columnPrefix + "_qte"));

        columns.add(Column.aliased("commande_id", table, columnPrefix + "_commande_id"));
        columns.add(Column.aliased("plat_id", table, columnPrefix + "_plat_id"));
        return columns;
    }
}
