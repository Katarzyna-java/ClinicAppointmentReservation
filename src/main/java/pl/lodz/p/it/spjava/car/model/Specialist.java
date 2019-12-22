/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.model;

import java.io.Serializable;
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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "SPECIALIST", uniqueConstraints = {
    @UniqueConstraint(name = "UNIQUE_LICENSE_NR", columnNames = "LICENSE_NUMBER")
})
@TableGenerator(name = "SpecialistGenerator", table = "TableGenerator", pkColumnName = "ID", valueColumnName = "value", pkColumnValue = "SpecialistGen")
@NamedQueries({
    @NamedQuery(name = "Specialist.findAll", query = "SELECT s FROM Specialist s")
    , @NamedQuery(name = "Specialist.findBySpecializationCode", query = "SELECT s FROM Specialist s WHERE s.specialization.specializationCode = :specializationCode")
    , @NamedQuery(name = "Specialist.findBySpecializationCodeAndActive", query = "SELECT s FROM Specialist s WHERE s.specialization.specializationCode = :specializationCode AND s.active = true")
    , @NamedQuery(name = "Specialist.findByLicenseNumber", query = "SELECT s FROM Specialist s WHERE s.licenseNumber = :licenseNumber")
})
public class Specialist extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SpecialistGenerator")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;

    @JoinColumn(name = "SPECIALIZATION", referencedColumnName = "ID", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private Specialization specialization;

    @Basic(optional = false)
    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "SPECIALIST_NAME", nullable = false)
    private String specialistName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "SPECIALIST_SURNAME", nullable = false)
    private String specialistSurname;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @Basic(optional = false)
    @NotNull
    @Size(min = 7, max = 7)
    @Column(name = "LICENSE_NUMBER", nullable = false)
    private String licenseNumber;

    @JoinColumn(name = "SPECIALIST_CREATOR", referencedColumnName = "ID", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private Reception specialistCreator;

    @JoinColumn(name = "MODIFIED_BY", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = false)
    private Reception modifiedBy;

    public Specialist() {
    }

    public Specialist(Long id) {
        this.id = id;
    }

    public Specialist(Long id, Specialization specialization, String specialistName, String specialistSurname, String licenseNumber) {
        this.id = id;
        this.specialization = specialization;
        this.specialistName = specialistName;
        this.specialistSurname = specialistSurname;
        this.licenseNumber = licenseNumber;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Reception getSpecialistCreator() {
        return specialistCreator;
    }

    public void setSpecialistCreator(Reception specialistCreator) {
        this.specialistCreator = specialistCreator;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Reception getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Reception modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.specialization);
        hash = 59 * hash + Objects.hashCode(this.specialistName);
        hash = 59 * hash + Objects.hashCode(this.specialistSurname);
        hash = 59 * hash + (this.active ? 1 : 0);
        hash = 59 * hash + Objects.hashCode(this.licenseNumber);
        hash = 59 * hash + Objects.hashCode(this.specialistCreator);
        hash = 59 * hash + Objects.hashCode(this.modifiedBy);
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
        final Specialist other = (Specialist) obj;
        if (this.active != other.active) {
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.specialization, other.specialization)) {
            return false;
        }
        if (!Objects.equals(this.specialistCreator, other.specialistCreator)) {
            return false;
        }
        if (!Objects.equals(this.modifiedBy, other.modifiedBy)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return specialistName;
    }

}
