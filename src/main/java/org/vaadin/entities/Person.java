package org.vaadin.entities;

import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class Person {

    private static final Faker faker = new Faker();

    private static final Random random = new Random();

    private long id;

    private String name;

    private Integer age;

    public Person() {
        id = random.nextLong();
        name = faker.name().fullName();
        age = random.nextInt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
}