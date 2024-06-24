package ru.innotech.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static ru.innotech.annotations.TestUtils.*;


public class TestRunner {
    public static void runTests(Class cl) throws Exception {
        List<Method> lst;
        Annotation ann;

        // 1.Найти все методы, помеченные аннотациями за один проход по методам класса
        Map<String,List<Method>> ans = getMethWithAnno(cl, 1);

        // 2.Проверить аннотации на допустимость встречаемости
        ValidAnnotations(ans);

        // 3.Запустить метод beforeSuite
        lst = ans.get("BeforeSuite");
        if (lst!=null) {
            Method mt = lst.get(0);
            mt.invoke(null, null);
            System.out.println("1. Метод "+mt.getName());
        }

        // 4.Получить список методов-тестов (@Test), упорядоченные по приоритету
        List<Method> mts = getTestMethods(ans);

        // 5.Запустить в порядке приоритета методы-тесты (@Test)
        for (Method mt: mts) {
            // 5.1.Запустить методы BeforeTest
            lst = ans.get("BeforeTest");
            if (lst!=null) {
                for (Method mt1 : lst) {
                    Object[] args = {2, mt1.getName()};
                    mt1.invoke(null, args);
                }
            }

            // 5.2.Запустить методы-тесты
            Object obj = Employee.getObj(0);
            System.out.println("...До запуска: " + obj.toString());
            Object rez = invokeParMeth(obj, mt, 2);
            System.out.println("...После запуска: " + obj.toString());

            // 5.3.Запустить методы AfterTest
            lst = ans.get("AfterTest");
            if (lst!=null) {
                for (Method mt1 : lst) {
                    Object[] args = {2, mt1.getName()};
                    mt1.invoke(null, args);
                }
            }
        }

        // 6.Запустить метод afterSuite
        lst = ans.get("AfterSuite");
        if (lst!=null) {
            Method mt = lst.get(0);
            mt.invoke(null, null);
            System.out.println("3. Метод "+mt.getName());
        }
    }
}

