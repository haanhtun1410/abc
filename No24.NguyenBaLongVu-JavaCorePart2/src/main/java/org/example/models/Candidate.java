package org.example.models;



import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    private int candidateId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String address;
    private String phone;
    private String email;
    private int candidateType;


    @Override
    public String toString() {
        return "Candidate{" +
                "candidateId=" + candidateId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Candidate(String firstName, String lastName, Date birthDate, String address, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
}
