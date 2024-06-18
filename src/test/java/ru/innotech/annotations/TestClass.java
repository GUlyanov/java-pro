package ru.innotech.annotations;

public class TestClass {
    public static void main(String[] args) {
        try {
            TestRunner.runTests(Employee.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
