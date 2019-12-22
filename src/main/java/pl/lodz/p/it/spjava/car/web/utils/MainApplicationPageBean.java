/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import pl.lodz.p.it.spjava.car.dto.AccountDTO;

@Named(value = "mainApplicationBean")
@ApplicationScoped
public class MainApplicationPageBean {

    public MainApplicationPageBean() {
    }

    AccountDTO accountDTO = new AccountDTO();

    public String logOutAction() {
        ContextUtils.invalidateSession();
        return "main";
    }

    public String getMyLogin() {
        return ContextUtils.getUserName();
    }
}
