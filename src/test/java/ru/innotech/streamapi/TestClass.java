package ru.innotech.streamapi;

import java.util.*;
import java.util.stream.Collectors;

public class TestClass {
    public static void main(String[] args) {
        int nTest = 9;
        List<Integer> lst11 = List.of(1,7,7,3,5,2,10,2,7,15,14,20);
        // Тест1
        switch(nTest){
            case 0 :
                System.out.println(lst11.toString());
                List<Integer> lst02 = test0(lst11);
                System.out.println(lst02.toString()); break;
            case 1 :
                System.out.println(lst11.toString());
                List<Integer> lst12 = test1(lst11);
                System.out.println(lst12.toString()); break;
            case 2 :
                System.out.println(lst11.toString());
                int n = test2(lst11);
                System.out.println(n); break;
            case 3 :
                System.out.println(lst11.toString());
                n = test3(lst11);
                System.out.println(n);  break;
            case 4 :
                List<Employee> lst41 = createEmpList();
                List<String> lst42 = test4(lst41);
                lst42.stream().forEach(System.out::println); break;
            case 5 :
                List<Employee> lst51 = createEmpList();
                double rez5 = test5(lst51);
                System.out.println(rez5); break;
            case 6 :
                List<String> lst61 =
                    List.of("Корова", "Диверсификация", "Революция", "Бык", "Протон", "Аполлон", "Тротилл");
                String rez6 = test6(lst61);
                System.out.println(rez6); break;
            case 7 :
                String str71 = "aa aa aaa aa aaa aa aaaa aa aa aaa aaaa";
                Map<String,Long> lst72 = test7(str71);
                lst72.forEach((k,v) -> System.out.println(k + " - " + v)); break;
            case 8 :
                List<String> lst81 =
                        List.of("Корова", "Сон", "Революция", "Борона", "Бык", "Тротилл", "Стручок");
                List<String> lst82 = test8(lst81);
                lst82.stream().forEach(System.out::println); break;
            case 9 :
                List<String> lst91 = createStrList();
                String str92 = test9(lst91);
                System.out.println(str92); break;
        }
    }
    // Тест0. Сортировка элементов списка
    public static List<Integer> test0(List<Integer> lst ){
        return lst.stream()
                .sorted(Integer::compare)
                .toList();
    }

    // Тест1. Удаление из листа всех дубликатов
    public static <T> List<T> test1(List<T> lst ){
        return lst.stream()
                .distinct()
                .toList();
    }

    // Тест2. Поиск в списке целых чисел 3-го наибольшего числа
    public static int test2(List<Integer> lst ){
        return lst.stream()
                .sorted(Integer::compare)
                .skip(2)
                .findFirst()
                .orElseThrow();
    }

    // Тест3. Поиск в списке целых чисел 3-го наибольшего уникального числа
    public static int test3(List<Integer> lst ){
        return lst.stream()
                .distinct()
                .sorted(Integer::compare)
                .skip(2)
                .findFirst()
                .orElseThrow();
    }

    // Тест4. получить список имен 3 самых старших сотрудников
    //      с должностью «Инженер», в порядке убывания возраста
    public static List<String> test4(List<Employee> lst ){
        return lst.stream()
                .filter((x) -> x.getPosition() == Position.ENGENEER)
                .sorted(Comparator.comparingInt(Employee::getAge).reversed())
                .limit(3)
                .map(Employee::getName)
                .toList();
    }
    // Тест5. Получить средний возраст сотрудников с должностью «Инженер»
    public static double test5(List<Employee> lst ) {
        return lst.stream()
                .mapToInt(Employee::getAge)
                .average()
                .orElseThrow();
    }

    // Тест6. Получить самое длинное слово в списке слов
    public static String test6(List<String> lst) {
        return lst.stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .findFirst()
                .orElseThrow();
    }

    // Тест7. Получить хеш-мап - (слово, встречаемость) для строки слов
    public static Map<String,Long> test7(String str) {
        return Arrays.stream(str.split(" "))
                .collect(Collectors.groupingBy(x->x, Collectors.counting()));
    }

    // Тест8. Отсортировать список в порядке увеличения длины слова, если слова имеют одинаковую длины,
    //        то должен быть сохранен алфавитный порядок
    public static List<String> test8(List<String> lst) {
        return lst.stream()
                .sorted(Comparator.comparing(String::length).thenComparing(x->x))
                .toList();
    }

    // Тест9. Имеется массив строк, в каждой из которых лежит набор из 5 строк, разделенных пробелом,
    //        найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них
    public static String test9(List<String> lst) {
             return lst.stream()
                     .map(s->s.split(" "))
                     .flatMap(Arrays::stream)
                     .sorted(Comparator.comparing(String::length).reversed())
                     .findFirst()
                     .orElseThrow();

    }

    public static List<Employee> createEmpList(){
        List<Employee> lst = new ArrayList<>();
        lst.add(new Employee("Иванов", 23, Position.ENGENEER));
        lst.add(new Employee("Петров", 47, Position.CLERK));
        lst.add(new Employee("Сидоров", 24, Position.ENGENEER));
        lst.add(new Employee("Афонькин", 38, Position.MANAGER));
        lst.add(new Employee("Лобастов", 26, Position.ENGENEER));
        lst.add(new Employee("Грибов", 51, Position.ENGENEER));
        return lst;
    }

    public static List<String> createStrList(){
        List<String> lst = new ArrayList<>();
        lst.add("Протокол Растение Нос Бред Прокол");
        lst.add("Один Хвастовство Певичка Тротуар Статуэтка");
        lst.add("Диплодок Java Распутство Апробирование Гондурас");
         return lst;
    }

}


