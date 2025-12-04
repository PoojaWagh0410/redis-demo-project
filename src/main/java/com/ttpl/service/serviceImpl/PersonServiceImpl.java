package com.ttpl.service.serviceImpl;

import com.ttpl.entity.Address;
import com.ttpl.entity.Person;
import com.ttpl.repository.AddressRepository;
import com.ttpl.repository.PersonRepository;
import com.ttpl.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final RedisService redisService;

    @Override
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person getPersonById(Long id) {

        String key = "person details with id :" + id;

        Person cachedPerson = redisService.getValue(key, Person.class);

        if (cachedPerson != null) {
            log.info("Person fetched from Redis(Cache) - Id: ",id);
            return cachedPerson;
        }

        Person personFromDb = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));

        redisService.setValue(key, personFromDb, 60l);
        log.info("Person fetched from database - Id: ",id);
        return personFromDb;
    }


    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Person updatePerson(Long id, Person personDetails) {
        Person person = getPersonById(id);
        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        person.setEmailId(personDetails.getEmailId());
        return personRepository.save(person);
    }

    @Override
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
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
