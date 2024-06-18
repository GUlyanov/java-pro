package ru.innotech.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static ru.innotech.annotations.Anno.*;


public class TestRunner {
    public static void runTests(Class cl) throws Exception {
        // 1.Найти метод beforeSuite
        Method beforeSuite = getMethWithAnno(cl, BeforeSuite.class, 1);

        // 2.Найти метод afterSuite
        Method afterSuite  = getMethWithAnno(cl, AfterSuite.class, 2);

        // 3.Найти метод beforeTest
        Method beforeTest  = getMethWithAnno(cl, BeforeTest.class, 3);

        // 4.Найти метод afterTest
        Method afterTest  = getMethWithAnno(cl, AfterTest.class, 4);

        // 5.Запустить метод beforeSuite
        doBeforeTest(beforeTest, beforeSuite, 5);
        invokeMethod(beforeSuite, null, null, 5);
        doAfterTest(afterTest, beforeSuite, 5);

        // 6.Получить список методов-тестов (@Test), упорядоченные по приоритету
        List<Method> mts = getTestMethods(cl);

        // 7.Запустить в порядке приоритета методы-тесты (@Test)
        for (Method mt : mts) {
            doBeforeTest(beforeTest, mt, 7);
            Object obj = Employee.getObj(0);
            System.out.println("...До запуска: " + obj.toString());
            Object rez = invokeParMeth(obj, mt, 7);
            System.out.println("...После запуска: " + obj.toString());
            doAfterTest(afterTest, mt, 7);
        }

        // 8.Запустить метод afterSuite
        doBeforeTest(beforeTest, afterSuite, 8);
        invokeMethod(afterSuite, null, null, 8);
        doAfterTest(afterTest, afterSuite, 8);
    }
}

