/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.common.ComLib;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.LoginSession;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped

public class CheckUserPrivilege implements Serializable {

    static HttpServletRequest request;
    HttpServletResponse response;

    @EJB
    public static UniDBLocal uniDB;

    @PostConstruct
    public void init() {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

    }

    public static String checkUser(String si_id, int ls_id,UniDBLocal uniDB1,ExternalContext ext) {
        
        LoginSession lss = (LoginSession) uniDB1.find(ls_id, LoginSession.class);
        if (lss == null) {
            try {

                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();

                FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath() + "/login.xhtml");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (!ComLib.CheckUserPrivilage(uniDB1, lss.getUserLoginGroupId().getUserRoleId().getId(), si_id)) {

//                    return "/main-pages/dashboard.xhtml?faces-redirect=true";
                try {
//                    FacesContext context = FacesContext.getCurrentInstance();
//                    HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
//                    System.out.println("conte "+context);
//                    context.getExternalContext().redirect(origRequest.getContextPath() + "/error-pages/access-denied.xhtml");

//ext.

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
