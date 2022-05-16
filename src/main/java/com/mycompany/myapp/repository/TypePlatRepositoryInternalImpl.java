package com.mycompany.myapp.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.mycompany.myapp.domain.TypePlat;
import com.mycompany.myapp.repository.rowmapper.TypePlatRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the TypePlat entity.
 */
@SuppressWarnings("unused")
class TypePlatRepositoryInternalImpl extends SimpleR2dbcRepository<TypePlat, Long> implements TypePlatRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TypePlatRowMapper typeplatMapper;

    private static final Table entityTable = Table.aliased("type_plat", EntityManager.ENTITY_ALIAS);

    public TypePlatRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TypePlatRowMapper typeplatMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TypePlat.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.typeplatMapper = typeplatMapper;
    }

    @Override
    public Flux<TypePlat> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<TypePlat> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TypePlatSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TypePlat.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TypePlat> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TypePlat> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private TypePlat process(Row row, RowMetadata metadata) {
        TypePlat entity = typeplatMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends TypePlat> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
