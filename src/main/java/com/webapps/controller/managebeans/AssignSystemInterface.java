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
import com.ejb.model.entity.OrgSystemInterfaceController;
import com.ejb.model.entity.SystemInterface;
import com.ejb.model.entity.UserRole;
import com.ejb.model.entity.UserRoleHasSystemInterface;
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

public class AssignSystemInterface implements Serializable {

    private String userRoleName;
    private String systemInterfaceName;

    private List<ExisInterfaces> existInterfaceList = new ArrayList<ExisInterfaces>();

    private List<SelectItem> userRoleNameList = new ArrayList<SelectItem>();
    private List<SelectItem> systemInterfaceNameList = new ArrayList<SelectItem>();

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

        initializeData();
    }

    public void initializeData() {

        // Get Menu
        getUserRoleNameList().add(new SelectItem("", "Select"));

        String query = "SELECT g FROM UserRole g order by g.roleName ASC";
        List<UserRole> listAS = uni.searchByQuery(query);
        for (UserRole cc : listAS) {

            getUserRoleNameList().add(new SelectItem(cc.getId(), cc.getRoleName()));
        }
        // Get Sub Menu
        getSystemInterfaceNameList().add(new SelectItem("", "Select"));

        String orgSysIds = "";
        int p = 0;
        String query0 = "SELECT g FROM OrgSystemInterfaceController g where g.generalOrganizationProfileId.id='1' and  g.projectsId.id='1' ";
        List<OrgSystemInterfaceController> listIM0 = uni.searchByQuery(query0);
        for (OrgSystemInterfaceController o : listIM0) {
            if (p == 0) {
                orgSysIds = o.getSystemInterfaceId().getId() + "";
                p++;
            } else {
                orgSysIds += "," + o.getSystemInterfaceId().getId();

            }
        }
        if (listIM0.size() > 0) {
            String query_sub = "SELECT g FROM SystemInterface g where g.id in (" + orgSysIds + ") order by g.interfaceMenuId.menuName ASC";
            List<SystemInterface> listAS_sub = uni.searchByQuery(query_sub);
            if (listAS_sub.size() > 0) {
                for (SystemInterface cc : listAS_sub) {

                    getSystemInterfaceNameList().add(new SelectItem(cc.getId(), cc.getInterfaceMenuId().getMenuName() + " - " + cc.getInterfaceSubMenuId().getName() + " - " + cc.getInterfaceName()));
                }
            }
        }
    }

    public String saveAssignSystemInterface() {

        FacesMessage msg = null;
        if (this.userRoleName.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select User Role !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (this.systemInterfaceName.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select System Interface !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {

            boolean result = comDiv.saveAssignSystemInterface(Integer.parseInt(this.systemInterfaceName), Integer.parseInt(this.userRoleName));

            if (result == true) {

                loadExistingInterfaces();

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully Saved !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Duplicate Record Found !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            }

        }
        return null;
    }

    public void loadExistingInterfaces() {
        System.out.println("awa");
        if (this.userRoleName.equals("")) {
            this.existInterfaceList.clear();
        } else {

            this.existInterfaceList = new ArrayList();

            String orgSysIds = "";
            int p = 0;
            String query0 = "SELECT g FROM OrgSystemInterfaceController g where g.generalOrganizationProfileId.id='1' and  g.projectsId.id='1' ";
            List<OrgSystemInterfaceController> listIM0 = uni.searchByQuery(query0);
            for (OrgSystemInterfaceController o : listIM0) {
                if (p == 0) {
                    orgSysIds = o.getSystemInterfaceId().getId() + "";
                    p++;
                } else {
                    orgSysIds += "," + o.getSystemInterfaceId().getId();

                }
            }
            if (listIM0.size() > 0) {

                String query = "SELECT g FROM UserRoleHasSystemInterface g where g.userRoleId.id='" + this.userRoleName + "' and g.systemInterfaceId.id in (" + orgSysIds + ") order by g.systemInterfaceId.interfaceMenuId.menuName ASC";
                List<UserRoleHasSystemInterface> listAS = uni.searchByQuery(query);

                int i = 1;
                for (UserRoleHasSystemInterface cc : listAS) {

                    this.existInterfaceList.add(new ExisInterfaces(i, cc.getSystemInterfaceId().getInterfaceMenuId().getMenuName(), cc.getSystemInterfaceId().getInterfaceSubMenuId().getName(), cc.getSystemInterfaceId().getInterfaceName()));
                    i++;
                }
            }
        }

    }

    public class ExisInterfaces implements Serializable {

        private int no;
        private String interfaceMenuName;
        private String interfaceSubMenuName;
        private String interfaceName;

        public ExisInterfaces() {
        }

        public ExisInterfaces(int no, String interfaceMenuName, String interfaceSubMenuName, String interfaceName) {
            this.no = no;
            this.interfaceMenuName = interfaceMenuName;
            this.interfaceSubMenuName = interfaceSubMenuName;
            this.interfaceName = interfaceName;

        }

        public String getInterfaceMenuName() {
            return interfaceMenuName;
        }

        public void setInterfaceMenuName(String interfaceMenuName) {
            this.interfaceMenuName = interfaceMenuName;
        }

        public String getInterfaceSubMenuName() {
            return interfaceSubMenuName;
        }

        public void setInterfaceSubMenuName(String interfaceSubMenuName) {
            this.interfaceSubMenuName = interfaceSubMenuName;
        }

        public String getInterfaceName() {
            return interfaceName;
        }

        public void setInterfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getSystemInterfaceName() {
        return systemInterfaceName;
    }

    public void setSystemInterfaceName(String systemInterfaceName) {
        this.systemInterfaceName = systemInterfaceName;
    }

    public List<SelectItem> getUserRoleNameList() {
        return userRoleNameList;
    }

    public void setUserRoleNameList(List<SelectItem> userRoleNameList) {
        this.userRoleNameList = userRoleNameList;
    }

    public List<SelectItem> getSystemInterfaceNameList() {
        return systemInterfaceNameList;
    }

    public void setSystemInterfaceNameList(List<SelectItem> systemInterfaceNameList) {
        this.systemInterfaceNameList = systemInterfaceNameList;
    }

    public List<ExisInterfaces> getExistInterfaceList() {
        return existInterfaceList;
    }

    public void setExistInterfaceList(List<ExisInterfaces> existInterfaceList) {
        this.existInterfaceList = existInterfaceList;
    }

}
