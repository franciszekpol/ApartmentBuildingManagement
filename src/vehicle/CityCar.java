package vehicle;

public class CityCar extends Vehicle implements Driveable{

    public CityCar(String name, String engine, int numberOfSeats) {
        super(name, 2, engine, numberOfSeats);
    }

    public void drive(){

    }

    public void stop(){

    }
}
