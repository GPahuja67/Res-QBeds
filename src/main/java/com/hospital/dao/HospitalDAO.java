package com.hospital.dao;

import com.hospital.model.Hospital;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalDAO {

    public List<Hospital> getAllHospitals() {
        List<Hospital> list = new ArrayList<>();
        String sql = "SELECT id, name, location, total_beds, available_beds FROM hospitals ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Hospital(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getInt("total_beds"),
                        rs.getInt("available_beds")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching hospitals: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public Hospital getHospitalById(int id) {
        String sql = "SELECT id, name, location, total_beds, available_beds FROM hospitals WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Hospital(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getInt("total_beds"),
                            rs.getInt("available_beds")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting hospital by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public int addHospital(Hospital h) {
        String sql = "INSERT INTO hospitals (name, location, total_beds, available_beds) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, h.getName());
            ps.setString(2, h.getLocation());
            ps.setInt(3, h.getTotalBeds());
            ps.setInt(4, h.getAvailableBeds());

            int rows = ps.executeUpdate();
            if (rows == 0) return -1;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error adding hospital: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateAvailableBeds(int hospitalId, int newBeds) {
        String sql = "UPDATE hospitals SET available_beds = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newBeds);
            ps.setInt(2, hospitalId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating beds: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHospital(int id) {
        String sql = "DELETE FROM hospitals WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting hospital: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
