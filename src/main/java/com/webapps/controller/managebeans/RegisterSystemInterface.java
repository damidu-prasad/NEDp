/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.ComPath;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.InterfaceMenu;
import com.ejb.model.entity.InterfaceSubMenu;
import com.ejb.model.entity.LoginSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped

public class RegisterSystemInterface implements Serializable {

    private String interfaceMenu;
    private String interfaceSubMenu;
    private String interfaceName;
    private String displayName;
    private String url;
    private String displayIcon = "star";
    private String description = "";

    private List<SelectItem> interfaceMenuList = new ArrayList<SelectItem>();
    private List<SelectItem> interfaceSubMenuList = new ArrayList<SelectItem>();

    HttpServletResponse response;
    HttpServletRequest request;

    private ComLib comlib;
    private ComPath comPath;

    @EJB
    private ComDev comDiv;

    @EJB
    UniDBLocal uni;

    @PostConstruct
    public void init() {

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        System.out.println("bb " + response.isCommitted());
        initializeData();
    }

    public void initializeData() {

        // Get Menu
        getInterfaceMenuList().add(new SelectItem("", "Select"));

        String query = "SELECT g FROM InterfaceMenu g ";
        List<InterfaceMenu> listAS = uni.searchByQuery(query);
        for (InterfaceMenu cc : listAS) {

            getInterfaceMenuList().add(new SelectItem(cc.getId(), cc.getMenuName()));
        }
        // Get Sub Menu
        getInterfaceSubMenuList().add(new SelectItem("", "Select"));

        String query_sub = "SELECT g FROM InterfaceSubMenu g ";
        List<InterfaceSubMenu> listAS_sub = uni.searchByQuery(query_sub);
        for (InterfaceSubMenu cc : listAS_sub) {

            getInterfaceSubMenuList().add(new SelectItem(cc.getId(), cc.getName()));
        }

        LoginSession lss = (LoginSession) uni.find(Integer.parseInt(request.getSession().getAttribute("LS").toString()), LoginSession.class);
        if (lss != null) {
            if (!comlib.CheckUserPrivilage(uni, 1, "Register System Interface")) {
                try {

                    FacesContext context = FacesContext.getCurrentInstance();
                    HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
                    System.out.println("conte " + context);
                    context.getExternalContext().redirect(origRequest.getContextPath() + "/error-pages/access-denied.xhtml");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

//        CheckUserPrivilege.checkUser("Register System Interface", Integer.parseInt(request.getSession().getAttribute("LS").toString()),uni,FacesContext.getCurrentInstance());
    }

    public String SaveRegisterSystemInterface() {

        FacesMessage msg = null;
        if (interfaceMenu.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Interface Menu !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (interfaceSubMenu.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Interface Sub Menu !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (interfaceName.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Enter Interface Name !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (displayName.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Enter Display Name !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (url.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Enter Url !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (displayIcon.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Enter Display Icon !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {

            boolean result = comDiv.saveRegisterSystemInterface(this.interfaceMenu, this.interfaceSubMenu, this.interfaceName, this.displayName, this.url, this.displayIcon, this.description);

            if (result == true) {

                this.interfaceMenu = "";
                this.interfaceSubMenu = "";
                this.interfaceName = "";
                this.displayName = "";
                this.url = "";
                this.displayIcon = "";

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully Saved !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Duplicate Record Found !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            }

        }

        return null;
    }

    public void changeIcon() {
        if (interfaceSubMenu.equals("1")) {
            displayIcon = "star";
        } else if (interfaceSubMenu.equals("2")) {
            displayIcon = "link";
        } else if (interfaceSubMenu.equals("3")) {
            displayIcon = "list";
        }

    }

    public String getInterfaceMenu() {
        return interfaceMenu;
    }

    public void setInterfaceMenu(String interfaceMenu) {
        this.interfaceMenu = interfaceMenu;
    }

    public String getInterfaceSubMenu() {
        return interfaceSubMenu;
    }

    public void setInterfaceSubMenu(String interfaceSubMenu) {
        this.interfaceSubMenu = interfaceSubMenu;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayIcon() {
        return displayIcon;
    }

    public void setDisplayIcon(String displayIcon) {
        this.displayIcon = displayIcon;
    }

    public List<SelectItem> getInterfaceMenuList() {
        return interfaceMenuList;
    }

    public void setInterfaceMenuList(List<SelectItem> interfaceMenuList) {
        this.interfaceMenuList = interfaceMenuList;
    }

    public List<SelectItem> getInterfaceSubMenuList() {
        return interfaceSubMenuList;
    }

    public void setInterfaceSubMenuList(List<SelectItem> interfaceSubMenuList) {
        this.interfaceSubMenuList = interfaceSubMenuList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
