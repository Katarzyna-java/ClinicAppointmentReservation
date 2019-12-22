/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "RESERVATION", uniqueConstraints = {
    @UniqueConstraint(name = "UNIQUE_SPECIAL_DATE", columnNames = {"RESERVATION_DATE", "RESERVED_SPECIALIST"})
    , 
    @UniqueConstraint(name = "UNIQUE_CREATOR_DATE", columnNames = {"RESERVATION_CREATOR", "RESERVATION_DATE"})
})
@TableGenerator(name = "ReservationGenerator", table = "TableGenerator", pkColumnName = "ID", valueColumnName = "value", pkColumnValue = "ReservationGen")
@NamedQueries({
    @NamedQuery(name = "Reservation.findAll", query = "SELECT r FROM Reservation r")
    , @NamedQuery(name = "Reservation.findBySpecialistAndDate", query = "SELECT r FROM Reservation r WHERE r.reservedSpecialist = :reservedSpecialist AND r.reservationDate = :reservationDate")
    , @NamedQuery(name = "Reservation.findByReservedSpecialistLicenseNumber", query = "SELECT r FROM Reservation r WHERE r.reservedSpecialist.licenseNumber = :licenseNumber")
    , @NamedQuery(name = "Reservation.findCurrentReservations", query = "SELECT r FROM Reservation r WHERE r.reservationDate >= :currentDate")
    , @NamedQuery(name = "Reservation.findPastReservations", query = "SELECT r FROM Reservation r WHERE r.reservationDate < :currentDate")
    , @NamedQuery(name = "Reservation.findCurrentPatientReservations", query = "SELECT r FROM Reservation r WHERE r.reservationCreator.login = :login AND r.reservationDate >= :currentDate")
    , @NamedQuery(name = "Reservation.findPastPatientReservations", query = "SELECT r FROM Reservation r WHERE r.reservationCreator.login = :login AND r.reservationDate < :currentDate")
    , @NamedQuery(name = "Reservation.findCurrentSpecialistReservations", query = "SELECT r FROM Reservation r WHERE r.reservedSpecialist.licenseNumber = :licenseNumber AND r.reservationDate >= :currentDate")
    , @NamedQuery(name = "Reservation.findPastSpecialistReservations", query = "SELECT r FROM Reservation r WHERE r.reservedSpecialist.licenseNumber = :licenseNumber AND r.reservationDate < :currentDate")
})
public class Reservation extends AbstractEntity implements Serializable {

    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ReservationGenerator")
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "RESERVATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationDate;

    @JoinColumn(name = "RESERVATION_CREATOR", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Patient reservationCreator;

    @JoinColumn(name = "RESERVED_SPECIALIST", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Specialist reservedSpecialist;

    @JoinColumn(name = "MODIFIED_BY", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = false)
    private Patient modifiedBy;

    public Reservation() {
    }

    public Reservation(Long id) {
        this.id = id;
    }

    public Reservation(Long id, Date reservationDate, Patient reservationCreator, Specialist reservedSpecialist, Patient modifiedBy) {
        this.id = id;
        this.reservationDate = reservationDate;
        this.reservationCreator = reservationCreator;
        this.reservedSpecialist = reservedSpecialist;
        this.modifiedBy = modifiedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Patient getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Patient modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
        hash = 43 * hash + Objects.hashCode(this.reservationDate);
        hash = 43 * hash + Objects.hashCode(this.reservationCreator);
        hash = 43 * hash + Objects.hashCode(this.reservedSpecialist);
        hash = 43 * hash + Objects.hashCode(this.modifiedBy);
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
        final Reservation other = (Reservation) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.reservationDate, other.reservationDate)) {
            return false;
        }
        if (!Objects.equals(this.reservationCreator, other.reservationCreator)) {
            return false;
        }
        if (!Objects.equals(this.reservedSpecialist, other.reservedSpecialist)) {
            return false;
        }
        if (!Objects.equals(this.modifiedBy, other.modifiedBy)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Reservation{" + "id=" + id + ", reservationDate=" + reservationDate + ", reservationCreator=" + reservationCreator + ", reservedSpecialist=" + reservedSpecialist + ", modifiedBy=" + modifiedBy + '}';
    }

}
