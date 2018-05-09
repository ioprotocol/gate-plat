package com.github.app.api.services;

import com.github.app.api.config.SpringApplication;
import com.github.app.utils.ServerEnvConstant;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringApplication.class)
public class BaseServiceTest {

    @ClassRule
    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @BeforeClass
    public static void setup() {
        if(System.getProperty("os.name").toLowerCase().startsWith("win")) {
            environmentVariables.set(ServerEnvConstant.APP_HOME, "D:\\project_my\\com-github-app\\application-home");
        } else {
            environmentVariables.set(ServerEnvConstant.APP_HOME, "/home/xsy/github/com-github-app/application-home");
        }
    }

    protected boolean isEqual(Object oldEntity, Object newEntity) {
        Class<?> cls = oldEntity.getClass();

        if (oldEntity == null && newEntity == null) {
            return true;
        }
        if (oldEntity == null || newEntity == null) {
            Assert.assertTrue(this.buildErrorMsg(cls.getName()), false);
        }

        Field[] fields = cls.getDeclaredFields();
        int size = fields.length;

        for (int index = 0; index < size; ++index) {
            Field field = fields[index];
            field.setAccessible(true);

            Object oldValue;
            Object newValue;
            try {
                oldValue = field.get(oldEntity);
                newValue = field.get(newEntity);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            if (oldValue == null && newValue == null) {
                return true;
            }
            if (oldValue == null || newValue == null) {
                Assert.assertTrue(this.buildErrorMsg(cls.getName() + ":" + field.getName(), oldValue, newValue), false);
                return false;
            }

            if (oldValue instanceof Map) {
                Map oldMap = (Map) oldValue;
                Map newMap = (Map) newValue;
                Iterator var22 = oldMap.keySet().iterator();

                while (var22.hasNext()) {
                    Object key = var22.next();
                    Object value = oldMap.get(key);
                    if (this.isPrimitiveType(oldMap.get(key))) {
                        Assert.assertTrue(this.buildErrorMsg(cls.getName() + ":" + field.getName(), oldValue, newValue), value.equals(newMap.get(key)));
                    } else {
                        Assert.assertTrue(this.buildErrorMsg(cls.getName() + ":" + field.getName(), oldValue, newValue), this.isEqual(value, newMap.get(key)));
                    }
                }
            } else if (oldValue instanceof List) {
                List oldList = (List) oldValue;
                List newList = (List) newValue;
                Assert.assertTrue(this.buildErrorMsg(cls.getName() + ":" + field.getName(), oldValue, newValue), oldList.size() == newList.size());

                for (int i = 0; i < oldList.size(); ++i) {
                    Assert.assertTrue(this.buildErrorMsg(cls.getName() + ":" + field.getName(), oldList.get(i), newList.get(i)), this.isEqual(oldList.get(i), newList.get(i)));
                }
            } else {
                SimpleDateFormat sdf;
                if (oldValue instanceof Date) {
                    sdf = new SimpleDateFormat("yyyy-mm-dd");
                    Assert.assertTrue(this.buildErrorMsg(cls.getName() + ":" + field.getName(), oldValue, newValue), sdf.format(oldValue).equals(sdf.format(newValue)));
                } else if (oldValue instanceof Time) {
                    sdf = new SimpleDateFormat("HH:mm:ss");
                    Assert.assertTrue(this.buildErrorMsg(cls.getName() + ":" + field.getName(), oldValue, newValue), sdf.format(oldValue).equals(sdf.format(newValue)));
                } else if (oldValue instanceof java.util.Date) {
                    sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                    Assert.assertTrue(this.buildErrorMsg(cls.getName() + ":" + field.getName(), oldValue, newValue), sdf.format(oldValue).equals(sdf.format(newValue)));
                } else {
                    if(isPrimitiveType(oldValue)) {
                        Assert.assertTrue(this.buildErrorMsg(cls.getName() + ":" + field.getName(), oldValue, newValue), oldValue.equals(newValue));
                    } else {
                        Assert.assertTrue(this.buildErrorMsg(cls.getName() + ":" + field.getName(), oldValue, newValue), isEqual(oldValue, newValue));
                    }
                }
            }
        }

        return true;
    }

    private String buildErrorMsg(Object... args) {
        String errorMsg = "fieldName:%s\noldValue:%s\nnewValue:%s\n";
        return String.format(errorMsg, args);
    }

    private boolean isPrimitiveType(Object value) {
        return value instanceof String || value instanceof Integer || value instanceof Short || value instanceof Long || value instanceof java.util.Date;
    }
}
