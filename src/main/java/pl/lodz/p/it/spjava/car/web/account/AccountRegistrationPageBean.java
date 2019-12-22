/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.account;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.dto.AccountDTO;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "accountRegistrationPageBean")
@RequestScoped
public class AccountRegistrationPageBean implements Serializable {

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    private String passwordRepeat;

    public AccountRegistrationPageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    @PostConstruct
    public void init() {
        accountDTO = new AccountDTO();

    }

    public String registerAccountAction() {
        if (passwordRepeat.equals(accountDTO.getPassword())) {
            try {
                accountControllerBean.registerAccount(accountDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(AccountRegistrationPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
                return null;
            }
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:passwordRepeat", "passwords.not.matching");
            return null;
        }
        return "main";
    }

}
