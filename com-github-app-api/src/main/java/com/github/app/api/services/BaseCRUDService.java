package com.github.app.api.services;

import com.github.app.api.utils.AppContext;
import com.github.app.api.utils.Condition;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Component
public class BaseCRUDService {

    public Object selectByPrimaryKey(Object key, Class modelClass) throws Exception {
        Class mapperClass = getMapperClass(modelClass);
        Object mapper = AppContext.getContext().getBean(mapperClass);

        Method method = mapperClass.getMethod("selectByPrimaryKey", key.getClass());

        return method.invoke(mapper, key);
    }

    public int insert(Object entity) throws Exception {
        Class mapperClass = getMapperClass(entity.getClass());
        Object mapper = AppContext.getContext().getBean(mapperClass);
        Method method = mapperClass.getMethod("insert", entity.getClass());
        return (int) method.invoke(mapper, entity);
    }

    public int insertSelective(Object entity) throws Exception {
        Class mapperClass = getMapperClass(entity.getClass());
        Object mapper = AppContext.getContext().getBean(mapperClass);
        Method method = mapperClass.getMethod("insertSelective", entity.getClass());
        return (int) method.invoke(mapper, entity);
    }

    public int updateByPrimaryKeySelective(Object entity) throws Exception {
        Class mapperClass = getMapperClass(entity.getClass());
        Object mapper = AppContext.getContext().getBean(mapperClass);
        Method method = mapperClass.getMethod("updateByPrimaryKeySelective", entity.getClass());
        return (int) method.invoke(mapper, entity);
    }

    public int updateByPrimaryKey(Object entity) throws Exception {
        Class mapperClass = getMapperClass(entity.getClass());
        Object mapper = AppContext.getContext().getBean(mapperClass);
        Method method = mapperClass.getMethod("updateByPrimaryKey", entity.getClass());
        return (int) method.invoke(mapper, entity);
    }

    public int deleteByPrimaryKey(Object key, Class modelClass) throws Exception {
        Class mapperClass = getMapperClass(modelClass);
        Object mapper = AppContext.getContext().getBean(mapperClass);

        Method method = mapperClass.getMethod("deleteByPrimaryKey", key.getClass());
        return (int) method.invoke(mapper, key);
    }

    public int batchInsert(Object... entities) throws Exception {
        Class mapperClass = getMapperClass(entities[0].getClass());
        Object mapper = AppContext.getContext().getBean(mapperClass);

        Method method = mapperClass.getMethod("batchInsert", entities.getClass());
        return (int) method.invoke(mapper, Arrays.asList(entities));
    }

    public List query(Integer offset, Integer rows, Class modelClass, Condition... conditions) throws Exception {
        return query(offset, rows, null, modelClass, conditions);
    }

    public List query(Integer offset, Integer rows, String orderByClause, Class modelClass, Condition... conditions) throws Exception {
        String exampleClassName = "com.github.app.api.dao.domain." + modelClass.getSimpleName() + "Example";
        Class exampleClass = Class.forName(exampleClassName);
        Object example = exampleClass.getConstructor().newInstance();

        Class mapperClass = getMapperClass(modelClass);
        Object mapper = AppContext.getContext().getBean(mapperClass);

        if (offset != null) {
            Method method = exampleClass.getMethod("setOffset", Integer.class);
            method.invoke(example, offset);
        }

        if (rows != null) {
            Method method = exampleClass.getMethod("setRows", Integer.class);
            method.invoke(example, rows);
        }

        if(orderByClause != null) {
            Method method = exampleClass.getMethod("setOrderByClause", String.class);
            method.invoke(example, orderByClause);
        }

        if (conditions != null) {
            Method method = exampleClass.getMethod("createCriteria");
            for (Condition condition : conditions) {
                Object criteria = method.invoke(example);
                String methodName = null;
                if (condition.isLikeValue()) {
                    methodName = "Like";
                } else if (condition.isNotLikeValue()) {
                    methodName = "NotLike";
                } else if (condition.isEqualValue()) {
                    methodName = "EqualTo";
                } else if (condition.isNotEqualValue()) {
                    methodName = "NotEqualTo";
                } else if (condition.isBetweenValue()) {
                    methodName = "Between";
                } else if (condition.isGreeterValue()) {
                    methodName = "GreaterThan";
                } else if (condition.isGreeterEqualValue()) {
                    methodName = "GreaterThanOrEqualTo";
                } else if (condition.isLessValue()) {
                    methodName = "LessThan";
                } else if (condition.isLessEqualValue()) {
                    methodName = "LessThanOrEqualTo";
                } else if (condition.isNullValue()) {
                    methodName = "IsNull";
                } else if (condition.isNotNullValue()) {
                    methodName = "IsNotNull";
                } else if (condition.isInValue()) {
                    methodName = "In";
                } else if (condition.isNotInValue()) {
                    methodName = "NotIn";
                }

                Method op = criteria.getClass().getMethod("and"
                        + condition.getProperty().substring(0, 1).toUpperCase()
                        + condition.getProperty().substring(1)
                        + methodName, condition.getValue().getClass());

                if(condition.getSecondValue() == null)
                    op.invoke(criteria, condition.getValue());
                else
                    op.invoke(criteria, condition.getValue(), condition.getSecondValue());
            }
        }

        Method method = mapperClass.getMethod("selectByExample", exampleClass);
        return (List) method.invoke(mapper, example);
    }

    public List query(Class modelClass, Condition... conditions) throws Exception {
        return query(null, null, null, modelClass, conditions);
    }

    public List query(String orderByClause, Class modelClass, Condition... conditions) throws Exception {
        return query(null, null, orderByClause, modelClass, conditions);
    }

    private Class getMapperClass(Class modelClass) throws ClassNotFoundException {
        String modelName = modelClass.getSimpleName();
        String mapperClassName = "com.github.app.api.dao.mapper."
                + modelName.substring(0, 1).toUpperCase()
                + modelName.substring(1)
                + "Mapper";
        Class mapperClass = Class.forName(mapperClassName);
        return mapperClass;
    }
}
