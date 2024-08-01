/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.OrgSystemInterfaceController;
import com.ejb.model.entity.UserRoleHasSystemInterface;
import com.webapps.filterlogin.beans.NavigationBean;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped
public class PrincipalDashboard implements Serializable {

    HttpServletRequest request;
    HttpServletResponse response;

    private LoginSession ls;

    @EJB
    private ComDev comDiv;

    @EJB
    private UniDBLocal uniDB;

    @PostConstruct
    public void init() {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        ls = (LoginSession) uniDB.find(Integer.parseInt(request.getSession().getAttribute("LS").toString()), LoginSession.class);

        
    }

}
