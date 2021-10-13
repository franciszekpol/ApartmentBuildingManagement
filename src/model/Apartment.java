package model;

import exception.CarCannotBeStoredInAnApartmentException;
import exception.TooManyThingsException;
import vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Apartment extends Space{
    private List<Person> residents;

    public Apartment(Integer size) {
        super(size);
        residents = new ArrayList<>();
    }

    @Override
    public void addItem(Item item) throws TooManyThingsException, CarCannotBeStoredInAnApartmentException{
        checkIfItemFitsIntoSpace(item);
        checkIfItemIsNotACar(item);
        this.getItems().add(item);
    }

    public void addResident(Person person) {
        this.residents.add(person);
    }

    public void evictAllResidents() {
        this.residents = new ArrayList<>();
    }

    public List<Person> getResidents() {
        return residents;
    }

    public void checkIfItemIsNotACar(Item item) throws CarCannotBeStoredInAnApartmentException {
        if(item instanceof Vehicle) {
           throw new CarCannotBeStoredInAnApartmentException("You cannot store a car in an apartment!");
        }
    }

    public void printResidents() {
        System.out.println("> Zameldowane osoby w mieszkaniu");
        for(Person person : residents) {
            System.out.println(residents.indexOf(person) + " - " + person);
        }
    }

    @Override
    public String toString() {
        String residentsInformation = "";
        if(residents.size() != 0) {
            residentsInformation += "\nZameldowane osoby: ";
            for(Person person : residents) {
                residentsInformation += person;
                residentsInformation += ", ";
            }
            residentsInformation = residentsInformation.substring(0, residentsInformation.length() -2);
        }
        return "Apartament. Id: " + this.getId() + " Metraż " + this.getSize() + residentsInformation;
    }
}
