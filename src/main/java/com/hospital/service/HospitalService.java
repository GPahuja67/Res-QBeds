package com.hospital.service;

import com.hospital.dao.HospitalDAO;
import com.hospital.model.Hospital;

import java.util.List;
import java.util.Scanner;

public class HospitalService {
    private final HospitalDAO dao = new HospitalDAO();
    private final Scanner sc;

    public HospitalService(Scanner sc) {
        this.sc = sc;
    }

    public void printWelcome() {
        System.out.println("=======================================");
        System.out.println("   Welcome to — Hospital Bed Manager   ");
        System.out.println("   Manage beds quickly during crisis  ");
        System.out.println("=======================================\n");
    }

    public void viewHospitals() {
        List<Hospital> list = dao.getAllHospitals();
        if (list.isEmpty()) {
            System.out.println("No hospitals found. Add a hospital first.");
            return;
        }
        System.out.println("\nID | Name            | Location     | Total | Available");
        System.out.println("-------------------------------------------------------");
        for (Hospital h : list) System.out.println(h.toString());
    }

    public void addHospital() {
        System.out.print("Enter hospital name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }

        System.out.print("Enter location: ");
        String location = sc.nextLine().trim();
        if (location.isEmpty()) { System.out.println("Location cannot be empty."); return; }

        int total = readInt("Enter total beds: ");
        if (total <= 0) { System.out.println("Total beds must be > 0."); return; }

        int avail = total; // new hospital available = total
        Hospital h = new Hospital(name, location, total, avail);
        int newId = dao.addHospital(h);
        if (newId > 0) System.out.println("✅ Hospital added with ID: " + newId);
        else System.out.println("❌ Failed to add hospital.");
    }

    public void allocateBed() {
        int id = readInt("Enter Hospital ID to allocate a bed: ");
        Hospital h = dao.getHospitalById(id);
        if (h == null) { System.out.println("Hospital ID not found."); return; }
        if (h.getAvailableBeds() <= 0) { System.out.println("No available beds in this hospital."); return; }

        int newAvail = h.getAvailableBeds() - 1;
        boolean ok = dao.updateAvailableBeds(id, newAvail);
        if (ok) System.out.println("✅ Bed allocated. New available beds: " + newAvail);
        else System.out.println("❌ Failed to allocate bed (internal error).");
    }

    public void releaseBed() {
        int id = readInt("Enter Hospital ID to release a bed: ");
        Hospital h = dao.getHospitalById(id);
        if (h == null) { System.out.println("Hospital ID not found."); return; }

        int newAvail = h.getAvailableBeds() + 1;
        if (newAvail > h.getTotalBeds()) {
            System.out.println("Cannot release beyond total beds (" + h.getTotalBeds() + ").");
            return;
        }
        boolean ok = dao.updateAvailableBeds(id, newAvail);
        if (ok) System.out.println("✅ Bed released. New available beds: " + newAvail);
        else System.out.println("❌ Failed to release bed (internal error).");
    }

    public void deleteHospital() {
        int id = readInt("Enter Hospital ID to delete: ");
        Hospital h = dao.getHospitalById(id);
        if (h == null) { System.out.println("Hospital ID not found."); return; }

        System.out.print("Are you sure delete hospital '" + h.getName() + "' (y/n)? ");
        String ans = sc.nextLine().trim().toLowerCase();
        if (!ans.equals("y")) { System.out.println("Delete cancelled."); return; }

        boolean ok = dao.deleteHospital(id);
        if (ok) System.out.println("✅ Hospital deleted.");
        else System.out.println("❌ Could not delete hospital.");
    }

    // helper to read int with validation
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
}
