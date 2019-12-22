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
@DiscriminatorValue(AccessLevel.AccessLevelKeys.RECEPTION_KEY)
@NamedQueries({
    @NamedQuery(name = "Reception.findAll", query = "SELECT a FROM Reception a")
    , @NamedQuery(name = "Reception.findByLogin", query = "SELECT a FROM Reception a WHERE a.login = :login")
})
public class Reception extends Account implements Serializable {

    public Reception() {
    }

    public Reception(Account account) {
        super(account.getId(), account.getLogin(), account.getPassword(), account.getQuestion(), account.getAnswer(), account.getName(), account.getSurname(), account.getPhoneNumber(), account.isAuthorized(), account.isActive(), account.getAccountCreator(), account.getModifiedBy());
    }

}
