package org.vaadin.components;


import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.DataProvider;
import org.vaadin.entities.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonComboBoxOnList {

    private ComboBox<Person> component;


    public PersonComboBoxOnList() {
        List<Person> people = new ArrayList<>(Stream.generate(Person::new).limit(10000).collect(Collectors.toList()));

        component = new ComboBox<>();
        component.setWidthFull();
        component.setItemLabelGenerator(Person::getName);
        component.setLabel("Person ComboBox on List");
        component.setItems(people);

    }






    public ComboBox<Person> getComponent() {
        return component;
    }
}
