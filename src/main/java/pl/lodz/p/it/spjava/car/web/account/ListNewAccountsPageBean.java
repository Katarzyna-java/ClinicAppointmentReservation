/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.spjava.car.model.AccessLevel;
import pl.lodz.p.it.spjava.car.dto.AccountDTO;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "listNewAccountsPageBean")
@ViewScoped
public class ListNewAccountsPageBean implements Serializable {

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountControllerBean accountControllerBean;

    private List<AccountDTO> listAccounts;

    private List<AccessLevel> listAccessLevels;

    public List<AccessLevel> getListAccessLevels() {
        return listAccessLevels;
    }

    private DataModel<AccountDTO> dataModelAccounts;

    public ListNewAccountsPageBean() {
    }

    public DataModel<AccountDTO> getDataModelAccounts() {
        return dataModelAccounts;
    }

    @PostConstruct
    public void initListNewAccounts() {
        try {
            listAccounts = accountControllerBean.listNewAccounts();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListNewAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelAccounts = new ListDataModel<>(listAccounts);

        AccessLevel[] listAllAccessLevels = AccessLevel.values();
        for (AccessLevel accessLevel : listAllAccessLevels) {
            accessLevel.setAccessLevelI18NValue(ContextUtils.getI18NMessage(accessLevel.getAccessLevelKey()));
        }

        listAccessLevels = new ArrayList<>(Arrays.asList(listAllAccessLevels));
        listAccessLevels.remove(AccessLevel.ACCOUNT);
        listAccessLevels.remove(AccessLevel.NEWACCOUNT);
    }

    public String deleteSelectedAccountAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.deleteAccount(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListNewAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListNewAccounts();
        return null;
    }

    public String changeAccessLevelSelectedAccountAction(AccountDTO accountDTO) {
        if (accountDTO.getAccessLevel() != null) {
            try {
                accountControllerBean.changeAccessLevelAccount(accountDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(ListNewAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
            }
            initListNewAccounts();
        }
        return null;
    }
}
