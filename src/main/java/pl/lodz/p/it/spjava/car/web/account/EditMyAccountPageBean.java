/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.account;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.dto.AccountDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "editMyAccountPageBean")
@ViewScoped
public class EditMyAccountPageBean implements Serializable {

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    public EditMyAccountPageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    @PostConstruct
    public void init() {
        try {
            accountDTO = accountControllerBean.getMyAccountDTOForEdit();
        } catch (AppBaseException ex) {
            Logger.getLogger(EditMyAccountPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        accountControllerBean.getMyAccountDTO().setPassword("");
    }

    public String editMyAccountAction() {
        try {
            accountControllerBean.editMyAccount(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(EditMyAccountPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            ContextUtils.getContext().getFlash().setKeepMessages(true);
            if (ex.getMessage().equals("error.optimistic.lock.problem")) {
                return "displayMyAccount";
            }
            return null;
        }
        return "displayMyAccount";
    }
}
