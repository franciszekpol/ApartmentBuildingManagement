import service.RealEstateApplication;

public class Main {
    public static void main(String[] args) {
          RealEstateApplication application = new RealEstateApplication();
          application.printUsers();
          application.login();
          application.printMainMenu();
    }
}
