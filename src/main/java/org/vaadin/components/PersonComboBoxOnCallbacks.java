package org.vaadin.components;


import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.DataProvider;
import org.vaadin.entities.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonComboBoxOnCallbacks {

    private ComboBox<Person> component;


    public PersonComboBoxOnCallbacks() {
        List<Person> people = new ArrayList<>(Stream.generate(Person::new).limit(10000).collect(Collectors.toList()));

        component = new ComboBox<>();
        component.setWidthFull();
        component.setItemLabelGenerator(Person::getName);
        component.setLabel("Person ComboBox on Callbacks");

        DataProvider<Person, String> dataProvider = DataProvider.fromFilteringCallbacks(query -> {
            Optional<String> filter = query.getFilter();
            int offset = query.getOffset();
            int limit = query.getLimit();
            List<Person> subList = getFilteredSubList2(people, filter, offset, limit);
            return subList.stream();
        }, query -> {
            Optional<String> filter = query.getFilter();
            return getFilteredSubListCount2(people, filter);
        });

        component.setDataProvider(dataProvider);
    }


    private static List<Person> getFilteredSubList2(List<Person> streetPlaceCombinedList, Optional<String> optionalFilter, int offset, int limit) {
        // Пробую использовать предварительно отсортированный список (при генерации элемента кэширования)
        Set<Person> resultSet = new LinkedHashSet<>(streetPlaceCombinedList);
        // Если есть фильтрация - то фильтруем, сначала по началу строки, потом по совпадению в строке
        optionalFilter.ifPresent(filterString -> {
            if (!filterString.isEmpty()) {
                // Список отфильтрованных по началу строки
                LinkedHashSet<Person> filteredByStartsSet = resultSet.stream()
                        .filter(person -> person.getName().toLowerCase().startsWith(filterString.toLowerCase()))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                // Список отфильтрованных по совпадению в строке
                LinkedHashSet<Person> filteredByContainsSet = resultSet.stream()
                        .filter(person -> person.getName().toLowerCase().contains(filterString.toLowerCase()))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                resultSet.clear();
                resultSet.addAll(filteredByStartsSet);
                resultSet.addAll(filteredByContainsSet);
            }
        });

        // Результирующий список. Проверяем на выход за границы списка.
        LinkedList<Person> resultList = new LinkedList<>(resultSet);
        return (limit + offset > resultList.size())
                ? resultList
                : resultList.subList(offset, offset + limit);
    }


    private static int getFilteredSubListCount2(List<Person> streetPlaceCombinedList, Optional<String> optionalFilter) {

        // Добавляем сначала улицы и сортируем по названию
        Set<Person> resultSet = new LinkedHashSet<>(streetPlaceCombinedList);
        // Если есть фильтрация - то фильтруем, сначала по началу строки, потом по совпадению в строке
        if (optionalFilter.isPresent()) {
            String filterString = optionalFilter.get();
            if (!filterString.isEmpty()) {
                // Список отфильтрованных по началу строки
                Set<Person> filteredByStartsSet = resultSet.stream()
                        .filter(person -> person.getName().toLowerCase().startsWith(filterString.toLowerCase()))
                        .collect(Collectors.toSet());
                // Список отфильтрованных по совпадению в строке
                Set<Person> filteredByContainsSet = resultSet.stream()
                        .filter(person -> person.getName().toLowerCase().contains(filterString.toLowerCase()))
                        .collect(Collectors.toSet());
                resultSet.clear();
                resultSet.addAll(filteredByStartsSet);
                resultSet.addAll(filteredByContainsSet);
            }
        }

        return resultSet.size();
    }




    public ComboBox<Person> getComponent() {
        return component;
    }
}
