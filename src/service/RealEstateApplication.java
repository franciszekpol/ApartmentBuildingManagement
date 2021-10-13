package service;

import exception.*;
import model.*;
import vehicle.AmphibiousVehicle;
import vehicle.CityCar;
import vehicle.OffRoadCar;
import vehicle.Vehicle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class RealEstateApplication {
    private Person user;
    private List<Person> people = new ArrayList<>();
    private List<Space> spaces = new ArrayList<>();
    private LeaseService leaseService = new LeaseService();
    private TimePassingThread timePassingThread = new TimePassingThread(leaseService);

    public RealEstateApplication() {
        loadData();
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public void printUsers() {
        System.out.println("Lista użytkowników w bazie:");
        for(Person person : people) {
            System.out.println(people.indexOf(person) + " - " + person);
        }
    }

    public void login() {
        System.out.println("\nPodaj swój indeks z listy aby się zalogować...");
        Scanner scanner = new Scanner(System.in);
        int userIndex = scanner.nextInt();
        this.setUser(people.get(userIndex));
    }

    public void printMainMenu() {
        while(true) {
            System.out.println("\n==========Main Menu=========== \n" +
           "0: Wyjdź z menu (Wznów upływ czasu) \n" +
           "1: Wyświetl dane użytkownika \n" +
           "2: Wyświetl pomieszczenia dostępne na wynajem \n" +
           "3: Wynajmij nowe pomieszczenie \n" +
           "4: Przedłuż umowę \n" +
           "5: Anuluj umowę  \n" +
           "6: Włóż lub wyjmij przedmiot z pomieszczenia \n" +
           "7: Zamelduj lub wymelduj osobę z mieszkania \n" +
           "8: Zapisz stan aplikacji do pliku tekstowego \n" +
           "9: Zakończ działanie programu \n" +
           "\nWybierz czynność podając odpowiedni numer...\n");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch(choice) {
                case 0: continueTimeFlow();
                    break;
                case 1: printPersonDataDependingOnPrivileges();
                    break;
                case 2: printEmptySpaces();
                    break;
                case 3: leaseNewSpace();
                    break;
                case 4: extendTheLease();
                    break;
                case 5: cancelLeaseAndEvictPerson();
                    break;
                case 6: depositOrWithdrawItem();
                    break;
                case 7: checkInOrCheckOutResident();
                    break;
                case 8: printInformationToFile();
                    break;
                case 9: System.exit(0);
                    break;
                default: break;
            }
        }
    }

    private void continueTimeFlow() {
        startTheThreadIfItWasNotStartedAlready();
        timePassingThread.resumeThread();
        System.out.println("Wciśnij dowolny przycisk by wrócić do menu");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        timePassingThread.suspendThread();
    }

    private void startTheThreadIfItWasNotStartedAlready() {
        if(timePassingThread.getState().equals(Thread.State.NEW)) {
            timePassingThread.start();
        }
    }

    private void printPersonDataDependingOnPrivileges() {
        if(user.hasAdministratorPrivelages()) {
            printUsers();
            System.out.println("\nPodaj indeks osoby której dane chcesz wyświetlić...");
            Scanner scanner = new Scanner(System.in);
            int personIndex = scanner.nextInt();
            Person person = people.get(personIndex);
            printPersonData(person);
        }else{
            printPersonData(user);
        }
    }

    private void printPersonData(Person person) {
        System.out.println("==== Informacje o użytkowniku ====");
        System.out.println(person);
        System.out.println("> Wynajmowane pomieszczenia:");
        for(Lease lease : leaseService.getAllLeaseOfACertainPerson(person)) {
            System.out.println(lease);
            Space space = lease.getSpace();
            space.printItems();
        }
        person.printItems();
        System.out.println("> Pliki File w aktach osoby:");
        person.printFiles();
    }

    private void leaseNewSpace() {
        printEmptySpaces();
        System.out.println("\n" + "Wybierz przestrzeń którą chcesz wynająć ...");
        Scanner scanner = new Scanner(System.in);

        int id = scanner.nextInt();
        System.out.println("Do którego dnia chcesz wynajmować to pomieszczenie?");
        int dayOfLeaseExpiration = scanner.nextInt();
        Space space = spaces.get(id);

        Lease lease = new Lease(this.user, space, dayOfLeaseExpiration);

        try {
            leaseService.addLease(lease);
            } catch(SpaceIsAlreadyAssignedException e) {
                e.printStackTrace();
                return;
            } catch(ProblematicTenantException e) {
                e.printStackTrace();
                return;
            } catch(MoreThanFiveSpacesLeasedException e) {
                e.printStackTrace();
                return;
            } catch (PersonDoesntHaveAnApartmentException e) {
                e.printStackTrace();
                return;
        }

    }

    private List<Space> getAvailableSpaces() {
        List<Space> availableSpaces = spaces;
        List<Space> occupiedSpaces = new ArrayList<>();
        for(Lease lease : leaseService.getLeaseList()) {
            occupiedSpaces.add(lease.getSpace());
        }

        for(Space space : occupiedSpaces) {
            availableSpaces.remove(space);
        }

        return availableSpaces;
    }

    private void printEmptySpaces() {
        List<Space> emptySpaces = getAvailableSpaces();
        System.out.println("Dostępne Mieszkania / Garaże do wynajęcia:" + "\n");
        for (Space space : emptySpaces) {
            System.out.println(spaces.indexOf(space) + " - " + space);
        }
    }

    private void printItems() {
        List<Item> items= user.getItems();
        System.out.println("> Przedmioty należące do użytkownika:");
        for (Item item :items) {
            System.out.println(items.indexOf(item) + " - " + item);
        }
    }

    private void depositOrWithdrawItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wybierz 0 żeby wstawić przedmiot do pomieszczenia");
        System.out.println("Wybierz 1 żeby wyjąć przedmiot z pomieszczenia");
        int choice = scanner.nextInt();
        boolean doesUserWantToDeposit = choice == 0;

        printLeaseListAccordingToPrivelages();

        System.out.println("Wybierz indeks umowy wybranego pomieszczenia...");
        int spaceIndex = scanner.nextInt();
        Space space = leaseService.getLeaseList().get(spaceIndex).getSpace();

        if(doesUserWantToDeposit) {
            createOrChooseItemToDeposit(space);
        } else {
            withdrawItem(space);
        }
    }

    private void createOrChooseItemToDeposit(Space space) {
        System.out.println("Wybierz 0 żeby wprowadzić nowy przedmiot do systemu");
        System.out.println("Wybierz 1 żeby wybrać już istniejący przedmiot");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        boolean doesUserWantToCreateANewItem = choice == 0;

        try{
            if(doesUserWantToCreateANewItem) {
                depositItem(createNewItem(), space);
            } else {
                printItems();
                System.out.println("Wybierz przedmot...");
                int itemIndex = scanner.nextInt();
                Item item = user.getItems().get(itemIndex);
                depositItem(item, space);
            }
        } catch(TooManyThingsException e) {
            e.printStackTrace();
            return;
        } catch(CarCannotBeStoredInAnApartmentException e) {
            e.printStackTrace();
            return;
        }

    }

    private Item createNewItem() {
        System.out.println("0 - Zwykły przedmiot" + "\n" + "1 - Pojazd");
        System.out.println("Wybierz typ przedmiotu...");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice == 1) {
            return createVehicle();
        } else {
            System.out.println("Podaj nazwę przedmiotu...");
            String name = scanner.nextLine();
            System.out.println("Podaj rozmiar przedmiotu...");
            int size = scanner.nextInt();
            return new Item(name, size);
        }
    }

     public void depositItem(Item item, Space space) throws TooManyThingsException, CarCannotBeStoredInAnApartmentException {
        space.addItem(item);
        user.getItems().remove(item);
        System.out.println("Przedmiot dodano pomyślnie");
    }

    private void withdrawItem(Space space) {
        Scanner scanner = new Scanner(System.in);
        space.printItems();
        System.out.println("Wybierz przedmiot który chcesz wyjąć...");
        int itemIndex = scanner.nextInt();
        user.getItems().add(space.getItems().get(itemIndex));
        space.getItems().remove(itemIndex);
        System.out.println("Przedmiot wyjęto pomyślnie");
    }

    private void checkInOrCheckOutResident() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wybierz 0 żeby zameldować nową osobę do mieszkania");
        System.out.println("Wybierz 1 żeby wymeldować osobę z mieszkania");
        int choice = scanner.nextInt();
        boolean doesUserWantToCheckInAPerson = choice == 0;

        System.out.println("Dostępne pomieszczenia:");
        if(user.hasAdministratorPrivelages()) {
            // Konieczne dodanie warunku wypisywania tylko tych apartamentów które mają lokatorów
            for(Lease lease : leaseService.getLeaseList()) {
                if (lease.getSpace() instanceof Apartment) {
                    System.out.println(lease.getSpace().getId() + " - " + lease.getSpace());
                }
            }
        } else {
            List<Lease> list = leaseService.getAllLeaseOfACertainPerson(user);
            for(Lease lease : list){
                if(lease.getSpace() instanceof Apartment) {
                    System.out.println(lease.getSpace().getId() + " - " + lease.getSpace());
                }
            }
        }

        System.out.println("Podaj numer wybranego pomieszczenia...");
        int spaceIndex = scanner.nextInt();
        Apartment apartment = (Apartment) leaseService.getLeaseList().get(spaceIndex).getSpace();

        if(doesUserWantToCheckInAPerson) {
            apartment.addResident(createNewPerson());
            System.out.println("Osobę zameldowano pomyślnie");
        } else {
            apartment.printResidents();
            System.out.println("Podaj numer indeks osoby którą chcesz wymeldować");
            int personIndex = scanner.nextInt();
            apartment.getResidents().remove(personIndex);
            System.out.println("Osoba została wymeldowana");
        }
    }

    private Person createNewPerson() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj imię");
        String firstName = scanner.nextLine();
        System.out.println("Podaj nazwisko");
        String lastName = scanner.nextLine();
        System.out.println("Podaj adres");
        String address = scanner.nextLine();
        System.out.println("Podaj datę urodzin");
        String birthday = scanner.nextLine();
        System.out.println("Podaj pesel");
        Integer pesel = scanner.nextInt();
        System.out.println("Pole adres: " + address);

        return new Person(firstName, lastName, pesel, address, birthday);
    }

    private void cancelLeaseAndEvictPerson() {
        printLeaseListAccordingToPrivelages();
        System.out.println("Podaj numer umowy którą chcesz anulować...");
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();
        leaseService.getLeaseList().get(index).evictAndRemoveTheFile();
        leaseService.getLeaseList().remove(index);
    }

    private void printLeaseListAccordingToPrivelages() {
        if(user.hasAdministratorPrivelages()) {
            leaseService.printLeaseList();
        } else {
            leaseService.printAllLeaseOfACertainPerson(user);
        }
    }

    private void extendTheLease() {
        printLeaseListAccordingToPrivelages();
        System.out.println("Wybierz umowę którą chcesz przedłużyć... ");
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();
        System.out.println("O ile dni chcesz przedłużyć umowę?");
        int numberOfDays = scanner.nextInt();
        leaseService.getLeaseList().get(index).renewLease(numberOfDays);
    }

    private Vehicle createVehicle() {
        // TODO
        return new CityCar("Name", "Engine", 0);
    }

    private void printInformationToFile() {
        try{
            FileWriter fileWriter = new FileWriter("osiedle3.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            Collections.sort(leaseService.getLeaseList());
            for(Lease lease : leaseService.getLeaseList()) {
                Collections.sort(lease.getSpace().getItems());
                printWriter.println(lease);
                printWriter.println("Przechowywane przedmioty:");
                printWriter.println(lease.getSpace().getItems() + "\n");
            }

            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Plik zapisano pomyślnie");
    }

    private void loadData() {

        people.add(new Person("Adam", "Kowalski (Admin)", 32987354, "Ul. Malinowa 40 m8", "02-10-1980"));
        people.get(0).assignAdministratorPrivelages();
        people.add(new Person("Radosław", "Piskorski", 27280259, "Ul. Złota 44 m277", "14-03-1975"));
        people.add(new Person("Andrzej", "Warszawski", 37104779, "Ul. Wiejska 10", "19-04-1971"));
        people.add(new Person("Paweł","Dzban",784378643,"Aleje Jerozolimskie 420","29-04-1999"));
        people.add(new Person("Beata", "Wójcik", 23894232, "Ul. Dworcowa 9 m20", "30-01-1945"));
        people.add(new Person("Jolanta", "Kobojek", 28847624, "Ul. Piwna 10 m43", "15-12-1950"));
        people.add(new Person("Rafał", "Dzik", 73847723, "Ul. Nowogrodzka 100 m2", "09-09-1959"));
        people.add(new Person("Antoni", "Wójtowicz", 8734673, "Ul. Hrubielowska 8 m1", "23-03-1950"));
        people.add(new Person("Irena", "Tosiek", 98873734, "Ul. Tulczyńska 30 m4", "01-12-1974"));
        people.add(new Person("Anna", "Skrzypczuk", 98327683, "Ul. Warszawskie Przedmieście 120 m80", "12-05-2000"));
        people.add(new Person("Xawery", "Lubieniecki", 92893487, "Ul. Aleje Palestyńskie 201 m19", "28-08-1945"));

        spaces.add(new Apartment(38));
        spaces.add(new Apartment(38));
        spaces.add(new Apartment(60));
        spaces.add(new Apartment(60));
        spaces.add(new Apartment(60));
        spaces.add(new Apartment(60));
        spaces.add(new Apartment(90));
        spaces.add(new Garage()); // indeks 7
        spaces.add(new Garage());
        spaces.add(new Garage());
        spaces.add(new Garage());
        spaces.add(new Garage());

        // Tworzenie umów o wynajem

        try {

        // 1 Radoslaw Piskorski
        leaseService.addLease(new Lease(people.get(1), spaces.get(0), 10));
        leaseService.addLease(new Lease(people.get(1), spaces.get(7), 10));
        // 2 Andrzej Warszawski
        leaseService.addLease(new Lease(people.get(2), spaces.get(1), 20));
        leaseService.addLease(new Lease(people.get(2), spaces.get(8), 10));
        // 3 Pawel Dzban
        leaseService.addLease(new Lease(people.get(3), spaces.get(2),12));
        leaseService.addLease(new Lease(people.get(3), spaces.get(6),8));
        // 4 Beata Wojcik
        leaseService.addLease(new Lease(people.get(4), spaces.get(3), 30));
        // 5 Jolanta Kobojek
        leaseService.addLease(new Lease(people.get(5), spaces.get(4), 5));
        leaseService.addLease(new Lease(people.get(5), spaces.get(9), 5));
        } catch(Exception e){

        }

        // Dodawanie przedmiotów do mieszkań i garaży
        try {
            leaseService.getLeaseList().get(0).getSpace().addItem(new Item("Dywan", 3));
            leaseService.getLeaseList().get(0).getSpace().addItem(new Item("Suszarka",1));
            leaseService.getLeaseList().get(0).getSpace().addItem(new Item("Bieżnia", 4));
            leaseService.getLeaseList().get(1).getSpace().addItem(new OffRoadCar("Tesla Off Road", "Tesla engine",5));
            leaseService.getLeaseList().get(2).getSpace().addItem(new Item("Meblościanka",10));
            leaseService.getLeaseList().get(2).getSpace().addItem(new Item("Dywan", 5));
            leaseService.getLeaseList().get(2).getSpace().addItem(new Item("Łóżko z baldachimem", 8));

            leaseService.getLeaseList().get(4).getSpace().addItem(new Item("Kuchnia", 10));
            leaseService.getLeaseList().get(4).getSpace().addItem(new Item("Biurko", 2));
            leaseService.getLeaseList().get(5).getSpace().addItem(new Item("Kanapa", 5));
            leaseService.getLeaseList().get(5).getSpace().addItem(new Item("Ława", 4));
            leaseService.getLeaseList().get(7).getSpace().addItem(new Item("Szafa", 5));
            leaseService.getLeaseList().get(7).getSpace().addItem(new Item("Jacuzzi", 5));
            leaseService.getLeaseList().get(3).getSpace().addItem(new CityCar("Maluch 126p", "R2 594 cm³, 23 KM", 4));
            leaseService.getLeaseList().get(8).getSpace().addItem(new AmphibiousVehicle("ZiŁ-485", 5, "R6, gaźnikowy, 5,56 l, moc 110 KM",28,"A"));
        } catch (TooManyThingsException e) {
            e.printStackTrace();
        } catch (CarCannotBeStoredInAnApartmentException e) {
            e.printStackTrace();
        }

        // Dodawanie przedmiotów do listy przedmiotów spoza osiedla (List<Item> w klasie Person)
        people.get(7).getItems().add(new AmphibiousVehicle("ZiŁ-485", 5, "R6, gaźnikowy, 5,56 l, moc 110 KM",28,"A"));
        people.get(7).getItems().add(new Item("Fotel", 2));
        people.get(0).getItems().add(new Item("Wieloryb", 2000));

        // Meldowanie osób w mieszkaniach
        Apartment tmpApartment = (Apartment) leaseService.getLeaseList().get(0).getSpace();
        tmpApartment.addResident(new Person("Iwona", "Abacka", 232323, "ul. Rowerowa", "03-04-2000"));
        tmpApartment = (Apartment) leaseService.getLeaseList().get(2).getSpace();
        tmpApartment.addResident(new Person("Anna", "Warszawska", 232323, "ul. Rowerowa", "03-04-2000"));
        tmpApartment = (Apartment) leaseService.getLeaseList().get(5).getSpace();
        tmpApartment.addResident(people.get(9));

    }
}
