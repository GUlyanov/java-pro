package ru.innotech.streamapi;

import ru.innotech.streamapi.Position;


public class Employee {
    private String name;
    private Integer age;
    private Position position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Employee(String name, int age, Position position) {
        this.name = name;
        this.age = age;
        this.position = position;
    }

    @Override
    public String toString() {
        return "ru.innotech.streamapi.Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", position=" + position +
                '}';
    }
}
