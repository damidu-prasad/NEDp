package com.webapps.filterlogin.beans;

import com.ejb.model.businesslogic.SaveLogin;
import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.Security;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.UserLogin;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 * Simple login bean.
 *
 * @author itcuties
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private UniDBLocal uniDB;

    @EJB
    private SaveLogin login;

    @EJB
    private ComLib comLib;

    @EJB
    private ComDev comDev;

    private static final long serialVersionUID = 7765876811740798583L;

    private String username;
    private String password;
    private int loginsession;
    private String date;
    private String order_no;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getLoginsession() {
        return loginsession;
    }

    public void setLoginsession(int loginsession) {
        this.loginsession = loginsession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private String name;

    private boolean loggedIn;

    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;

    /**
     * Login operation.
     *
     * @return
     */
    public String doLogin() {
        // Get every user from our sample database :)
//        for (String user : users) {
//            String dbUsername = user.split(":")[0];
//            String dbPassword = user.split(":")[1];

        // Successful login
//        if (login.ValidateUserLogin(username, password)) {
//            loggedIn = true;
//
//            List<UserLogin> ul = uniDB.searchByQuery("SELECT u FROM UserLogin u WHERE u.username='" + username + "'");
//
//            boolean b = false;
//
//            UserLogin userLogin = null;
//
//            for (UserLogin userL : ul) {
//
//                String pass = "";
//                try {
//                    pass = Security.decrypt(userL.getPassword());
//                } catch (Exception e) {
//                }
//
//                b = ((pass.equals(password)) ? true : false);
//
//                if (b) {
//                    userLogin = userL;
//                }
//            }
//
//            setName(userLogin.getGeneralUserProfileId().getFirstName());
//
//            LoginSession ls = new LoginSession();
//            ls.setGeneralOrganizationProfileId(userLogin.getGeneralOrganizationProfileId());
//            ls.setUserLoginId(userLogin);
//            ls.setStartTime(new Date());
//            uniDB.create(ls);
//
//            setLoginsession(ls.getId());
//
//            date = ComLib.getDate(new Date());
//            order_no = comLib.VIDGenerator(1, 91);
//
//            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//            request.getSession().setAttribute("loginsession", ls);
//            request.getSession().setAttribute("userloin", userLogin);
//
//            return navigationBean.redirectToWelcome();
//        }
//        }

        // Set login ERROR
//		FacesMessage msg = new FacesMessage("Login error!", "ERROR MSG");
//        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext contex = RequestContext.getCurrentInstance();
        contex.execute("swal('Username or Password Invalid!','Login Faild!','error')");

        // To to login page
        return navigationBean.toLogin();

    }

    /**
     * Logout operation.
     *
     * @return
     */
    public void doLogout() throws IOException {
        // Set the paremeter indicating that user is logged in to false
        loggedIn = false;

        // Set logout message
//		FacesMessage msg = new FacesMessage("Logout success!", "INFO MSG");
//		msg.setSeverity(FacesMessage.SEVERITY_INFO);
//		FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext contex = RequestContext.getCurrentInstance();
        contex.execute("swal('Logout success!','','success')");

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect("../login.xhtml");
        //    return navigationBean.toLogin();
    }

    // ------------------------------
    // Getters & Setters 
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

}
