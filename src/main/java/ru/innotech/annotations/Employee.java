package ru.innotech.annotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Employee {
    private String fio;
    private Integer age;
    private LocalDate begDate;
    private BigDecimal fee;
    private static List<Employee> emps = new ArrayList<>();

    public Employee(String fio, Integer age, LocalDate begDate, BigDecimal fee) {
        this.fio = fio;
        this.age = age;
        this.begDate = (begDate==null) ? LocalDate.now() : begDate;
        this.fee = fee;
    }

    public Employee() {
        this.begDate = LocalDate.now();
    }

    public Integer getAge() {
        return age;
    }

    @Test(priority = 2)
    @CsvSource("43")
    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBegDate() {
        return begDate;
    }

    public void setBegDate(LocalDate begDate) {
        this.begDate = begDate;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "fio='" + fio + '\'' +
                ", age=" + age +
                ", begDate=" + begDate +
                ", fee=" + fee +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return getAge() == employee.getAge() && Objects.equals(getFio(), employee.getFio()) && Objects.equals(getBegDate(), employee.getBegDate()) && Objects.equals(getFee(), employee.getFee());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFio(), getAge(), getBegDate(), getFee());
    }

    public static void addEmployee(Employee emp){
        emps.add(emp);
    }

    public static void delAllEmployee(){
        emps.clear();
    }

    @BeforeSuite
    public static void beforeSuite(){
        addEmployee(new Employee("Иванов Степан Игоревич", 47, null, BigDecimal.valueOf(120000)));
        addEmployee(new Employee("Петров Антон Степанович", 32, null, BigDecimal.valueOf(150000)));
        addEmployee(new Employee("Кротов Иван Сергеевич", 24, null, BigDecimal.valueOf(70000)));
    }

    @AfterSuite
    public static void afterSuite(){
        delAllEmployee();
    }

    @Test(priority = 1)
    @CsvSource("500")
    public void Test1(BigDecimal addFee){
        setFee(getFee().add(addFee));
        System.out.println("...Test1. Параметр addFee="+addFee);
    }

    @Test
    @CsvSource("25, 1960-12-03, 350")
    public void Test2(Integer age, LocalDate begDate, BigDecimal fee){
        setAge(age);
        setBegDate(begDate);
        setFee(fee);
        System.out.println("...Test2. Параметр age="+age+",LocalDate="+begDate+ ", fee="+fee);
    }

    public static int getCount(){
        return emps.size();
    }

    @BeforeTest
    public static void beforeTest(int nTst, String methName){
        System.out.println(nTst + "+. Метод "+methName+". Старт.");
    }

    @AfterTest
    public static void afterTest(int nTst, String methName){
        System.out.println(nTst + "+. Метод "+methName+". Финиш.");
    }

    public static Employee getObj(int n){
        return emps.get(n);
    }

}
