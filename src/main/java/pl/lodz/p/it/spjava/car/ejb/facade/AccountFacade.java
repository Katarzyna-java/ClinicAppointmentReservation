/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.spjava.car.exception.AccountException;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.model.Account;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {

    static final public String DB_UNIQUE_CONSTRAINT_ACCOUNT_LOGIN = "UNIQUE_LOGIN";
    static final public String DB_FK_SPECIALIZATION_SPECIALIZATION_CREATOR = "SPCLZTNSPCLZTNCRTR";
    static final public String DB_FK_SPECIALIZATION_MODIFIED_BY = "SPCLIZATIONMDFEDBY";
    static final public String DB_FK_SPECIALIST_SPECIALIST_CREATOR = "SPCLSTSPCLSTCRATOR";
    static final public String DB_FK_SPECIALIST_MODIFIED_BY = "SPCIALISTMDIFIEDBY";
    static final public String DB_FK_ACCOUNT_MODIFIED_BY = "ACCOUNTMODIFIED_BY";
    static final public String DB_FK_ACCOUNT_ACCOUNT_CREATOR = "CCUNTCCOUNTCREATOR";
    static final public String DB_FK_RESERVATION_MODIFIED_BY = "RSRVATIONMDIFIEDBY";
    static final public String DB_FK_RESERVATION_RESERVATION_CREATOR = "RSRVTNRSRVTNCRATOR";

    @PersistenceContext(unitName = "CAR_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    @PermitAll
    @Override
    public void create(Account entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_ACCOUNT_LOGIN)) {
                throw AccountException.createExceptionLoginAlreadyExists(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @PermitAll
    @Override
    public void edit(Account entity) throws AppBaseException {
        try {
            super.edit(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (OptimisticLockException e) {
            throw AppBaseException.createExceptionOptimisticLock(e);
        } catch (PersistenceException e) {
            throw AppBaseException.createExceptionDatabaseQueryProblem(e);
        }
    }

    @RolesAllowed({"Admin"})
    @Override
    public void remove(Account entity) throws AppBaseException {
        try {
            super.remove(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (OptimisticLockException e) {
            throw AppBaseException.createExceptionOptimisticLock(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && (cause.getMessage().contains(DB_FK_SPECIALIZATION_SPECIALIZATION_CREATOR) || cause.getMessage().contains(DB_FK_SPECIALIZATION_MODIFIED_BY))) {
                throw AccountException.createExceptionAccountInUseInSpecialization(e, entity);
            }
            if (cause instanceof DatabaseException && (cause.getMessage().contains(DB_FK_SPECIALIST_SPECIALIST_CREATOR) || cause.getMessage().contains(DB_FK_SPECIALIST_MODIFIED_BY))) {
                throw AccountException.createExceptionAccountInUseInSpecialist(e, entity);
            }
            if (cause instanceof DatabaseException && (cause.getMessage().contains(DB_FK_ACCOUNT_ACCOUNT_CREATOR) || cause.getMessage().contains(DB_FK_ACCOUNT_MODIFIED_BY))) {
                throw AccountException.createExceptionAccountInUseInAccount(e, entity);
            }
            if (cause instanceof DatabaseException && (cause.getMessage().contains(DB_FK_RESERVATION_RESERVATION_CREATOR) || cause.getMessage().contains(DB_FK_RESERVATION_MODIFIED_BY))) {
                throw AccountException.createExceptionAccountInUseInReservation(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @PermitAll
    public Account findByLogin(String login) throws AppBaseException {
        TypedQuery<Account> tq = em.createNamedQuery("Account.findByLogin", Account.class);
        tq.setParameter("login", login);
        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            throw AccountException.createExceptionNoAccountFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Admin"})
    public List<Account> findNewAccount() throws AppBaseException {
        TypedQuery<Account> tq = em.createNamedQuery("Account.findNewAccount", Account.class);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Admin"})
    public List<Account> findAuthorizedAccount() throws AppBaseException {
        TypedQuery<Account> tq = em.createNamedQuery("Account.findAuthorizedAccount", Account.class);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof DatabaseException && e.getCause().getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

}
