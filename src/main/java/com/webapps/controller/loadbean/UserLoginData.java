/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.loadbean;

import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.OrganizationType;
import com.ejb.model.entity.OrganizationTypeManager;
import com.ejb.model.entity.School;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.SystemInterface;
import com.ejb.model.entity.UserRoleHasSystemInterface;
import com.webapps.controller.managebeans.DataInputScore.SchoolList;
import com.webapps.controller.utilities.SortArraysDataInputScore;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped

public class UserLoginData implements Serializable {

    private String companyName;
    private String companyLogo;
    private String profileName;
    private String userImage;
    private String mainOrgName;
    private boolean isLogged;
    private String currentDateFormat;

    private List<MainInterface> mi_list = new ArrayList();

    HttpServletRequest request;
    HttpServletResponse response;

    private LoginSession ls;

    private boolean checkAdmin = false;

    @EJB
    private UniDBLocal uniDB;

    public String getCurrentDateFormat() {
        return currentDateFormat;
    }

    public void setCurrentDateFormat(String currentDateFormat) {
        this.currentDateFormat = currentDateFormat;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public boolean isIsLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    @PostConstruct
    public void init() {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        ComLib.setHeaders(response);

        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (path.startsWith("/public/")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // You can change the format as needed
            this.currentDateFormat = sdf.format(new Date());
            return;
        } else {

            GetUserLoginData(Integer.parseInt(request.getSession().getAttribute("LS").toString()));
        }

    }

    public void GetUserLoginData(int ls_id) {

        setIsLogged(true);
        System.out.println("lsss " + ls_id + " " + uniDB);
        LoginSession lss = (LoginSession) uniDB.find(ls_id, LoginSession.class);
        if (lss == null) {

            try {
                request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
                System.out.println("nnnn " + origRequest.getContextPath() + "/login.xhtml");
                FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath() + "/login.xhtml");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
//            if (lss.getUserLoginId().getUserRoleId().getUrId() == 2) {
//                checkAdmin = "block";
//            }
//
//            ls = lss;

//            List<OrganizationTypeManager> otm = uniDB.searchByQuerySingle("SELECT im FROM OrganizationTypeManager im WHERE im.generalOrganizationProfileId1.id='" + lss.getGeneralOrganizationProfileId().getId() + "' ");
//            if (otm.size() > 0) {
//                mainOrgName = otm.iterator().next().getGeneralOrganizationProfileId().getName();
//            }
            mainOrgName = "National Education Development Portal";

            companyName = lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getName();

            String pn = lss.getUserLoginGroupId().getUserLoginId().getGeneralUserProfileId().getNameWithIn();
            if (pn.length() < 3) {

                pn = lss.getUserLoginId().getGeneralUserProfileId().getFirstName() + " " + lss.getUserLoginId().getGeneralUserProfileId().getLastName();
            }

            profileName = pn;
            if (lss.getUserLoginGroupId().getUserRoleId().getId() == 1) {
                setCheckAdmin(true);
            }

            setLeftMenu(lss.getUserLoginGroupId().getUserRoleId().getId());
        }
    }

    public void setLeftMenu(int userRole) {

        String query1 = "SELECT g FROM UserRoleHasSystemInterface g where g.userRoleId.id='" + userRole + "'  group by g.systemInterfaceId.interfaceMenuId.id order by g.systemInterfaceId.interfaceMenuId.id ASC";
        List<UserRoleHasSystemInterface> listIM = uniDB.searchByQuery(query1);
        for (UserRoleHasSystemInterface cc_im : listIM) {

            List<SubInterface> subi = new ArrayList();

            String query2 = "SELECT g FROM UserRoleHasSystemInterface g where g.userRoleId.id='" + userRole + "' and g.systemInterfaceId.interfaceMenuId.id='" + cc_im.getSystemInterfaceId().getInterfaceMenuId().getId() + "'  group by g.systemInterfaceId.interfaceSubMenuId.id order by g.systemInterfaceId.interfaceSubMenuId.id ASC";
            List<UserRoleHasSystemInterface> listISM = uniDB.searchByQuery(query2);
            for (UserRoleHasSystemInterface cc_ism : listISM) {

                List<InterfaceList> il = new ArrayList();

                String query3 = "SELECT g FROM UserRoleHasSystemInterface g where g.userRoleId.id='" + userRole + "' and g.systemInterfaceId.interfaceMenuId.id='" + cc_im.getSystemInterfaceId().getInterfaceMenuId().getId() + "' and  g.systemInterfaceId.interfaceSubMenuId.id='" + cc_ism.getSystemInterfaceId().getInterfaceSubMenuId().getId() + "' order by g.systemInterfaceId.id ASC";
                List<UserRoleHasSystemInterface> listI = uniDB.searchByQuery(query3);
                for (UserRoleHasSystemInterface cc_i : listI) {
                    il.add(new InterfaceList(cc_i.getSystemInterfaceId().getDisplayName(), cc_i.getSystemInterfaceId().getIcon(), cc_i.getSystemInterfaceId().getUrl()));
                }

                subi.add(new SubInterface(cc_ism.getSystemInterfaceId().getInterfaceSubMenuId().getName(), cc_ism.getSystemInterfaceId().getInterfaceSubMenuId().getIcon(), il));

            }

            this.mi_list.add(new MainInterface(cc_im.getSystemInterfaceId().getInterfaceMenuId().getMenuName(), cc_im.getSystemInterfaceId().getInterfaceMenuId().getIcon(), subi));

        }
    }

    public LoginSession getLs() {
        return ls;
    }

    public void setLs(LoginSession ls) {
        this.ls = ls;
    }

    public List<MainInterface> getMi_list() {
        return mi_list;
    }

    public void setMi_list(List<MainInterface> mi_list) {
        this.mi_list = mi_list;
    }

    public String getMainOrgName() {
        return mainOrgName;
    }

    public void setMainOrgName(String mainOrgName) {
        this.mainOrgName = mainOrgName;
    }

    public boolean isCheckAdmin() {
        return checkAdmin;
    }

    public void setCheckAdmin(boolean checkAdmin) {
        this.checkAdmin = checkAdmin;
    }

    public class MainInterface implements Serializable {

        private String menuname;
        private String menuIcon;
        private List<SubInterface> subInterfaceList;

        public MainInterface(String menuname, String menuIcon, List<SubInterface> subInterfaceList) {
            this.menuIcon = menuIcon;
            this.menuname = menuname;
            this.subInterfaceList = subInterfaceList;
        }

        public String getMenuname() {
            return menuname;
        }

        public void setMenuname(String menuname) {
            this.menuname = menuname;
        }

        public String getMenuIcon() {
            return menuIcon;
        }

        public void setMenuIcon(String menuIcon) {
            this.menuIcon = menuIcon;
        }

        public List<SubInterface> getSubInterfaceList() {
            return subInterfaceList;
        }

        public void setSubInterfaceList(List<SubInterface> subInterfaceList) {
            this.subInterfaceList = subInterfaceList;
        }

    }

//    public static class SchoolList implements Serializable {
//
//        private int no;
//        private String school_name;
//        private double val;
//        private String value;
//
//        public SchoolList() {
//        }
//
//        public SchoolList(int no, String school_name, double val, String value) {
//            this.no = no;
//            this.school_name = school_name;
//            this.val = val;
//            this.value = value;
//
//        }
//
//        public int getNo() {
//            return no;
//        }
//
//        public void setNo(int no) {
//            this.no = no;
//        }
//
//        public String getSchool_name() {
//            return school_name;
//        }
//
//        public void setSchool_name(String school_name) {
//            this.school_name = school_name;
//        }
//
//        public double getVal() {
//            return val;
//        }
//
//        public void setVal(double val) {
//            this.val = val;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//
//    }
    public class SubInterface implements Serializable {

        private String menuname;
        private String menuIcon;

        private List<InterfaceList> interfaceList;

        public SubInterface(String menuname, String menuIcon, List<InterfaceList> interfaceList) {
            this.menuIcon = menuIcon;
            this.menuname = menuname;
            this.interfaceList = interfaceList;
        }

        public String getMenuname() {
            return menuname;
        }

        public void setMenuname(String menuname) {
            this.menuname = menuname;
        }

        public String getMenuIcon() {
            return menuIcon;
        }

        public void setMenuIcon(String menuIcon) {
            this.menuIcon = menuIcon;
        }

        public List<InterfaceList> getInterfaceList() {
            return interfaceList;
        }

        public void setInterfaceList(List<InterfaceList> interfaceList) {
            this.interfaceList = interfaceList;
        }

    }

    public class InterfaceList implements Serializable {

        private String menuname;
        private String menuIcon;
        private String menuUrl;

        public InterfaceList(String menuname, String menuIcon, String menuUrl) {
            this.menuIcon = menuIcon;
            this.menuname = menuname;
            this.menuUrl = menuUrl;
        }

        public String getMenuname() {
            return menuname;
        }

        public void setMenuname(String menuname) {
            this.menuname = menuname;
        }

        public String getMenuIcon() {
            return menuIcon;
        }

        public void setMenuIcon(String menuIcon) {
            this.menuIcon = menuIcon;
        }

        public String getMenuUrl() {
            return menuUrl;
        }

        public void setMenuUrl(String menuUrl) {
            this.menuUrl = menuUrl;
        }

    }

}
