/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.exception;

import javax.persistence.NoResultException;
import pl.lodz.p.it.spjava.car.model.Account;

public class AccountException extends AppBaseException {

    static final public String KEY_ACCOUNT_LOGIN_EXIST = "error.account.login.exist.problem";
    static final public String KEY_ACCOUNT_WRONG_STATE = "error.account.wrong.state.problem";
    static final public String KEY_ACCOUNT_WRONG_PASSWORD = "error.account.wrong.password.problem";
    static final public String KEY_NO_ACCOUNT_FOUND = "error.no.account.found.problem";
    static final public String KEY_ACCOUNT_ALREADY_ACTIVATED = "error.account.already.active.problem";
    static final public String KEY_ACCOUNT_ALREADY_DEACTIVATED = "error.account.already.deactive.problem";
    static final public String KEY_ACCOUNT_ALREADY_CHANGED = "error.account.already.changed.problem";
    static final public String KEY_ACCOUNT_USED_IN_ACCOUNTS = "error.unique.account.used.in.accounts.problem";
    static final public String KEY_ACCOUNT_USED_IN_SPECIALISTS = "error.unique.account.used.in.specialists.problem"; 
    static final public String KEY_ACCOUNT_USED_IN_SPECIALIZATION = "error.unique.account.used.in.specializations.problem"; 
    static final public String KEY_ACCOUNT_USED_IN_RESERVATIONS = "error.unique.account.used.in.reservations.problem";
    static final public String KEY_ACCOUNT_NOT_ACTIVE = "error.account.not.active.problem";

    private Account account;

    public Account getAccount() {
        return account;
    }

    private AccountException(String message, Account account) {
        super(message);
        this.account = account;
    }

    private AccountException(String message, Throwable cause, Account account) {
        super(message, cause);
        this.account = account;
    }

    private AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    private AccountException(String message) {
        super(message);
    }

    static public AccountException createExceptionLoginAlreadyExists(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_LOGIN_EXIST, cause, account);
    }

    static public AccountException createExceptionAccountInUseInSpecialization(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_USED_IN_SPECIALISTS, cause, account);
    }

    static public AccountException createExceptionAccountInUseInSpecialist(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_USED_IN_SPECIALISTS, cause, account);
    }

    static public AccountException createExceptionAccountInUseInAccount(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_USED_IN_ACCOUNTS, cause, account);
    }

    static public AccountException createExceptionAccountInUseInReservation(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_USED_IN_RESERVATIONS, cause, account);
    }

    static public AccountException createExceptionWrongState(Account account) {
        return new AccountException(KEY_ACCOUNT_WRONG_STATE, account);
    }

    static public AccountException createExceptionWrongPassword(Account account) {
        return new AccountException(KEY_ACCOUNT_WRONG_PASSWORD, account);
    }

    public static AccountException createExceptionNoAccountFound(NoResultException e) {
        return new AccountException(KEY_NO_ACCOUNT_FOUND, e);
    }

    static public AccountException createExceptionAccountAlreadyActvivated(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_ACTIVATED, account);
    }

    static public AccountException createExceptionAccountAlreadyDeactvivated(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_DEACTIVATED, account);
    }

    static public AccountException ceateExceptionAccountChangedByAnotherAdmin(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_CHANGED, account);
    }

    static public AccountException ceateExceptionAccountAlreadyHaveThisAccessLevel(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_CHANGED, account);
    }

    public static AccountException createExceptionAccountNotActive(Account account) {
        return new AccountException(KEY_ACCOUNT_NOT_ACTIVE);
    }

}
