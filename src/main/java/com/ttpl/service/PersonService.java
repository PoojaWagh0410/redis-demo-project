package com.ttpl.service;

import com.ttpl.entity.Address;
import com.ttpl.entity.Person;

import java.util.List;

public interface PersonService {

    Person createPerson(Person person);

    Person getPersonById(Long id);

    List<Person> getAllPersons();

    Person updatePerson(Long id, Person personDetails);

    void deletePerson(Long id);

    Address createAddress(Address address);

    Address getAddressById(Long id);
}

