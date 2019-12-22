/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.account;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.dto.AccountDTO;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "changePasswordPageBean")
@RequestScoped
public class ChangePasswordPageBean {

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    private String repeatNewPassword;

    public ChangePasswordPageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }

    @PostConstruct
    public void init() {
        accountDTO = accountControllerBean.getSelectedAccountDTO();
    }

    public String saveChangedPasswordAction() {

        if (repeatNewPassword.equals(accountDTO.getPassword())) {
            try {
                accountControllerBean.changeAccountPassword(accountDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(EditAccountPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
                return "listAuthorizedAccounts";
            }
        } else {
            ContextUtils.emitI18NMessage("ChangeAnyPasswordForm:repeatNewPassword", "passwords.not.matching");
            return null;
        }
        return "listAuthorizedAccounts";
    }
}
