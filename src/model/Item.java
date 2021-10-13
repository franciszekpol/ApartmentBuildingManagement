package model;

public class Item implements Comparable<Item>{
    private String name;
    private Integer size;

    public Item(String name, Integer size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    @Override
    public String toString() {
       return this.name + " " + this.size;
    }

    @Override
    public int compareTo(Item i) {
        if(size > i.size) {
            return -1;
        }
        else if (size < i.size) {
            return 1;
        }
        return name.compareTo(i.name);
    }

}
