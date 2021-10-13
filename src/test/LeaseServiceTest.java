package test;

import exception.*;
import model.*;
import org.junit.jupiter.api.*;
import service.LeaseService;
import service.RealEstateApplication;
import vehicle.CityCar;

public class LeaseServiceTest {

    private LeaseService service;

    @BeforeEach
    public void init() {
       service = new LeaseService();
    }

    @Nested
    @DisplayName("Testing Exceptions")
    class TestingExceptions{


    @Test
    public void shouldThrowCarCannotBeStoredInAnApartmentException() {
        Assertions.assertThrows(CarCannotBeStoredInAnApartmentException.class, () -> {
            Apartment apartment = new Apartment(100);
            apartment.addItem(new CityCar("Car name", "Engine", 0));
        }, "Attempt to place a car inside an apartment should throw an exception");
    }

    @Test
    public void shouldThrowMoreThanFiveSpacesLeasedException() {
        Person person = new Person("Andrzej", "Warszawski", 37104779, "Ul. Wiejska 10", "19-04-1971");
        Assertions.assertThrows(MoreThanFiveSpacesLeasedException.class, () -> {
            for(int i=0; i<6; i++) {
                service.addLease(new Lease(person, new Apartment(100),30));
            }
        }, "User cannot have more than five leased spaces");
    }

    @Test
    public void shouldThrowPersonDoesntHaveAnApartmentException() {
        Person person = new Person("Andrzej", "Warszawski", 37104779, "Ul. Wiejska 10", "19-04-1971");
        Assertions.assertThrows(PersonDoesntHaveAnApartmentException.class, () -> {
            Garage garage = new Garage();
            service.addLease(new Lease(person, garage, 100));
        }, "Person without an apartment cannot lease a garage");
    }

    @Test
    public void shouldThrowProblematicTenantException() {
        Person person = new Person("Andrzej", "Warszawski", 37104779, "Ul. Wiejska 10", "19-04-1971");
        person.addFile(new File(new Apartment(100)));
        person.addFile(new File(new Apartment(100)));
        person.addFile(new File(new Apartment(100)));
        Assertions.assertThrows(ProblematicTenantException.class, () -> {
            service.addLease(new Lease(person, new Apartment(100),30));
        }, "Person with three outstanding files cannot lease anymore spaces");
    }

    @Test
    public void shouldThrowSpaceIsAlreadyAssignedException() {
        Person person = new Person("Andrzej", "Warszawski", 37104779, "Ul. Wiejska 10", "19-04-1971");
        Person person2 = new Person("Andrzej", "Warszawski", 37104779, "Ul. Wiejska 10", "19-04-1971");
        Assertions.assertThrows(SpaceIsAlreadyAssignedException.class, () -> {
            Apartment apartment = new Apartment(100);
            service.addLease(new Lease(person, apartment,30));
            service.addLease(new Lease(person2, apartment,30));
        }, "Cannot lease a space that is already leased by somebody else");
    }

    // Test do przeniesienia do klasy testowej RealEstateApp..
    @Test
    public void shouldThrowTooManyThingsException() {
        RealEstateApplication application = new RealEstateApplication();
        Apartment apartment = new Apartment(100);
        Assertions.assertThrows(TooManyThingsException.class, () -> {
            application.depositItem(new Item("Name",2000), apartment);
        }, "The isn't enough empty space inside the space to add another item");
    }
    }
}































