package model;

public class File {
    private Space space;

    public File(Space space) {
       this.space = space;
    }

    public Space getSpace() {
        return space;
    }

    @Override
    public String toString() {
       return "Plik File - " + getSpace();
    }
}
