package awt.utils;

import awt.model.domain.*;
import awt.proto.enums.ElementType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenbiao
 * @date 2020-12-24 19:22
 */
public class LoadClassUtils {
    private static Map<String, Class> classMap = new HashMap<String, Class>();

    /**
     * 发射机制加载
     * 加载元素类 通过类名 参数类型
     *
     * @param elementType
     * @param constructorParameterTypes
     * @param params
     * @return
     */
    public static Element loadElementClass(ElementType elementType, Class<?>[] constructorParameterTypes, Object... params) throws Exception {
        if (constructorParameterTypes.length != params.length) {
            throw new Exception("类型参数与构造参数数量不一致");
        }
        try {
            Class<?> aClass;
            if (elementType != null) {
                String className=null;
                switch (elementType) {
                    case TANK:
                        className= Tank.class.getName();
                        break;
                    case WALL:
                        className= Wall.class.getName();
                        break;
                    case RIVER:
                        className= River.class.getName();
                        break;
                    case BULLET:
                        className= Bullet.class.getName();
                        break;
                    case GRASS_LAND:
                        className= GrassLand.class.getName();
                        break;
                }
                if (classMap.containsKey(className)) {
                    aClass = classMap.get(className);
                } else {
                    aClass = Class.forName(className);
                    classMap.put(className, aClass);
                }
            } else {
                aClass = Element.class;
            }
            Constructor<?>[] constructors = aClass.getConstructors();
            //是否包含带有element 的构造方法
            boolean isContainsElementsConstructor = false;
            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length == constructorParameterTypes.length) {
                    for (int i = 0; i < parameterTypes.length; i++) {
                        String parameterName = parameterTypes[i].getName();
                        String constructorParamName = constructorParameterTypes[i].getName();
                        if (parameterName.equals(constructorParamName)) {
                            isContainsElementsConstructor = true;
                            break;
                        }
                    }
                }

            }
            if (isContainsElementsConstructor) {
                Constructor<?> constructor = aClass.getConstructor(constructorParameterTypes);
                Element e = (Element) constructor.newInstance(params);
                return e;
            } else {
                return null;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        Element element = loadElementClass(ElementType.TANK
                , new Class<?>[]{int.class, int.class, int.class, int.class}
                , 1, 1, 1, 1);
        System.out.println(element);
    }
}
