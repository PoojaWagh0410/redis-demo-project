package com.ttpl.service.serviceImpl;

import com.ttpl.entity.Address;
import com.ttpl.entity.Person;
import com.ttpl.repository.AddressRepository;
import com.ttpl.repository.PersonRepository;
import com.ttpl.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    @Override
    public Person createPerson(Person person) {
        Person savedPerson = personRepository.save(person);
        log.info("Person saved in DB with id: {}", savedPerson.getId());
        return savedPerson;
    }

    @Override
    @Cacheable(value = "personCache", key = "#id")
    public Person getPersonById(Long id) {
        log.info("Fetching person from DB with id: {}", id);
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    @CachePut(value = "personCache", key = "#id")
    public Person updatePerson(Long id, Person personDetails) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        person.setEmailId(personDetails.getEmailId());
        Person updatedPerson = personRepository.save(person);
        log.info("Person updated in DB and cache with id: {}", id);
        return updatedPerson;
    }

    @Override
    @CacheEvict(value = "personCache", key = "#id")
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
        log.info("Person deleted from DB and cache with id: {}", id);
    }

    @Override
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }
}
