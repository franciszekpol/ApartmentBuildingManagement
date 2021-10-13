package model;

import exception.TooManyThingsException;
import vehicle.Vehicle;

public class Garage extends Space {
    private static final Integer GARAGE_SIZE = 5;

    public Garage() {
        super(GARAGE_SIZE);
    }

    @Override
    public void addItem(Item item) {
        try{
            this.checkIfItemFitsIntoSpace(item);
        } catch(TooManyThingsException e) {
            e.printStackTrace();
            return;
        }
        this.getItems().add(item);
    }

    @Override
    public String toString() {
        String carInformation = "";
        for(Item item : getItems()) {
            if(item instanceof Vehicle) {
               carInformation += "\nPrzechowywany samochód: ";
               carInformation += item;
            }
        }
        return "Garaż. Id: " + this.getId() + " Metraż " + this.getSize() + " " + carInformation;
    }
}
