package src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Welcome to the System ---");
            System.out.println("1. Seller");
            System.out.println("2. Buyer");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 3) {
                System.out.println("Goodbye!");
                break;
            }

            UserService userService;
            DatabaseHandler dbHandler;

            if (choice == 1) {
                dbHandler = new SellerDatabaseHandler();
                userService = new SellerService((SellerDatabaseHandler) dbHandler);
            } else if (choice == 2) {
                dbHandler = new BuyerDatabaseHandler();
                userService = new BuyerService((BuyerDatabaseHandler) dbHandler);
            } else {
                System.out.println("Invalid option. Please try again.");
                continue;
            }

            while (true) {
                System.out.println("\n--- Seller/Buyer Options ---");
                System.out.println("1. Register");
                System.out.println("2. Log in");
                System.out.println("3. Back");
                System.out.print("Choose an option: ");
                int subChoice = scanner.nextInt();
                scanner.nextLine();

                if (subChoice == 3)
                    break;

                switch (subChoice) {
                    case 1:
                        userService.register();
                        break;
                    case 2:
                        userService.login();
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }

        scanner.close();
    }
}
