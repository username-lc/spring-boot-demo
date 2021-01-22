package cn.lc.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BeanUtil {

    private BeanUtil(){}

    public static Map<String, Object> convertToMap(Object obj) {
        return convertToMap(obj, false);
    }

    public static Map<String, Object> convertToMap(Object obj, boolean convertNull) {
        if(obj == null){
            return null;
        }

        Map<String, Object> map = new HashMap<>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                Object o = field.get(obj);
                if (Objects.nonNull(o)){
                    if (o instanceof Collection) {
                        if (((Collection)o).size()==0){
                            continue;
                        }
                    } else if (o instanceof Map) {
                        if (((Map) o).size() == 0) {
                            continue;
                        }
                    } else if (o instanceof Enum) {
                        o = ((Enum) o).name();
                    }
                    map.put(field.getName(), o);
                }
                else if(convertNull) {
                   map.put(field.getName(), null);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }


    public static Object mapToObject(Map<Object, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                field.set(obj, map.get(field.getName()));
            }
        }
        return obj;
    }
}
