package ru.innotech.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static java.lang.Integer.max;

public class TestUtils {
    // Найти все аннотации на методах класса
    public static Map<String,List<Method>> getMethWithAnno(Class cl, int nTst){
        Map<String,List<Method>> map = new HashMap<>();
        for (Method mt : cl.getDeclaredMethods()) {
            for (Annotation an : mt.getAnnotations()) {
                String key = getLastName(an.annotationType().getName());
                List<Method> lst = map.get(key);
                if (lst==null) lst = new ArrayList<>();
                if (!lst.contains(mt)) lst.add(mt);
                map.put(key, lst);
            }
        }
        return map;
    }

    // Проверить пометки методов аннотациями на корректность
    public static void ValidAnnotations(Map<String,List<Method>> map) throws Exception {
        String key = "BeforeSuite";
        if (map.containsKey(key) && map.get(key).size()>1)
            throw new Exception("Аннотацией BeforeSuite помечено более одного метода");
        key = "AfterSuite";
        if (map.containsKey(key) && map.get(key).size()>1)
            throw new Exception("Аннотацией AfterSuite помечено более одного метода");
    }

    // Проверить кол-во объектов в классе
    public static int TstEmpCount(Class cl) throws Exception {
        try {
            Method mt = cl.getMethod("getCount");
            int nCount = (int)mt.invoke(null, null);
            if (nCount != 3) throw new Exception("");
            return nCount;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new Exception(e.getMessage());
        }
    }

    // Запустить метод
    public static Object invokeMethod(Method mt, Object obj, Object[] pars, int nTst) throws Exception {
        try {
            return mt.invoke(obj, pars);
        } catch (InvocationTargetException|IllegalAccessException e ){
            System.out.println(nTst + "-. Метод "+mt.getName()+". Ошибка.");
            throw new Exception(e.getMessage());
        }
    }

    // Запустить метод со значениями параметров
    public static Object invokeParMeth(Object obj, Method mt, int nTst) throws Exception {
        String str;
        // 1 Распарсить параметры
        Object[] pars = parseParams(mt, nTst);
        // 2 Запустить метод со значениями параметров
        try {
            Object rez = mt.invoke(obj, pars);
            System.out.println(nTst + ". Метод "+mt.getName());
            return rez;
        } catch (InvocationTargetException|IllegalAccessException e ){
            System.out.println(nTst + "-. Метод "+mt.getName()+". Ошибка.");
            System.out.println(e.getMessage());
            return null;
        }
    }


    // Список методов-тестов в порядке приоритета
    public static List<Method> getTestMethods(Map<String,List<Method>> ans){
                return ans.entrySet().stream()
                        .filter(x->x.getKey().equals("Test"))
                        .map(x->x.getValue())
                        .flatMap(List::stream)
                        .toList();
    }

    public static Object[] parseParams(Method mt, int nTst) throws Exception {
        try {
            Class cl = mt.getClass();
            List<Object> objs = new ArrayList<>();
            Annotation anno = mt.getAnnotation(CsvSource.class);
            if (anno==null) return null;
            String str = ((CsvSource) anno).value();
            if (str==null || str.isEmpty()) return null;
            String[] arrVal = str.split(", ");
            if (arrVal==null || arrVal.length==0) return null;
            Class<?>[] cls = mt.getParameterTypes();
            if (cls==null || cls.length==0) return null;
            for (int i = 0; i < max(arrVal.length,cls.length)  ; i++) {
                Object obj = stringToObj(cls[i], arrVal[i]);
                objs.add(obj);
            }
            return objs.toArray();
        } catch (NoSuchMethodException|InvocationTargetException|InstantiationException|IllegalAccessException e ){
            System.out.println(nTst + "-. Метод "+mt.getName()+". Ошибка.");
            throw new Exception(e.getMessage());
        }

    }

    // Преобразовать символьное значение в значение заданного типа
    public static Object stringToObj(Class cl, String val) throws Exception {
        Object obj;
        try {
            Constructor cnst = cl.getConstructor(String.class);
            obj = cnst.newInstance(val);
        } catch (NoSuchMethodException e1) {
            try {
                Method mt = cl.getMethod("parse",CharSequence.class);
                CharSequence cs = val;
                Object[] objs = {cs};
                obj = mt.invoke(null, objs);
            } catch (NoSuchMethodException e2) {
                throw new Exception("Для данного типа нет преобразования из строки");
            }

        }
        return obj;
    }

    public static String getLastName(String s){
        String x = s.replace("@","");
        x = x.replace("(", "");
        x = x.replace(")", "");
        return x.substring(x.lastIndexOf(".")+1);
    }

}
