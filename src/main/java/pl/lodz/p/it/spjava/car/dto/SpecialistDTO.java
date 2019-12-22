/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.dto;

import java.util.Objects;
import pl.lodz.p.it.spjava.car.model.Specialization;

public class SpecialistDTO implements Comparable<SpecialistDTO> {

    private Specialization specialization;
    private String specialistName;
    private String specialistSurname;
    private String licenseNumber;
    private boolean active;
    private boolean presentInReservation;

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public String getSpecialistName() {
        return specialistName;
    }

    public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }

    public String getSpecialistSurname() {
        return specialistSurname;
    }

    public void setSpecialistSurname(String specialistSurname) {
        this.specialistSurname = specialistSurname;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPresentInReservation() {
        return presentInReservation;
    }

    public void setPresentInReservation(boolean presentInReservation) {
        this.presentInReservation = presentInReservation;
    }

    public SpecialistDTO() {
    }

    public SpecialistDTO(Specialization specialization, String specialistName, String specialistSurname, String licenseNumber) {
        this.specialization = specialization;
        this.specialistName = specialistName;
        this.specialistSurname = specialistSurname;
        this.licenseNumber = licenseNumber;
    }

    public SpecialistDTO(Specialization specialization, String specialistName, String specialistSurname, String licenseNumber, boolean active) {
        this.specialization = specialization;
        this.specialistName = specialistName;
        this.specialistSurname = specialistSurname;
        this.licenseNumber = licenseNumber;
        this.active = active;
    }

    public SpecialistDTO(Specialization specialization, String specialistName, String specialistSurname, String licenseNumber, boolean active, boolean presentInReservation) {
        this.specialization = specialization;
        this.specialistName = specialistName;
        this.specialistSurname = specialistSurname;
        this.licenseNumber = licenseNumber;
        this.active = active;
        this.presentInReservation = presentInReservation;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.specialization);
        hash = 97 * hash + Objects.hashCode(this.specialistName);
        hash = 97 * hash + Objects.hashCode(this.specialistSurname);
        hash = 97 * hash + Objects.hashCode(this.licenseNumber);
        hash = 97 * hash + (this.active ? 1 : 0);
        hash = 97 * hash + (this.presentInReservation ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpecialistDTO other = (SpecialistDTO) obj;
        if (this.active != other.active) {
            return false;
        }
        if (this.presentInReservation != other.presentInReservation) {
            return false;
        }
        if (!Objects.equals(this.specialistName, other.specialistName)) {
            return false;
        }
        if (!Objects.equals(this.specialistSurname, other.specialistSurname)) {
            return false;
        }
        if (!Objects.equals(this.licenseNumber, other.licenseNumber)) {
            return false;
        }
        if (!Objects.equals(this.specialization, other.specialization)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return specialistName + " " + specialistSurname + ", " + specialization;
    }

    @Override
    public int compareTo(SpecialistDTO o) {
        return this.specialistSurname.compareTo(o.specialistSurname);
    }

}
