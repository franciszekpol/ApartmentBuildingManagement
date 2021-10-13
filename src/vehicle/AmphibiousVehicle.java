package vehicle;

public class AmphibiousVehicle extends Vehicle implements Driveable, Swimmable{
    private String waterResistanceGrade;

    public AmphibiousVehicle(String name, Integer size, String engine, int numberOfSeats,
                             String waterResistanceGrade) {
        super(name, size, engine, numberOfSeats);
        this.waterResistanceGrade = waterResistanceGrade;
    }

    @Override
    public void drive() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void submerge() {

    }

    @Override
    public void emerge() {

    }

}
