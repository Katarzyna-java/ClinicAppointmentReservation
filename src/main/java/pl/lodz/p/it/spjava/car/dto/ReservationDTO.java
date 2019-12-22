/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.dto;

import java.util.Date;
import java.util.Objects;
import pl.lodz.p.it.spjava.car.model.Patient;
import pl.lodz.p.it.spjava.car.model.Specialist;

public class ReservationDTO implements Comparable<ReservationDTO> {

    private Date reservationDate;
    private Specialist reservedSpecialist;
    private Patient reservationCreator;

    public ReservationDTO() {
    }

    public ReservationDTO(Specialist reservedSpecialist) {
        this.reservedSpecialist = reservedSpecialist;
    }

    public ReservationDTO(Date reservationDate, Specialist reservedSpecialist, Patient reservationCreator) {
        this.reservationDate = reservationDate;
        this.reservedSpecialist = reservedSpecialist;
        this.reservationCreator = reservationCreator;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Patient getReservationCreator() {
        return reservationCreator;
    }

    public void setReservationCreator(Patient reservationCreator) {
        this.reservationCreator = reservationCreator;
    }

    public Specialist getReservedSpecialist() {
        return reservedSpecialist;
    }

    public void setReservedSpecialist(Specialist reservedSpecialist) {
        this.reservedSpecialist = reservedSpecialist;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.reservationDate);
        hash = 73 * hash + Objects.hashCode(this.reservedSpecialist);
        hash = 73 * hash + Objects.hashCode(this.reservationCreator);
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
        final ReservationDTO other = (ReservationDTO) obj;
        if (!Objects.equals(this.reservationDate, other.reservationDate)) {
            return false;
        }
        if (!Objects.equals(this.reservedSpecialist, other.reservedSpecialist)) {
            return false;
        }
        if (!Objects.equals(this.reservationCreator, other.reservationCreator)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" + "reservationDate=" + reservationDate + ", reservedSpecialist=" + reservedSpecialist + ", reservationCreator=" + reservationCreator + '}';
    }

    @Override
    public int compareTo(ReservationDTO o) {
        return this.reservationDate.compareTo(o.reservationDate);
    }

}
