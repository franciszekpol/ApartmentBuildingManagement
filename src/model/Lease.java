package model;

import vehicle.Vehicle;

import java.util.Iterator;
import java.util.List;

public class Lease implements Comparable<Lease>{
    private Person tenant;
    private Space space;
    private Integer validUntil;
    private boolean hasAnOutstandingFile = false;

    public Lease(Person tenant, Space space, Integer validUntil) {
        this.tenant = tenant;
        this.space = space;
        this.validUntil = validUntil;
    }

    public Person getTenant() {
        return tenant;
    }

    public Space getSpace() {
        return space;
    }

    public Integer isValidUntil() {
        return validUntil;
    }

    public boolean doesHaveAnOutstandingFile() {
        return hasAnOutstandingFile;
    }

    public void setHasAnOutstandingFile(boolean flag) {
        if(flag) {
            this.hasAnOutstandingFile = true;
        } else {
            this.hasAnOutstandingFile = false;
        }
    }

    public void moveAllItemsToPersonsList() {
        List<Item> items = getSpace().getItems();
        Iterator<Item> iterator = items.iterator();
        while(iterator.hasNext()) {
            getTenant().addItem(iterator.next());
            iterator.remove();
        }
    }

    public void evictAndKeepTheFile() {
        moveAllItemsToPersonsList();
        if(getSpace() instanceof Apartment) {
            ((Apartment) getSpace()).evictAllResidents();

        }
        System.out.println("Eksmitowano " + getTenant() + ", plik File pozostał w aktach.");
    }

    public void evictAndRemoveTheFile() {
        moveAllItemsToPersonsList();
        if(getSpace() instanceof Apartment) {
            ((Apartment) getSpace()).evictAllResidents();

        }
        getTenant().removeFile(getSpace());
        setHasAnOutstandingFile(false);
        System.out.println("Eksmitowano " + getTenant() + ", plik File został usunięty.");
    }

    public void renewLease(int numberOfDays) {
        riseTheValidUntilNumber(numberOfDays);
        getTenant().removeFile(getSpace());
        setHasAnOutstandingFile(false);
        System.out.println("Przedłużono umowę dla: " + getTenant() + "\n" + "Umowa wygasa w dniu: " + isValidUntil());
    }

    public void sellTheCarAndRenewLease() {
        List<Item> items = getSpace().getItems();
        Iterator<Item> iterator = items.iterator();
        while(iterator.hasNext()) {
            if(iterator.next() instanceof Vehicle) {
                iterator.remove();
                riseTheValidUntilNumber(60);
                getTenant().removeFile(getSpace());
                setHasAnOutstandingFile(false);
                System.out.println("Sprzedano samochód " + getTenant() + ", przedłużono umowę o 60 dni, usunięto plik File");
            }
        }
    }

    public void riseTheValidUntilNumber(int numberOfDays) {
        this.validUntil += numberOfDays;
    }

    @Override
    public String toString() {
        return (space + "\n" +
                "Umowa na osobę: " + tenant.getFirstName() + " " + tenant.getLastName() + "\n" +
                "Umowa wygasa w dniu: " + validUntil);
    }

    @Override
    public int compareTo(Lease lease) {
        return this.getSpace().getSize().compareTo(lease.getSpace().getSize());
    }



}
