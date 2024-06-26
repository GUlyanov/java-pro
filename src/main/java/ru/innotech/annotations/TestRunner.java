package ru.innotech.annotations;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static ru.innotech.annotations.TestUtils.*;


public class TestRunner {
    public static void runTests(Class cl) throws Exception {

        // 1.Найти все методы, помеченные аннотациями за один проход по методам класса
        Map<String,List<Method>> ans = getMethodsByAnnotationMap(cl);

        // 2.Проверить аннотации на допустимость встречаемости
        validateAnnotations(ans);

        // 3.Запустить метод beforeSuite
        List<Method> lst = ans.get("BeforeSuite");
        if (lst!=null) {
            Method mt = lst.get(0);
            mt.invoke(null, null);
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
            Object obj = new Employee("Иванов Степан Игоревич", 47, null, BigDecimal.valueOf(120000));
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
        }
    }
}

