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

@Named(value = "resetPassword2PageBean")
@RequestScoped
public class ResetPassword2PageBean implements Serializable {

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    private String myAnswer;

    public ResetPassword2PageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public String getMyAnswer() {
        return myAnswer;
    }

    public void setMyAnswer(String myAnswer) {
        this.myAnswer = myAnswer;
    }

    @PostConstruct
    public void init() {
        accountDTO = accountControllerBean.getQuestionCheckAccountDTO();
    }

    public String resetPassword2Action() throws AppBaseException {
        if (accountDTO == null) {
            ContextUtils.emitI18NMessage(null, "error.link.forced");
            ContextUtils.getContext().getFlash().setKeepMessages(true);
            return "main";
        }
        if (!myAnswer.equals(accountDTO.getAnswer())) {
            ContextUtils.emitI18NMessage("ResetPassword2Form:answer", "answer.not.matching");
            return null;
        }
        try {
            accountControllerBean.selectAccountForPasswordReset(accountDTO);
            if (accountDTO == null || !accountDTO.getLogin().equals(accountControllerBean.getPasswordResetAccountDTO().getLogin())) {
                ContextUtils.emitI18NMessage("ResetPassword2Form:login", "error.login.not.matching");
                return null;
            }
        } catch (AppBaseException ex) {
            Logger.getLogger(ResetPassword2PageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            accountControllerBean.setPasswordResetAccountDTO(null);
        }
        return "resetPassword3";
    }

}
