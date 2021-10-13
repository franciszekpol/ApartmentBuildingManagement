package vehicle;

import model.Item;

public abstract class Vehicle extends Item {
    private String engine;
    private int numberOfSeats;
    private String color;

    public Vehicle(String name, Integer size, String engine, int numberOfSeats) {
        super(name, size);
        this.engine = engine;
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
