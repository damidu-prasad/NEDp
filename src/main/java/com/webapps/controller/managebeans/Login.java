/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.webapps.filterlogin.beans.NavigationBean;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@SessionScoped
@ViewScoped

public class Login implements Serializable {

    @EJB
    private com.ejb.model.businesslogic.SaveLogin savelogin;
    private com.ejb.model.businesslogic.SaveLogout savelogout;

    private String username;
    private String password;
    private boolean is_error;
    private HttpServletRequest request;

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

    public boolean getIs_error() {
        return is_error;
    }

    public void setIs_error(boolean is_error) {
        this.is_error = is_error;
    }

    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;

    public String saveLogin() {

//        VoucherType vt = new VoucherType();
//        vt.setName("abc");
//        vt.setIdAbbreviation("ccc");
//        uni.create(vt);
//          System.out.println("vt.geti " + vt.getId());
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        ArrayList<String> result_list = savelogin.ValidateUserLogin(username, password, request);

        if (result_list.get(0).equals("OK")) {

            try {

                request.getSession().setAttribute("LS", result_list.get(1));

//                FacesContext.getCurrentInstance().getExternalContext().redirect( );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getNavigationBean().redirectToWelcome();
        } else {

            RequestContext requestContext = RequestContext.getCurrentInstance();

            setIs_error(true);

            requestContext.execute("$('#loginModalMsg').html('" + result_list.get(0) + "')");
            requestContext.execute("PF('login_error').show()");
            return null;
        }

    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public String doLogout() {

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

//        if (request.getSession().getAttribute("LS") != null) {
//            System.out.println("gg "+Integer.parseInt(request.getSession().getAttribute("LS").toString()));
//            savelogout.validateLogout(Integer.parseInt(request.getSession().getAttribute("LS").toString()));
//        }
        request.getSession().invalidate();

        return getNavigationBean().redirectToLogin();
    }

}
