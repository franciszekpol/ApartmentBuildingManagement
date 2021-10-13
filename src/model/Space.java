package model;

import exception.CarCannotBeStoredInAnApartmentException;
import exception.TooManyThingsException;
import vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public abstract class Space {
    private static int idCounter = 0;
    private Integer id;
    private Integer size;
    private List<Item> items;

    public Space(Integer size) {
        this.id = idCounter++;
        this.size = size;
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return items;
    }

    public abstract void addItem(Item item) throws TooManyThingsException, CarCannotBeStoredInAnApartmentException;

    public Integer getId() {
        return id;
    }

    public Integer getSize() {
        return size;
    }

    public void checkIfItemFitsIntoSpace(Item item) throws TooManyThingsException {
        if(getSizeOfAllItems() + item.getSize() > this.size) {
            throw new TooManyThingsException("Remove some old items to insert a new item.");
        }
    }

    public boolean doesTheSpaceStoreACar() {
        boolean isCarStoredInItems = false;
        for(Item item : items) {
            if(item instanceof Vehicle) {
                isCarStoredInItems = true;
            }
        }
        return isCarStoredInItems;
    }

    public void printItems() {
        System.out.println("> Przechowywane przedmioty: ");
        for(Item item : items) {
            System.out.println(items.indexOf(item) + " - " + item);
        }
        System.out.println();
    }

    private int getSizeOfAllItems(){
        int totalSize = 0;
        for(Item i : items) {
            totalSize += i.getSize();
        }
        return totalSize;
    }

}
