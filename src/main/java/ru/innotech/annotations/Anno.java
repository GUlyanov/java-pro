package ru.innotech.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.lang.Integer.max;

public class Anno {
    // Получить метод с заданной аннотацией, Exception, если их более одного
    public static Method getMethWithAnno(Class cl, Class annoCl, int nTst) throws Exception {
        Method rez = null;
        Method[] methods = cl.getMethods();
        for (Method mt : methods) {
            if (mt.isAnnotationPresent(annoCl)) {
                if (rez != null) throw
                        new Exception(nTst + "-. У класса "+cl.getName()+" более одной аннотации "+ annoCl.getName());
                else rez = mt;
            }
        }
        if (rez!=null)
            System.out.println(nTst + "+. В классе найден единственный метод "+rez.getName()+" с аннотацией "+getLastName(annoCl.getName()));
        else
            System.out.println(nTst + "+. В классе нет метода с аннотацией "+getLastName(annoCl.getName()));
        return rez;
    }

    //
    public static String getLastName(String s){
        return s.substring(s.lastIndexOf(".")+1);
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
            Object rez = mt.invoke(obj, pars);
            return rez;
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
            return rez;
        } catch (InvocationTargetException|IllegalAccessException e ){
            System.out.println(nTst + "-. Метод "+mt.getName()+". Ошибка.");
            System.out.println(e.getMessage());
            return null;
        }
    }


    // Список методов-тестов в порядке приоритета
    public static List<Method> getTestMethods(Class cl){
        List<Method> methods =
                Arrays.stream(cl.getMethods())
                        .filter((r)->r.isAnnotationPresent(Test.class))
                        .sorted(Comparator.comparingInt(s->s.getAnnotation(Test.class).priority()))
                        .toList();
        return methods;
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


    public static void doBeforeTest(Method annoMt, Method mt, int nTst) throws Exception {
        Object[] pars = {nTst, mt.getName()};
        invokeMethod(annoMt, null, pars, nTst);
    }

    public static void doAfterTest(Method annoMt, Method mt, int nTst) throws Exception {
        Object[] pars = {nTst, mt.getName()};
        invokeMethod(annoMt, null, pars, nTst);
    }
}
