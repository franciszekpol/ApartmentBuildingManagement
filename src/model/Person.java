package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Person {
    private String firstName;
    private String lastName;
    private Integer pesel;
    private String address;
    private String birthday;
    private List<File> files = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private boolean administratorPrivelages = false;

    public Person(String firstName, String lastName, Integer pesel, String address, String birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.address = address;
        this.birthday = birthday;
    }

    public void assignAdministratorPrivelages() {
        this.administratorPrivelages = true;
    }

    public boolean hasAdministratorPrivelages() {
        return this.administratorPrivelages;
    }

    @Override
    public String toString() {
        return (firstName + " " + lastName);
    }

    public boolean hasThreeOrMoreFiles() {
        if(this.files.size() >= 3) {
            return true;
        } else {
            return false;
        }
    }

    public void removeFile(Space space) {
        Iterator<File> iterator = files.iterator();
        while(iterator.hasNext()) {
            if(iterator.next().getSpace() == space) {
                iterator.remove();
            }
        }
    }

    public void printFiles() {
        for(File file : files) {
            System.out.println(file);
        }
    }

    public List<File> getFiles() {
        return files;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void printItems() {
        System.out.println("> Przedmioty użytkownika spoza osiedla:");
        for(Item item : items) {
            System.out.println(items.indexOf(item) + " - " + item);
        }
    }
}
