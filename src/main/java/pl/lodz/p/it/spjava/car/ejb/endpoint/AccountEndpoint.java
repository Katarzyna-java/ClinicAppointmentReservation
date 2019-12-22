/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.ejb.endpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.OptimisticLockException;
import pl.lodz.p.it.spjava.car.dto.AccountDTO;
import pl.lodz.p.it.spjava.car.ejb.facade.AccountFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.AdminFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.PatientFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.ReceptionFacade;
import pl.lodz.p.it.spjava.car.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.car.exception.AccountException;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.model.AccessLevel;
import pl.lodz.p.it.spjava.car.model.Account;
import pl.lodz.p.it.spjava.car.model.Admin;
import pl.lodz.p.it.spjava.car.model.NewAccount;
import pl.lodz.p.it.spjava.car.model.Patient;
import pl.lodz.p.it.spjava.car.model.Reception;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class AccountEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @EJB
    private AdminFacade adminFacade;

    @EJB
    private PatientFacade patientFacade;

    @EJB
    private ReceptionFacade receptionFacade;

    @EJB
    private AccountFacade accountFacade;

    @Resource
    private SessionContext sessionContext;

    private Account accountState;

    private Account myAccountState;

    private Account questionCheckAccountState;

    private Account passwordResetAccountState;

    private List<Account> savedAccountStateList;

    @RolesAllowed({"Admin"})
    public Account getAccountState() {
        return accountState;
    }

    @RolesAllowed({"Admin"})
    public void setAccountState(Account accountState) {
        this.accountState = accountState;
    }

    @RolesAllowed({"Admin", "Reception", "Patient"})
    public Account getMyAccountState() {
        return myAccountState;
    }

    @RolesAllowed({"Admin", "Reception", "Patient"})
    public void setMyAccountState(Account myAccountState) {
        this.myAccountState = myAccountState;
    }

    public Account getQuestionCheckAccountState() {
        return questionCheckAccountState;
    }

    public void setQuestionCheckAccountState(Account questionCheckAccountState) {
        this.questionCheckAccountState = questionCheckAccountState;
    }

    public Account getPasswordResetAccountState() throws AccountException {
        if (!passwordResetAccountState.getLogin().equals(questionCheckAccountState.getLogin())) {
            throw AccountException.createExceptionWrongState(passwordResetAccountState);
        }
        return passwordResetAccountState;
    }

    public void setPasswordResetAccountState(Account passwordResetAccountState) throws AccountException {
        if (!passwordResetAccountState.getLogin().equals(questionCheckAccountState.getLogin())) {
            throw AccountException.createExceptionWrongState(passwordResetAccountState);
        }
        this.passwordResetAccountState = passwordResetAccountState;
    }

    @RolesAllowed({"Admin"})
    private Admin loadCurrentAdmin() throws AppBaseException {
        String adminLogin = sessionContext.getCallerPrincipal().getName();
        Admin adminAccount = adminFacade.findByLogin(adminLogin);
        if (adminAccount == null) {
            throw AppBaseException.createExceptionNotAuthorizedAction();
        }
        if (!adminAccount.isActive()) {
            throw AccountException.createExceptionAccountNotActive(adminAccount);
        }
        return adminAccount;
    }

    public Account selectAccountWithIterator(String login, List<Account> accounts) {
        Iterator<Account> iterator = accounts.iterator();
        while (iterator.hasNext()) {
            Account account = iterator.next();
            if (account.getLogin().equals(login)) {
                return account;
            }
        }
        return null;
    }

    @PermitAll
    public void registerAccount(AccountDTO accountDTO) throws AppBaseException {
        Account newAccount = new NewAccount();
        newAccount.setLogin(accountDTO.getLogin());
        newAccount.setPassword(accountDTO.getPassword());
        newAccount.setQuestion(accountDTO.getQuestion());
        newAccount.setAnswer(accountDTO.getAnswer());
        newAccount.setName(accountDTO.getName());
        newAccount.setSurname(accountDTO.getSurname());
        newAccount.setPhoneNumber(accountDTO.getPhoneNumber());

        newAccount.setActive(false);
        newAccount.setAuthorized(false);

        accountFacade.create(newAccount);
    }

    @RolesAllowed({"Admin"})
    public List<AccountDTO> listNewAccounts() throws AppBaseException {
        List<Account> listRegisteredAccount = accountFacade.findNewAccount();
        savedAccountStateList = listRegisteredAccount;
        List<AccountDTO> listNewRegisteredAccount = new ArrayList<>();
        for (Account account : listRegisteredAccount) {
            AccountDTO accountDTO = new AccountDTO(
                    account.getLogin(),
                    account.getName(),
                    account.getSurname(),
                    account.getPhoneNumber(),
                    account.getCreatedAt()
            );
            listNewRegisteredAccount.add(accountDTO);
        }
        Collections.sort(listNewRegisteredAccount);
        return listNewRegisteredAccount;
    }

    @RolesAllowed({"Admin"})
    public void deleteAccount(AccountDTO accountDTO) throws AppBaseException {
        accountState = accountFacade.findByLogin(accountDTO.getLogin());
        if (accountState instanceof NewAccount) {
            accountFacade.remove(accountState);
        } else {
            throw AccountException.ceateExceptionAccountChangedByAnotherAdmin(accountState);
        }
    }

    @RolesAllowed({"Admin"})
    public void changeAccessLevelAccount(AccountDTO accountDTO) throws AppBaseException {
        Account account = selectAccountWithIterator(accountDTO.getLogin(), savedAccountStateList);
        if (accountDTO.getCreatedAt() == null || accountDTO.getCreatedAt().equals(account.getCreatedAt())) {
            Account newAccount = null;
            switch (accountDTO.getAccessLevel()) {
                case RECEPTION:
                    newAccount = new Reception(account);
                    break;
                case ADMIN:
                    newAccount = new Admin(account);
                    break;
                case PATIENT:
                    newAccount = new Patient(account);
                    break;
            }

            if (newAccount != null) {
                accountFacade.remove(account);
                if (!newAccount.isAuthorized()) {
                    newAccount.setAuthorized(true);
                    newAccount.setActive(true);
                }
                newAccount.setAccountCreator(loadCurrentAdmin());
                accountFacade.create(newAccount);
            }
        } else {
            try {
                throw new OptimisticLockException("Wyjątek wywołany podczas zmiany poziomu dostępu użytkownika na liście autoryzowanych kont, w sytuacji gdy konto po wyświetleniu listy zostało już edytowane przez innego administratora");
            } catch (OptimisticLockException e) {
                throw AppBaseException.createExceptionOptimisticLock(e);
            }
        }
    }

    @RolesAllowed({"Admin"})
    public List<AccountDTO> listAuthorizedAccounts() throws AppBaseException {
        List<Account> listAuthorizedAccounts = accountFacade.findAuthorizedAccount();
        savedAccountStateList = listAuthorizedAccounts;
        List<AccountDTO> listAccounts = new ArrayList<>();
        for (Account account : listAuthorizedAccounts) {
            AccountDTO accountDTO = new AccountDTO(
                    account.getLogin(),
                    account.getName(),
                    account.getSurname(),
                    account.getPhoneNumber(),
                    account.isActive(),
                    account.getCreatedAt()
            );

            Admin adminAccount = adminFacade.findByLogin(account.getLogin());
            Patient patientAccount = patientFacade.findByLogin(account.getLogin());
            Reception receptionAccount = receptionFacade.findByLogin(account.getLogin());

            if (adminAccount instanceof Admin) {
                accountDTO.setAccessLevel(AccessLevel.ADMIN);
            }
            if (patientAccount instanceof Patient) {
                accountDTO.setAccessLevel(AccessLevel.PATIENT);
            }
            if (receptionAccount instanceof Reception) {
                accountDTO.setAccessLevel(AccessLevel.RECEPTION);
            }
            listAccounts.add(accountDTO);
        }
        Collections.sort(listAccounts);
        return listAccounts;
    }

    @RolesAllowed({"Admin"})
    public void activateAccount(String login) throws AppBaseException {
        Account account = selectAccountWithIterator(login, savedAccountStateList);
        if (!adminFacade.findByLogin(sessionContext.getCallerPrincipal().getName()).isActive()) {
            throw AccountException.createExceptionAccountNotActive(account);
        }
        if (!account.isActive()) {
            account.setActive(true);
            account.setModifiedBy(loadCurrentAdmin());
            accountFacade.edit(account);
        } else {
            throw AccountException.createExceptionAccountAlreadyActvivated(account);
        }
    }

    @RolesAllowed({"Admin"})
    public void deactivateAccount(String login) throws AppBaseException {
        Account account = selectAccountWithIterator(login, savedAccountStateList);
        if (account.isActive()) {
            account.setActive(false);
            account.setModifiedBy(loadCurrentAdmin());
            accountFacade.edit(account);
        } else {
            throw AccountException.createExceptionAccountAlreadyDeactvivated(account);
        }
    }

    @RolesAllowed({"Admin"})
    public void changeAccountPassword(AccountDTO accountDTO) throws AppBaseException {
        if (accountState.getLogin().equals(accountDTO.getLogin())) {
            accountState.setPassword(accountDTO.getPassword());
            accountState.setModifiedBy(loadCurrentAdmin());
            accountFacade.edit(accountState);
        } else {
            throw AccountException.createExceptionWrongState(accountState);
        }
    }

    @PermitAll
    public void resetAccountPassword(AccountDTO accountDTO) throws AppBaseException {
        if (passwordResetAccountState.getLogin().equals(accountDTO.getLogin())) {
            passwordResetAccountState.setPassword(accountDTO.getPassword());
            passwordResetAccountState.setModifiedBy(passwordResetAccountState.getClass().getSimpleName().equals("Admin") ? adminFacade.findByLogin(accountDTO.getLogin()) : null);
            accountFacade.edit(passwordResetAccountState);
        } else {
            throw AccountException.createExceptionWrongState(passwordResetAccountState);
        }
    }

    @RolesAllowed({"Admin"})
    public void editAccount(AccountDTO accountDTO) throws AppBaseException {
        if (accountState.getLogin().equals(accountDTO.getLogin())) {
            accountState.setName(accountDTO.getName());
            accountState.setSurname(accountDTO.getSurname());
            accountState.setPhoneNumber(accountDTO.getPhoneNumber());
            accountState.setQuestion(accountDTO.getQuestion());
            accountState.setAnswer(accountDTO.getAnswer());
            accountState.setModifiedBy(loadCurrentAdmin());
            accountFacade.edit(accountState);
        } else {
            throw AccountException.createExceptionWrongState(accountState);
        }
    }

    @RolesAllowed({"Admin", "Reception", "Patient"})
    public void editMyAccount(AccountDTO accountDTO) throws AppBaseException {
        Account tempAccountForPasswordHash = new Account();
        tempAccountForPasswordHash.setPassword(accountDTO.getPassword());

        if (myAccountState.getLogin().equals(accountDTO.getLogin())) {
            if (myAccountState.getPassword().equals(tempAccountForPasswordHash.getPassword())) {
                myAccountState.setName(accountDTO.getName());
                myAccountState.setSurname(accountDTO.getSurname());
                myAccountState.setPhoneNumber(accountDTO.getPhoneNumber());
                myAccountState.setModifiedBy(myAccountState.getClass().getSimpleName().equals("Admin") ? loadCurrentAdmin() : null);
                accountFacade.edit(myAccountState);
            } else {
                throw AccountException.createExceptionWrongPassword(myAccountState);
            }
        } else {
            throw AccountException.createExceptionWrongState(myAccountState);
        }
    }

    @RolesAllowed({"Admin", "Reception", "Patient"})
    public void changeMyPassword(AccountDTO accountDTO) throws AppBaseException {

        if (myAccountState.getLogin().equals(accountDTO.getLogin())) {
            myAccountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());

            Account account = new Account();
            account.setPassword(accountDTO.getOldPassword());
            if ((myAccountState.getPassword().equals(account.getPassword()))) {
                myAccountState.setPassword(accountDTO.getPassword());
                myAccountState.setModifiedBy(myAccountState.getClass().getSimpleName().equals("Admin") ? loadCurrentAdmin() : null);
                accountFacade.edit(myAccountState);
            } else {
                throw AccountException.createExceptionWrongPassword(myAccountState);
            }
        } else {
            throw AccountException.createExceptionWrongState(myAccountState);
        }
    }

    @RolesAllowed({"Admin"})
    public AccountDTO rememberSelectedAccountForPasswordChange(String login) throws AppBaseException {
        accountState = accountFacade.findByLogin(login);
        return new AccountDTO(
                accountState.getLogin()
        );
    }

    @PermitAll
    public AccountDTO rememberSelectedAccountForPasswordResetAndQuestionCheck(String login) throws AppBaseException {
        passwordResetAccountState = accountFacade.findByLogin(login);
        return new AccountDTO(
                passwordResetAccountState.getLogin(),
                passwordResetAccountState.getQuestion(),
                passwordResetAccountState.getAnswer(),
                passwordResetAccountState.isActive()
        );
    }

    @RolesAllowed({"Admin"})
    public AccountDTO rememberSelectedAccountForEdit(String login) throws AppBaseException {
        accountState = accountFacade.findByLogin(login);
        return new AccountDTO(
                accountState.getLogin(),
                accountState.getQuestion(),
                accountState.getAnswer(),
                accountState.getName(),
                accountState.getSurname(),
                accountState.getPhoneNumber()
        );
    }

    @RolesAllowed({"Admin", "Reception", "Patient"})
    public AccountDTO rememberMyAccountForDisplayAndEdit() throws AppBaseException {
        myAccountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        return new AccountDTO(
                myAccountState.getLogin(),
                myAccountState.getPassword(),
                myAccountState.getQuestion(),
                myAccountState.getAnswer(),
                myAccountState.getName(),
                myAccountState.getSurname(),
                myAccountState.getPhoneNumber()
        );
    }

    @RolesAllowed({"Admin", "Reception", "Patient"})
    public AccountDTO rememberMyAccountForPasswordChange() throws AppBaseException {
        myAccountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        return new AccountDTO(
                myAccountState.getLogin(),
                myAccountState.getPassword()
        );
    }

}
