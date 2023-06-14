/*
 * Copyright(C) 2023 Luvina Software Company
 *
 * Main.java, Jun 134, 2023 Vunbl
 */

package org.example;

import lombok.SneakyThrows;
import org.example.connection.DBConnect;
import org.example.models.Candidate;
import org.example.models.ExperienceCandidate;
import org.example.models.FresherCandidate;
import org.example.models.InternCandidate;

import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

    /**
     * Quan ly ung vien bang console
	 * @author Vunbl
	 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ArrayList<Candidate> candidates = new ArrayList<>();

        boolean isRunning = true;
        while (isRunning) {
            displayMenu(); //hien thi menu
            int choice = getUserChoice();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    Candidate candidate = inputCandidateData();
                    candidates.add(candidate);
                    ExperienceCandidate experienceCandidate = inputExperienceData();
                    handleExperienceCandidate(candidate,experienceCandidate);
                    break;
                case 2:
                    Candidate candidateF = inputCandidateData();
                    candidates.add(candidateF);
                    FresherCandidate fresherCandidate = inputFresherData();
                    handleFresherCandidate(candidateF,fresherCandidate);
                    break;
                case 3:
                    Candidate candidateI = inputCandidateData();
                    candidates.add(candidateI);
                    InternCandidate internCandidate = inputInternData();
                    handleInternCandidate(candidateI,internCandidate);
                    break;
                case 4:
                    handleSearch();
                    break;
                case 5:
                     deleteById();
                     break;
                case 6:
                     updateById();
                case 7:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }



    private static ExperienceCandidate inputExperienceData() {
        System.out.println("Enter Experience Candidates details:");
        int exp = -1;
        boolean isValid = false;
        while (exp < 0 || exp > 100 || !isValid){
            System.out.print("Exp in Year : ");
          try {
              exp = Integer.parseInt(scanner.nextLine());
              isValid = true;
          }catch (NumberFormatException e){
              System.out.println("Please input correct number 1 - 100  ");
               exp = -1;
          }
        }
        System.out.print("Pro Skill: ");
        String skill = scanner.nextLine();
        return new ExperienceCandidate(exp ,skill);
    }

        private static InternCandidate inputInternData() {
        System.out.print("Majors: ");
        String major = scanner.nextLine();
            System.out.print("Semester: ");
            String semester = scanner.nextLine();
            System.out.print("University Name: ");
            String universityName = scanner.nextLine();
        return new InternCandidate(major,semester,universityName);
    }

    public static Candidate inputCandidateData() {
        scanner.nextLine();
        System.out.println("Enter candidate details:");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        // Birth Date input with validation
        Date birthDate = null;
        boolean validBirthDate = false;
        while (!validBirthDate) {
            System.out.print("Birth Date (yyyy-mm-dd): ");
            String birthDateString = scanner.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date parsedDate = dateFormat.parse(birthDateString);
                // Convert the java.util.Date to java.sql.Date
                birthDate = new Date(parsedDate.getTime());
                validBirthDate = true;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the birth date in yyyy-mm-dd format.");
            }
        }
        System.out.print("Address: ");
        String address = scanner.nextLine();
        // Phone input with validation
        String phone = null;
        boolean validPhone = false;
        while (!validPhone) {
            System.out.print("Phone: ");
            phone = scanner.nextLine();
            if (phone.trim().length() >= 10) {
                validPhone = true;
            } else {
                System.out.println("Invalid phone number. Phone number should have at least 10 characters.");
            }
        }
        // Email input with validation
        String email = null;
        boolean validEmail = false;
        while (!validEmail) {
            System.out.print("Email: ");
            email = scanner.nextLine();
            if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                validEmail = true;
            } else {
                System.out.println("Invalid email format. Please enter a valid email address.");
            }
        }
        return new Candidate( firstName, lastName, birthDate,address, phone, email);
    }

    public static FresherCandidate inputFresherData() {
        System.out.println("Enter fresher details:");
        System.out.print("Graduation Date:");
        Date graduationDate = null;
        boolean validDate = false;
        while (!validDate) {
            System.out.print("Birth Date (yyyy-mm-dd): ");
            String birthDateString = scanner.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date parsedDate = dateFormat.parse(birthDateString);
                // Convert the java.util.Date to java.sql.Date
                graduationDate = new Date(parsedDate.getTime());
                validDate = true;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-mm-dd format.");
            }
        }
        System.out.print("graduationRank: ");
        String graduationRank = scanner.nextLine();
        System.out.print("geducation: ");
        String education = scanner.nextLine();
        return new FresherCandidate(graduationDate,graduationRank,education);
    }
        /**
    	 * Xuat ra menu
    	 */
    private static void displayMenu() {
        System.out.println("----- CANDIDATE MANAGEMENT SYSTEM -----");
        System.out.println("1. Experience");
        System.out.println("2. Fresher");
        System.out.println("3. Internship");
        System.out.println("4. Searching");
        System.out.println("5. Delete by ID");
        System.out.println("6. Update basic data by ID");
        System.out.println("7. Exit");
    }

        /**
         * Chon chuc nang
         * @return Int dai dien cua chuc nang
         */
    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

        /**
         Thêm thông tin ứng viên kinh nghiệm vào cơ sở dữ liệu.
         @param candidate: Thông tin cơ bản của ứng viên.
         @param experienceCandidate: Thông tin về ứng viên kinh nghiệm.
         */
    @SneakyThrows
    private static void handleExperienceCandidate(Candidate candidate , ExperienceCandidate experienceCandidate) {
        Connection connection = new DBConnect().getConnection();
        String insertCandidatesQuery = "INSERT INTO Candidates (FirstName, LastName, BirthDate, Address, Phone, Email,CandicateType) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement candidatesStatement = null;
        int rowsAffected;
        try {
            int parameterIndex = 1;
            candidatesStatement = connection.prepareStatement(insertCandidatesQuery, Statement.RETURN_GENERATED_KEYS);
            candidatesStatement.setString(parameterIndex++, candidate.getFirstName());
            candidatesStatement.setString(parameterIndex++, candidate.getLastName());
            candidatesStatement.setDate(parameterIndex++,candidate.getBirthDate());
            candidatesStatement.setString(parameterIndex++, candidate.getAddress());
            candidatesStatement.setString(parameterIndex++, candidate.getPhone());
            candidatesStatement.setString(parameterIndex++, candidate.getEmail());
            candidatesStatement.setInt(parameterIndex,0);
            rowsAffected = candidatesStatement.executeUpdate();
            int candidateId = 1;
            if (rowsAffected > 0) {
                ResultSet generatedKeys = candidatesStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    candidateId = generatedKeys.getInt(1);
                }
            }
            String insertExperienceCandidatesQuery = "INSERT INTO ExperienceCandidates (CandidateId, ExpInYear,ProSkill) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement experiantStatement = connection.prepareStatement(insertExperienceCandidatesQuery);
            parameterIndex = 1;
            experiantStatement.setInt(parameterIndex++, candidateId);
            experiantStatement.setInt(parameterIndex++, experienceCandidate.getExpInYear());
            experiantStatement.setString(parameterIndex++, experienceCandidate.getProSkill());
            experiantStatement.executeUpdate();
            experiantStatement.close();
            candidatesStatement.close();
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }

        /**
         * Xử lý thông tin ứng viên mới tốt nghiệp.
         * @param candidate: Thông tin cơ bản của ứng viên.
         * @param fresherCandidate: Thông tin về ứng viên mới tốt nghiệp.
         */
    @SneakyThrows
    private static void handleFresherCandidate(Candidate candidate, FresherCandidate fresherCandidate){
        Connection connection = new DBConnect().getConnection();
        String insertCandidatesQuery = "INSERT INTO Candidates (FirstName, LastName, BirthDate, Address, Phone, Email) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement candidatesStatement = null;
        int rowsAffected;
        try {
            int parameterIndex = 1;
            candidatesStatement = connection.prepareStatement(insertCandidatesQuery, Statement.RETURN_GENERATED_KEYS);
            candidatesStatement.setString(parameterIndex++, candidate.getFirstName());
            candidatesStatement.setString(parameterIndex++, candidate.getLastName());
            candidatesStatement.setDate(parameterIndex++,candidate.getBirthDate());
            candidatesStatement.setString(parameterIndex++, candidate.getAddress());
            candidatesStatement.setString(parameterIndex++, candidate.getPhone());
            candidatesStatement.setString(parameterIndex++, candidate.getEmail());
            rowsAffected = candidatesStatement.executeUpdate();
            int candidateId = 1;
            if (rowsAffected > 0) {
                ResultSet generatedKeys = candidatesStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    candidateId = generatedKeys.getInt(1);
                }
            }
            String insertFresherQuery = "INSERT INTO FresherCandidates (CandidateId, GraduationDate, GraduationRank, Education) " +
                    "VALUES (?, ?, ?, ?)";
            parameterIndex = 1;
            PreparedStatement fresherStatement = connection.prepareStatement(insertFresherQuery);
            fresherStatement.setInt(parameterIndex++, candidateId);
            fresherStatement.setDate(parameterIndex++, fresherCandidate.getGraduationDate());
            fresherStatement.setString(parameterIndex++, fresherCandidate.getGraduationRank());
            fresherStatement.setString(parameterIndex++, fresherCandidate.getEducation());
            fresherStatement.executeUpdate();
            fresherStatement.close();
            candidatesStatement.close();
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }

        /**
         * Xử lý thông tin ứng viên thực tập.
         * @param candidate: Thông tin cơ bản của ứng viên.
         * @param internCandidate: Thông tin về ứng viên thực tập.
         */
    @SneakyThrows
    private static void handleInternCandidate(Candidate candidate , InternCandidate internCandidate) {
        Connection connection = new DBConnect().getConnection();
        String insertCandidatesQuery = "INSERT INTO Candidates (FirstName, LastName, BirthDate, Address, Phone, Email) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement candidatesStatement = null;
        int rowsAffected;
        try {
            int parameterIndex = 1;
            candidatesStatement = connection.prepareStatement(insertCandidatesQuery, Statement.RETURN_GENERATED_KEYS);
            candidatesStatement.setString(parameterIndex++, candidate.getFirstName());
            candidatesStatement.setString(parameterIndex++, candidate.getLastName());
            candidatesStatement.setDate(parameterIndex++,candidate.getBirthDate());
            candidatesStatement.setString(parameterIndex++, candidate.getAddress());
            candidatesStatement.setString(parameterIndex++, candidate.getPhone());
            candidatesStatement.setString(parameterIndex++, candidate.getEmail());
            rowsAffected = candidatesStatement.executeUpdate();
            int candidateId = 1;
            if (rowsAffected > 0) {
                ResultSet generatedKeys = candidatesStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    candidateId = generatedKeys.getInt(1);
                }
            }
            String insertInternCandidateQuery = "INSERT INTO InternCandidates (CandidateId, Majors, Semester, UniversityName) " +
                    "VALUES (?, ?, ?, ?)";
            parameterIndex = 1;
            PreparedStatement internStatement = connection.prepareStatement(insertInternCandidateQuery);
            internStatement.setInt(parameterIndex++, candidateId);
            internStatement.setString(parameterIndex++, internCandidate.getMajors());
            internStatement.setString(parameterIndex++, internCandidate.getSemester());
            internStatement.setString(parameterIndex++, internCandidate.getUniversityName());
            internStatement.executeUpdate();
            internStatement.close();
            candidatesStatement.close();
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }
        /**
         * Xử lý chức năng tìm kiếm ứng viên.
         */
    private static void handleSearch() {
        System.out.println("You have selected Searching.");
        System.out.println("Name of Candidate :");
        String name =  scanner.nextLine();
        System.out.println("Choose type of Candidate");
        System.out.println("1.Experience");
        System.out.println("2.Fresher");
        System.out.println("3.Intern");
        int choice = scanner.nextInt();
        String type = "";
        switch (choice) {
            case 1:
                type = "ExperienceCandidates";
                break;
            case 2:
                type = "FresherCandidates";
                break;
            case 3:
                type = "InternCandidates";
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        ArrayList<Candidate> candidateFound = searchByFirstNameAndType(name,type);
        if (candidateFound.isEmpty()){
            System.out.println("No candidates found with the given name and type.");
        }else {
           for (Candidate x :candidateFound){
               System.out.println(x.getFirstName() +" " +x.getLastName() + " | " + x.getBirthDate().getYear() + " | " +
                       x.getAddress() + " | " + x.getPhone() + " | " + x.getEmail() + " | " + type);
           }
        }
    }

        /**
         Tìm kiếm ứng viên theo tên và loại.
         @param firstName: Tên ứng viên.
         @param type: Loại ứng viên.
         @return Danh sách các ứng viên phù hợp với tên và loại đã cho.
         */
    private static ArrayList<Candidate> searchByFirstNameAndType(String firstName,String type) {
        ArrayList<Candidate> candidates = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        try {
            String query = "SELECT c.* FROM Candidates c INNER JOIN " + type + " t ON c.CandidateId = t.CandidateId WHERE c.FirstName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int candidateId = resultSet.getInt("CandidateId");
                String lastName = resultSet.getString("LastName");
                Date birthDate = resultSet.getDate("BirthDate");
                String address = resultSet.getString("Address");
                String phone = resultSet.getString("Phone");
                String email = resultSet.getString("Email");
                // Create the Candidate object based on the retrieved data
               Candidate candidateFound = new Candidate(candidateId, firstName, lastName, birthDate, address, phone, email,0);
               candidates.add(candidateFound);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return candidates;
    }
        /**
         * Xóa bản ghi của ứng viên dựa trên ID.
         * */
    private static void deleteById() {
        System.out.println("Insert ID of candidate");
        int id = scanner.nextInt();
        Connection connection = new DBConnect().getConnection();
        try {
            String query = "DELETE FROM Candidates WHERE CandidateId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Candidate with ID " + id + " deleted successfully.");
            } else {
                System.out.println("No candidate found with the given ID.");
            }
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    private static void updateById(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert ID of candidate");
        int id = scanner.nextInt();
        Connection connection = new DBConnect().getConnection();
        Candidate candidate = inputCandidateData();
        try {
            String query = "UPDATE Candidates SET FirstName = ?, LastName = ?, BirthDate = ?, Address = ?, Phone = ?, Email = ? WHERE CandidateId = ?";
            PreparedStatement candidatesStatement = connection.prepareStatement(query);
            int parameterIndex = 1;
            candidatesStatement.setString(parameterIndex++, candidate.getFirstName());
            candidatesStatement.setString(parameterIndex++, candidate.getLastName());
            candidatesStatement.setDate(parameterIndex++,candidate.getBirthDate());
            candidatesStatement.setString(parameterIndex++, candidate.getAddress());
            candidatesStatement.setString(parameterIndex++, candidate.getPhone());
            candidatesStatement.setString(parameterIndex++, candidate.getEmail());
            candidatesStatement.setInt(parameterIndex,id);
            int rowsAffected = candidatesStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Candidate with ID " + id + " updated successfully.");
            } else {
                System.out.println("No candidate found with the given ID.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}