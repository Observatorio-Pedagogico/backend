package com.obervatorio_pedagogico.backend.infrastructure.utils.buscaConstrutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.util.ReflectionUtils;

@Component
@RequestScope
public class PredicatesGenerator {
    private static final String PROPERTY_SEPARATOR = ".";

    private List<BooleanExpression> predicates = new ArrayList<>();

    public PredicatesGenerator add(Object object) {
        SearchEntity entity = object.getClass().getAnnotation(SearchEntity.class);
        PathBuilder<?> entityPath = new PathBuilder<>(entity.value(),
                StringUtils.uncapitalize(entity.value().getSimpleName()));
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value == null) {
                    continue;
                }
                add(field, field.get(object), entityPath, entity.value());
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(field.toString());
            }
        }
        return this;
    }

    private void add(Field field, Object value, PathBuilder<?> entityPath, Class<?> entityType) {
        if (field.isAnnotationPresent(SearchParam.class)) {
            BooleanExpression predicate = null;
            SearchParam param = field.getAnnotation(SearchParam.class);
            String property = param.property() == null ? field.getName() : param.property();
            Operation operation = param.operation();
            Pair<String, PathBuilder<?>> pair = listHandle(Pair.of(property, entityPath), entityType);
            property = pair.getFirst();
            entityPath = pair.getSecond();

            if (value instanceof BigDecimal) {
                predicate = getPredicate(property, operation, entityPath, new BigDecimal(value.toString()));
            } else if (value instanceof Number) {
                predicate = getPredicate(property, operation, entityPath, Integer.parseInt(value.toString()));
            } else if (value instanceof LocalDateTime) {
                predicate = getPredicate(property, operation, entityPath, LocalDateTime.parse(value.toString()));
            } else if (value instanceof Enum<?>) {
                predicate = getPredicate(property, entityPath, (Enum<?>) value);
            } else if (value instanceof Boolean) {
                predicate = getPredicate(property, entityPath, (Boolean) value);
            } else if (value instanceof List) {
                predicate = getPredicate(property, entityPath, (List<?>) value);
            } else {
                predicate = getPredicate(property, operation, entityPath, value.toString());
            }

            predicates.add(predicate);
        }
    }

    public BooleanExpression build() {
        BooleanExpression result = predicates.isEmpty() ? Expressions.asBoolean(true).isTrue() : predicates.get(0);

        for (int i = 1; i < predicates.size(); i++)
            result = result.and(predicates.get(i));

        predicates.clear();
        return result;
    }

    private Pair<String, PathBuilder<?>> listHandle(Pair<String, PathBuilder<?>> pair, Class<?> type) {
        String remainingProperty = pair.getFirst();
        PathBuilder<?> path = pair.getSecond();
        String actualProperty = StringUtils.substringBefore(remainingProperty, PROPERTY_SEPARATOR);
        Field field = ReflectionUtils.getFieldOrNull(type, actualProperty);

        if (field != null )
            remainingProperty = StringUtils.substringAfter(remainingProperty, field.getName() + PROPERTY_SEPARATOR);

        if (field.getName().equals(pair.getFirst()))
            return pair;

        if (Collection.class.isAssignableFrom(field.getType())) {
            type = FieldUtils.getGenericType(field);
            pair = Pair.of(remainingProperty, path.getList(field.getName(), type).any());
            return listHandle(pair, type);
        }

        return listHandle(Pair.of(remainingProperty, path.get(field.getName(), field.getType())), field.getType());
    }

    private BooleanExpression getPredicate(String property, PathBuilder<?> pathBuilder, List<?> value) {
        return !value.isEmpty() ? pathBuilder.get(property).in(value) : Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression getPredicate(String property, Operation operation, PathBuilder<?> pathBuilder,
            Integer value) {
        NumberPath<Integer> path = pathBuilder.getNumber(property, Integer.class);
        switch (operation) {
        case EQUALS:
            return path.eq(value);
        case MAJOR_EQUALS:
            return path.goe(value);
        case MINOR_EQUALS:
            return path.loe(value);
        default:
            return path.eq(value);
        }
    }

    private BooleanExpression getPredicate(String property, Operation operation, PathBuilder<?> pathBuilder,
                                           BigDecimal value) {
        NumberPath<BigDecimal> path = pathBuilder.getNumber(property, BigDecimal.class);
        switch (operation) {
            case EQUALS:
                return path.eq(value);
            case MAJOR_EQUALS:
                return path.goe(value);
            case MINOR_EQUALS:
                return path.loe(value);
            default:
                return path.eq(value);
        }
    }

    private BooleanExpression getPredicate(String property, Operation operation, PathBuilder<?> pathBuilder,
            String value) {
        StringPath path = pathBuilder.getString(property);
        switch (operation) {
        case EQUALS:
            return path.eq(value);
        case ILIKE:
            return path.containsIgnoreCase(value);
        default:
            return path.eq(value);
        }
    }

    private BooleanExpression getPredicate(String property, PathBuilder<?> pathBuilder, Boolean value) {
        return pathBuilder.getBoolean(property).eq(value);
    }

    private BooleanExpression getPredicate(String property, Operation operation, PathBuilder<?> pathBuilder,
            LocalDateTime value) {
        DatePath<LocalDateTime> path = pathBuilder.getDate(property, LocalDateTime.class);
        switch (operation) {
        case EQUALS:
            return path.eq(value);
        case MAJOR_EQUALS:
            return path.goe(value);
        case MINOR_EQUALS:
            return path.loe(value);
        default:
            return path.eq(value);
        }
    }

    @SuppressWarnings("unchecked")
    private BooleanExpression getPredicate(String property, PathBuilder<?> pathBuilder, Enum<?> value) {
        return pathBuilder.getEnum(property, Enum.class).eq(value);
    }

    private static class FieldUtils {

        public static Class<?> getGenericType(Field field) {
            ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
            return (Class<?>) fieldType.getActualTypeArguments()[0];
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface SearchEntity {

        public Class<?> value();

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SearchParam {

        public String property() default "";

        public Operation operation() default Operation.EQUALS;

        public String group() default "default";

    }

    public enum Operation {
        EQUALS, MINOR_EQUALS, MAJOR_EQUALS, ILIKE;
    
    }
}