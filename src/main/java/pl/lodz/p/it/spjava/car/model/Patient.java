/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.PATIENT_KEY)
@NamedQueries({
    @NamedQuery(name = "Patient.findAll", query = "SELECT a FROM Patient a")
    , @NamedQuery(name = "Patient.findByLogin", query = "SELECT a FROM Patient a WHERE a.login = :login")
})

public class Patient extends Account implements Serializable {

    public Patient() {
    }

    public Patient(Account account) {
        super(account.getId(), account.getLogin(), account.getPassword(), account.getQuestion(), account.getAnswer(), account.getName(), account.getSurname(), account.getPhoneNumber(), account.isAuthorized(), account.isActive(), account.getAccountCreator(), account.getModifiedBy());
    }

}
