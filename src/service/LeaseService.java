package service;

import exception.PersonDoesntHaveAnApartmentException;
import exception.ProblematicTenantException;
import exception.SpaceIsAlreadyAssignedException;
import exception.MoreThanFiveSpacesLeasedException;
import model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LeaseService {
    private List<Lease> leaseList;

    public LeaseService() {
        this.leaseList = new ArrayList<>();
    }

    public void addLease(Lease lease) throws SpaceIsAlreadyAssignedException, ProblematicTenantException,
            MoreThanFiveSpacesLeasedException, PersonDoesntHaveAnApartmentException {

            checkIfSpaceIsAlreadyAssigned(lease.getSpace());
            checkIfPersonIsInDebt(lease.getTenant());
            checkIfPersonHasMoreThanFiveSpaces(lease.getTenant());
            if(lease.getSpace() instanceof Garage) {
                checkIfPersonHasAnApartment(lease.getTenant());
            }

        leaseList.add(lease);
    }

    public List<Lease> getLeaseList() {
        return leaseList;
    }

    public List<Lease> getAllLeaseOfACertainPerson(Person person) {
        List<Lease> leaseListOfAGivenPerson = new ArrayList<>();
        for (Lease lease : leaseList) {
            if(lease.getTenant().equals(person)) {
                leaseListOfAGivenPerson.add(lease);
            }
        }
        return leaseListOfAGivenPerson;
    }


     public void printLeaseList() {
        for(Lease lease : leaseList) {
            System.out.println("Indeks umowy: " + leaseList.indexOf(lease));
            System.out.println(lease + "\n");
        }
    }

    public void printAllLeaseOfACertainPerson(Person person) {
        List<Lease> leaseListOfACertainPerson = getAllLeaseOfACertainPerson(person);
        for(Lease lease : leaseListOfACertainPerson) {
            for(Lease lease2 : leaseList) {
                if(lease == lease2) {
                    System.out.println("Indeks umowy: " + leaseList.indexOf(lease));
                }
            }
            System.out.println(lease + "\n");
        }
    }

    public void processAllLease(int currentDay) {
        Iterator<Lease> iterator = leaseList.iterator();
        while(iterator.hasNext()) {
           Lease lease = iterator.next();
           if(hasLeaseExpired(currentDay, lease)) {
              if(!lease.doesHaveAnOutstandingFile()) {
                  lease.getTenant().addFile(new File(lease.getSpace()));
                  lease.setHasAnOutstandingFile(true);
                  System.out.println("Plik File został umieszczony w aktach " + lease.getTenant() + "!");
              }
           }
           if(haveThirtyDaysPassedSinceExpirationOfTheLease(currentDay, lease)) {
               if(lease.getSpace() instanceof Garage && lease.getSpace().doesTheSpaceStoreACar()) {
                   lease.sellTheCarAndRenewLease();
               } else {
                    lease.evictAndKeepTheFile();
                    iterator.remove();
               }
            }
        }
    }

    private boolean hasLeaseExpired(int currentDay, Lease lease) {
           return currentDay > lease.isValidUntil();
    }

    private boolean haveThirtyDaysPassedSinceExpirationOfTheLease(int currentDay, Lease lease) {
        return ((currentDay - lease.isValidUntil()) >= 3);
    }

        public void checkIfSpaceIsAlreadyAssigned(Space space) throws SpaceIsAlreadyAssignedException {
        for(Lease lease : leaseList) {
            if(lease.getSpace() == space) {
                throw new SpaceIsAlreadyAssignedException("This space is already assigned to a tenant!");
            }
        }
    }

    public void checkIfPersonHasAnApartment(Person person) throws PersonDoesntHaveAnApartmentException{
        boolean personHasAnApartment = false;
        for(Lease lease : leaseList) {
            if(lease.getTenant() == person && lease.getSpace() instanceof Apartment) {
                personHasAnApartment = true;
            }
        }

        if(!personHasAnApartment) {
            throw new PersonDoesntHaveAnApartmentException("Cannot assign garage to a person without an apartment!");
        }
    }

    public void checkIfPersonIsInDebt(Person person) throws ProblematicTenantException {
        if(person.hasThreeOrMoreFiles()) {
            throw new ProblematicTenantException(person.getFirstName() + " " + person.getLastName() +
                    " is already in debt with: " + person.getFiles());
        }
    }

    public void checkIfPersonHasMoreThanFiveSpaces(Person person) throws MoreThanFiveSpacesLeasedException {
        int leaseCounter = 0;
        for(Lease lease : leaseList) {
            if(lease.getTenant() == person) {
                leaseCounter++;
            }
        }
        if(leaseCounter >= 5) {
            throw new MoreThanFiveSpacesLeasedException("This person is already leasing 5 spaces!");
        }
    }

}
