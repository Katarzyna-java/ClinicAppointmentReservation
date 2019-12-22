/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.NEWACCOUNT_KEY)
public class NewAccount extends Account implements Serializable {

    public NewAccount() {
    }
 
}
