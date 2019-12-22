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
@Table(name = "SPECIALIZATION", uniqueConstraints = {
    @UniqueConstraint(name = "UNIQUE_SPECIALIZA", columnNames = "SPECIALIZATION_CODE")
})
@TableGenerator(name = "SpecializationGenerator", table = "TableGenerator", pkColumnName = "ID", valueColumnName = "value", pkColumnValue = "SpecializationGen")
@NamedQueries({
    @NamedQuery(name = "Specialization.findAll", query = "SELECT s FROM Specialization s")
    , @NamedQuery(name = "Specialization.findBySpecializationCode", query = "SELECT s FROM Specialization s WHERE s.specializationCode = :specializationCode") 
})
public class Specialization extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SpecializationGenerator")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "SPECIALIZATION_CODE", nullable = false)
    private String specializationCode;

    @Basic(optional = false)
    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "SPECIALIZATION_NAME", nullable = false)
    private String specializationName;

    @JoinColumn(name = "SPECIALIZATION_CREATOR", referencedColumnName = "ID", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private Reception specializationCreator;

    @JoinColumn(name = "MODIFIED_BY", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = false)
    private Reception modifiedBy;

    public Specialization() {
    }

    public Specialization(Long id) {
        this.id = id;
    }

    public Specialization(String specializationCode) {

        this.specializationCode = specializationCode;

    }

    public Specialization(Long id, String specializationCode, String specializationName) {
        this.id = id;
        this.specializationCode = specializationCode;
        this.specializationName = specializationName;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecializationCode() {
        return specializationCode;
    }

    public void setSpecializationCode(String specializationCode) {
        this.specializationCode = specializationCode;
    }

    public String getSpecializationName() {
        return specializationName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    public Reception getSpecializationCreator() {
        return specializationCreator;
    }

    public void setSpecializationCreator(Reception specializationCreator) {
        this.specializationCreator = specializationCreator;
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
        hash = 59 * hash + Objects.hashCode(this.specializationCode);
        hash = 59 * hash + Objects.hashCode(this.specializationName);
        hash = 59 * hash + Objects.hashCode(this.specializationCreator);
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
        final Specialization other = (Specialization) obj;
        if (!Objects.equals(this.specializationCode, other.specializationCode)) {
            return false;
        }
        if (!Objects.equals(this.specializationName, other.specializationName)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.specializationCreator, other.specializationCreator)) {
            return false;
        }
        if (!Objects.equals(this.modifiedBy, other.modifiedBy)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return specializationName;
    }

}
