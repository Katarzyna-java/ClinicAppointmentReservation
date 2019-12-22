/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.dto;

import java.util.Date;
import java.util.Objects;
import pl.lodz.p.it.spjava.car.model.AccessLevel;
import pl.lodz.p.it.spjava.car.model.Admin;

public class AccountDTO implements Comparable<AccountDTO> {

    private String login;
    private String password;
    private String question;
    private String answer;
    private String name;
    private String surname;
    private String phoneNumber;
    private boolean authorized;
    private boolean active;
    private AccessLevel accessLevel;
    private Admin accountCreator;
    private Admin modifedBy;
    private Date createdAt;
    private Date modifiedAt;
    private String oldPassword;

    public AccountDTO() {
    }


    public AccountDTO(String login, String password, String question, String answer, String name, String surname, String phoneNumber) {
        this.login = login;
        this.password = password;
        this.question = question;
        this.answer = answer;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public AccountDTO(String login, String name, String surname, String phoneNumber, Date createdAt) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public AccountDTO(String login, String name, String surname, String phoneNumber, boolean active, Date createdAt) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.createdAt = createdAt;
    }

    public AccountDTO(String login) {
        this.login = login;
    }

    public AccountDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public AccountDTO(String login, String question, String answer, boolean active) {
        this.login = login;
        this.question = question;
        this.answer = answer;
        this.active = active;
    }

    public AccountDTO(String login, String question, String answer, String name, String surname, String phoneNumber) {
        this.login = login;
        this.question = question;
        this.answer = answer;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Admin getAccountCreator() {
        return accountCreator;
    }

    public void setAccountCreator(Admin accountCreator) {
        this.accountCreator = accountCreator;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Admin getModifedBy() {
        return modifedBy;
    }

    public void setModifedBy(Admin modifedBy) {
        this.modifedBy = modifedBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "AccountDTO:" + " login= " + login;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.login);
        hash = 11 * hash + Objects.hashCode(this.password);
        hash = 11 * hash + Objects.hashCode(this.question);
        hash = 11 * hash + Objects.hashCode(this.answer);
        hash = 11 * hash + Objects.hashCode(this.name);
        hash = 11 * hash + Objects.hashCode(this.surname);
        hash = 11 * hash + Objects.hashCode(this.phoneNumber);
        hash = 11 * hash + (this.authorized ? 1 : 0);
        hash = 11 * hash + (this.active ? 1 : 0);
        hash = 11 * hash + Objects.hashCode(this.accessLevel);
        hash = 11 * hash + Objects.hashCode(this.accountCreator);
        hash = 11 * hash + Objects.hashCode(this.modifedBy);
        hash = 11 * hash + Objects.hashCode(this.createdAt);
        hash = 11 * hash + Objects.hashCode(this.modifiedAt);
        hash = 11 * hash + Objects.hashCode(this.oldPassword);
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
        final AccountDTO other = (AccountDTO) obj;
        if (this.authorized != other.authorized) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.question, other.question)) {
            return false;
        }
        if (!Objects.equals(this.answer, other.answer)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.surname, other.surname)) {
            return false;
        }
        if (!Objects.equals(this.phoneNumber, other.phoneNumber)) {
            return false;
        }
        if (!Objects.equals(this.oldPassword, other.oldPassword)) {
            return false;
        }
        if (this.accessLevel != other.accessLevel) {
            return false;
        }
        if (!Objects.equals(this.accountCreator, other.accountCreator)) {
            return false;
        }
        if (!Objects.equals(this.modifedBy, other.modifedBy)) {
            return false;
        }
        if (!Objects.equals(this.createdAt, other.createdAt)) {
            return false;
        }
        if (!Objects.equals(this.modifiedAt, other.modifiedAt)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(AccountDTO o) {
        return this.createdAt.compareTo(o.createdAt);
    }

}
