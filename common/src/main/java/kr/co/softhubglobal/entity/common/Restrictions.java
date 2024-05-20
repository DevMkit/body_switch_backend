package kr.co.softhubglobal.entity.common;

import jakarta.persistence.criteria.*;
import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Restrictions {
    private final List<Condition> conditions = new ArrayList<Condition>();
    private final List<Restrictions> children = new ArrayList<Restrictions>();
    private final Conn currCon;

    public Restrictions() {
        this.currCon = Conn.AND;
    }

    public Restrictions(Conn conn) {
        this.currCon = conn;
    }

    public void clear() {
        this.conditions.clear();
    }

    public void addChild(Restrictions child) {
        this.children.add(child);
    }

    public Restrictions eq(String name, Object value) {
        conditions.add(new Condition(ConditionType.EQUALS, name, value));
        return this;
    }

    public Restrictions eq2(String name, Object value) {
        if (value != null) {
            conditions.add(new Condition(ConditionType.EQUALS, name, value));
        }
        return this;
    }

    public Restrictions le(String name, Object value) {
        conditions.add(new Condition(ConditionType.LESS_THAN_OR_EQUAL_TO, name, value));
        return this;
    }

    public Restrictions ge(String name, Object value) {
        conditions.add(new Condition(ConditionType.GREATER_THAN_OR_EQUAL_TO, name, value));
        return this;
    }

    public Restrictions isNull(String key) {
        conditions.add(new Condition(ConditionType.IS_NULL, key));
        return this;
    }

    public Restrictions isNotNull(String key) {
        conditions.add(new Condition(ConditionType.IS_NOT_NULL, key));
        return this;
    }

    public Restrictions ne(String key, Object object) {
        conditions.add(new Condition(ConditionType.NOT_EQUAL, key, object));
        return this;
    }

    public Restrictions like(String key, String object) {
        conditions.add(new Condition(ConditionType.LIKE, key, object));
        return this;
    }

    public Restrictions between(String key, Object start, Object end) {
        conditions.add(new Condition(ConditionType.BETWEEN, key, start, end));
        return this;
    }

    public Restrictions near(Double lat, Double lng) {
        conditions.add(new Condition(ConditionType.ORDER_NEAR, "distance", lat, lng));
        return this;
    }

    public Restrictions addOrder(String name, Sort.Direction sort) {
        conditions.add(new Condition(ConditionType.ORDER, name, sort));
        return this;
    }


    public Restrictions inNear(Double lat, Double lng, Double distance) {
        conditions.add(new Condition(ConditionType.IN_NEAR, distance, lat, lng));
        return this;
    }

    @SuppressWarnings("rawtypes")
    public void in(String name, Collection value) {
        conditions.add(new Condition(ConditionType.IN, name, value));
    }

    @SuppressWarnings("rawtypes")
    public void strIn(String name, Collection value) {
        conditions.add(new Condition(ConditionType.STR_IN, name, value));
    }

    @SuppressWarnings("rawtypes")
    public void notIn(String name, Collection value) {
        conditions.add(new Condition(ConditionType.NOT_IN, name, value));
    }

    public void isNullOrLe(String key, Object value) {
        conditions.add(new Condition(ConditionType.LESS_THAN_OR_IS_NULL, key, value));
    }

    public void isNullOrGe(String key, Object value) {
        conditions.add(new Condition(ConditionType.GREATER_THAN_OR_IS_NULL, key, value));
    }

    public void eqProperty(String key, String key2) {
        conditions.add(new Condition(ConditionType.EQUAL_PROPERTY, key, key2));
    }

    public void neProperty(String key, String key2) {
        conditions.add(new Condition(ConditionType.NOT_EQUAL_PROPERTY, key, key2));
    }

    public <T> Specification<T> output() {

        Specification<T> spec = new Specification<T>() {
            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> items = new ArrayList<Predicate>();
                List<Order> orders = new ArrayList<Order>();
                for (Condition condition : conditions) {

                    String key = condition.getName().toString();
                    Object object = condition.getValue1();
                    Object object2 = condition.getValue2();

                    switch (condition.type) {
                        case EQUALS -> {
                            items.add(cb.equal(getPath(root, key), object));
                        }
                        case EQUAL_PROPERTY -> {
                            items.add(cb.equal(getPath(root, key), getPath(root, object.toString())));
                        }
                        case NOT_EQUAL_PROPERTY -> {
                            items.add(cb.notEqual(getPath(root, key), getPath(root, object.toString())));
                        }
                        case NOT_EQUAL -> {
                            items.add(cb.notEqual(getPath(root, key), object));
                        }
                        case LIKE -> {
                            items.add(cb.like(getPath(root, key), (String) object));
                        }
                        case IS_NULL -> {
                            items.add(cb.isNull(getPath(root, key)));
                        }
                        case IS_NOT_NULL -> {
                            items.add(cb.isNotNull(getPath(root, key)));
                        }
                        case STR_IN -> {
                            Predicate p = null;
                            Path<String> field = getPath(root, key);

                            for (Object o : (Collection) object) {
                                Predicate _p = cb.like(field.as(String.class), "%" + o + "%");
                                p = (p == null) ? _p : cb.or(p, _p);
                            }

                            items.add(p);
                        }
                        case IN -> {
                            final Path<Object> group = getPath(root, key);
                            items.add(group.in((Collection) object));
                        }
                        case NOT_IN -> {
                            final Path<Object> group = getPath(root, key);
                            items.add(group.in((Collection) object).not());
                        }
                        case LESS_THAN_OR_EQUAL_TO -> {
                            if (object instanceof Date) {
                                items.add(
                                        cb.lessThanOrEqualTo(Restrictions.<Date>getPath(root, key), (Date) object));
                            } else if (object instanceof LocalDateTime) {
                                items.add(cb.lessThanOrEqualTo(Restrictions.<LocalDateTime>getPath(root, key),
                                        (LocalDateTime) object));
                            } else if (object instanceof LocalDate) {
                                items.add(cb.lessThanOrEqualTo(Restrictions.<LocalDate>getPath(root, key),
                                        (LocalDate) object));
                            } else if (object instanceof LocalTime) {
                                items.add(cb.lessThanOrEqualTo(Restrictions.<LocalTime>getPath(root, key),
                                        (LocalTime) object));
                            } else if (object instanceof String) {
                                items.add(cb.lessThanOrEqualTo(Restrictions.<String>getPath(root, key), (String) object));
                            } else {
                                items.add(cb.le(Restrictions.<Number>getPath(root, key), (Number) object));
                            }
                        }
                        case GREATER_THAN_OR_EQUAL_TO -> {
                            if (object instanceof Date) {
                                items.add(
                                        cb.greaterThanOrEqualTo(Restrictions.<Date>getPath(root, key), (Date) object));
                            } else if (object instanceof LocalDateTime) {
                                items.add(cb.greaterThanOrEqualTo(Restrictions.<LocalDateTime>getPath(root, key),
                                        (LocalDateTime) object));
                            } else if (object instanceof LocalDate) {
                                items.add(cb.greaterThanOrEqualTo(Restrictions.<LocalDate>getPath(root, key),
                                        (LocalDate) object));
                            } else if (object instanceof LocalTime) {
                                items.add(cb.greaterThanOrEqualTo(Restrictions.<LocalTime>getPath(root, key),
                                        (LocalTime) object));
                            } else if (object instanceof String) {
                                items.add(cb.greaterThanOrEqualTo(Restrictions.<String>getPath(root, key), (String) object));
                            } else {
                                items.add(cb.ge(Restrictions.<Number>getPath(root, key), (Number) object));
                            }
                        }
                        case LESS_THAN_OR_IS_NULL -> {
                            Path path = getPath(root, key);
                            if (object instanceof Date) {
                                items.add(cb.or(cb.isNull(path), cb.lessThanOrEqualTo(path, (Date) object)));
                            } else if (object instanceof LocalDateTime) {
                                items.add(
                                        cb.or(cb.isNull(path), cb.lessThanOrEqualTo(path, (LocalDateTime) object)));
                            } else if (object instanceof LocalDate) {
                                items.add(cb.or(cb.isNull(path), cb.lessThanOrEqualTo(path, (LocalDate) object)));
                            } else if (object instanceof LocalTime) {
                                items.add(cb.or(cb.isNull(path), cb.lessThanOrEqualTo(path, (LocalTime) object)));
                            } else {
                                items.add(cb.or(cb.isNull(path), cb.le(path, (Number) object)));
                            }
                        }
                        case GREATER_THAN_OR_IS_NULL -> {
                            Path path = getPath(root, key);
                            if (object instanceof Date) {
                                items.add(cb.or(cb.isNull(path), cb.greaterThanOrEqualTo(path, (Date) object)));
                            } else if (object instanceof LocalDateTime) {
                                items.add(
                                        cb.or(cb.isNull(path), cb.greaterThanOrEqualTo(path, (LocalDateTime) object)));
                            } else if (object instanceof LocalDate) {
                                items
                                        .add(cb.or(cb.isNull(path), cb.greaterThanOrEqualTo(path, (LocalDate) object)));
                            } else if (object instanceof LocalTime) {
                                items
                                        .add(cb.or(cb.isNull(path), cb.greaterThanOrEqualTo(path, (LocalTime) object)));
                            } else {
                                items.add(cb.or(cb.isNull(path), cb.ge(path, (Number) object)));
                            }
                        }
                        case BETWEEN -> {
                            if (object instanceof Date) {
                                items.add(cb.between(Restrictions.<Date>getPath(root, key), (Date) object,
                                        (Date) object2));
                            } else if (LocalDateTime.class.isAssignableFrom(object.getClass())) {
                                items.add(cb.between(Restrictions.getPath(root, key), (LocalDateTime) object,
                                        (LocalDateTime) object2));
                            } else if (LocalDate.class.isAssignableFrom(object.getClass())) {
                                items.add(cb.between(Restrictions.getPath(root, key), (LocalDate) object,
                                        (LocalDate) object2));
                            } else if (LocalTime.class.isAssignableFrom(object.getClass())) {
                                items.add(cb.between(Restrictions.getPath(root, key), (LocalTime) object,
                                        (LocalTime) object2));
                            } else {
                                items.add(
                                        cb.between(Restrictions.getPath(root, key), (Double) object, (Double) object2));
                            }
                        }
                        case ORDER -> {
                            if (object == Sort.Direction.DESC) {
                                orders.add(cb.desc(getPath(root, key)));
                            } else {
                                orders.add(cb.asc(getPath(root, key)));
                            }
                        }
                        case ORDER_NEAR -> {
                            orders.add(cb.asc(cb.function(key, Double.class, root.get("lat"), root.get("lng"),
                                    cb.literal((Double) object), cb.literal((Double) object2))));
                        }
                        case IN_NEAR -> {
                            Expression fn = cb
                                    .function("distance", Double.class, root.get("lat"), root.get("lng"),
                                            cb.literal((Double) object), cb.literal((Double) object2));
                            items.add(cb.le(fn, (Double) condition.getName()));
                        }
                        default -> {
                        }
                    }
                }

                if (!orders.isEmpty()) {
                    query.orderBy(orders);
                }

                if (items.size() > 1) {
                    Predicate[] ps = items.toArray(new Predicate[]{});
                    return (currCon == Conn.AND) ? cb.and(ps) : cb.or(ps);
                } else if (items.size() == 1) {
                    return items.get(0);
                }

                return null;
            }


        };

        if (!children.isEmpty()) {
            for (Restrictions child : children) {
                spec = (currCon == Conn.AND) ? spec.and(child.output()) : spec.or(child.output());
            }
        }

        return spec;

    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <Y> Path<Y> getPath(Root root, String key) {
        Path<Y> x = null;
        if (key.contains(".")) {
            for (String k : key.split("\\.")) {
                x = (x == null ? root.get(k) : x.get(k));
            }
        } else {
            x = root.<Y>get(key);
        }
        return x;
    }

    @Data
    private static class Condition {

        private ConditionType type;
        private Object name;
        private Object value1;
        private Object value2;

        public Condition(ConditionType type, Object name) {
            this(type, name, null);
        }

        public Condition(ConditionType type, Object name, Object value1) {
            this.type = type;
            this.name = name;
            this.value1 = value1;
        }

        public Condition(ConditionType type, Object name, Object value1, Object value2) {
            this.type = type;
            this.name = name;
            this.value1 = value1;
            this.value2 = value2;
        }

    }

    private enum ConditionType {
        EQUALS,
        IN,
        NOT_IN,
        LESS_THAN_OR_EQUAL_TO,
        GREATER_THAN_OR_EQUAL_TO,
        IS_NULL,
        IS_NOT_NULL,
        NOT_EQUAL,
        LIKE,
        BETWEEN,
        LESS_THAN_OR_IS_NULL,
        GREATER_THAN_OR_IS_NULL,
        EQUAL_PROPERTY,
        NOT_EQUAL_PROPERTY,
        ORDER,
        ORDER_NEAR,
        IN_NEAR,
        STR_IN,
    }

    public enum Conn {
        AND,
        OR
    }
}
