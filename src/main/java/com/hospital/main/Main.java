package com.hospital.main;

import com.hospital.service.HospitalService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HospitalService service = new HospitalService(sc);

        service.printWelcome();

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. View all hospitals");
            System.out.println("2. Add hospital");
            System.out.println("3. Allocate bed");
            System.out.println("4. Release bed");
            System.out.println("5. Delete hospital");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            String input = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1 -> service.viewHospitals();
                case 2 -> service.addHospital();
                case 3 -> service.allocateBed();
                case 4 -> service.releaseBed();
                case 5 -> service.deleteHospital();
                case 6 -> {
                    System.out.println("Goodbye! Closing app.");
                    sc.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}
