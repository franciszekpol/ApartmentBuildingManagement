package vehicle;

public class OffRoadCar extends Vehicle implements Driveable{

    public OffRoadCar(String name, String engine, int numberOfSeats) {
        super(name, 3, engine, numberOfSeats);
    }

    public void drive(){

    }

    public void stop(){

    }
}
