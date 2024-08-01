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
public class ControlPanel implements Serializable {

    HttpServletRequest request;
    HttpServletResponse response;

    private List<MainInterface> mi_list = new ArrayList();

    private LoginSession ls;

    private StreamedContent file;

    private String nameWithIn = "  ";
    private String mobileNo;
    private String email;
    private String address1 = " ";
    private String address2;
    private String address3;

    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;

    @EJB
    private ComDev comDiv;

    private String colors[] = {"#8bc34a", "#ffc107", "#ff5722", "#e91e63", "#259b24", "#00bcd4", "#ff9800", "#e51c23", "#9c27b0", "#3f51b5", "#03a9f4", "#9e9e9e", "#607d8b", "#673ab7", "#5677fc", "#009688", "#795548", "#212121"};

    @EJB
    private UniDBLocal uniDB;

    @PostConstruct
    public void init() {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        ComLib.setHeaders(response);

        setLeftMenu(Integer.parseInt(request.getSession().getAttribute("LS").toString()));

        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/NEDP.apk");
        file = new DefaultStreamedContent(stream, "application/vnd.apk", "NEDP.apk");

        ls = (LoginSession) uniDB.find(Integer.parseInt(request.getSession().getAttribute("LS").toString()), LoginSession.class);

        boolean check = true;

        
        if (ls.getUserLoginId().getGeneralUserProfileId().getNameWithIn() == null) {
            check = false;
        } else {
            if (ls.getUserLoginId().getGeneralUserProfileId().getNameWithIn().trim().equals("")) {
                check = false;
            } else {

                nameWithIn = ls.getUserLoginId().getGeneralUserProfileId().getNameWithIn().trim();
            }
        }
        if (ls.getUserLoginId().getGeneralUserProfileId().getMobilePhone() == null) {
            check = false;
        } else {
            if (ls.getUserLoginId().getGeneralUserProfileId().getMobilePhone().equals("")) {
                check = false;
            } else {

                mobileNo = ls.getUserLoginId().getGeneralUserProfileId().getMobilePhone();
            }
        }
        address1 = ls.getUserLoginId().getGeneralUserProfileId().getAddress1();
        if (ls.getUserLoginId().getGeneralUserProfileId().getAddress2().equals("") || ls.getUserLoginId().getGeneralUserProfileId().getAddress2().equals(" ")) {
            check = false;
        } else {

            address2 = ls.getUserLoginId().getGeneralUserProfileId().getAddress2();
        }
        if (ls.getUserLoginId().getGeneralUserProfileId().getAddress3().equals("") || ls.getUserLoginId().getGeneralUserProfileId().getAddress3().equals(" ")) {
            check = false;
        } else {
            address3 = ls.getUserLoginId().getGeneralUserProfileId().getAddress3();
        }
        if (ls.getUserLoginId().getGeneralUserProfileId().getEmail() == null) {
            check = false;
        } else {
            if (ls.getUserLoginId().getGeneralUserProfileId().getEmail().equals("") || ls.getUserLoginId().getGeneralUserProfileId().getEmail().equals(" ")) {
                check = false;
            } else {

                email = ls.getUserLoginId().getGeneralUserProfileId().getEmail();
            }
        }

        if (check == false) {

            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('CheckContactDetailsDLG').show()");
        }

    }

    public void setLeftMenu(int ls_id) {

        LoginSession lss = (LoginSession) uniDB.find(ls_id, LoginSession.class);
        int userRole = lss.getUserLoginGroupId().getUserRoleId().getId();
        int i = 0;

        String orgSysIds = "";
        int p = 0;
        String query0 = "SELECT g FROM OrgSystemInterfaceController g where g.generalOrganizationProfileId.id='1' and  g.projectsId.id='1' ";
        List<OrgSystemInterfaceController> listIM0 = uniDB.searchByQuery(query0);
        for (OrgSystemInterfaceController o : listIM0) {
            if (p == 0) {
                orgSysIds = o.getSystemInterfaceId().getId() + "";
                p++;
            } else {
                orgSysIds += "," + o.getSystemInterfaceId().getId();

            }
        }
         System.out.println("awa0"+orgSysIds);
        if (listIM0.size() > 0) {
             System.out.println("awa00"+userRole);
            String query1 = "SELECT g FROM UserRoleHasSystemInterface g where g.userRoleId.id='" + userRole + "' and g.systemInterfaceId.id in (" + orgSysIds + ") group by g.systemInterfaceId.interfaceMenuId.id order by g.systemInterfaceId.interfaceMenuId.id ASC";
            List<UserRoleHasSystemInterface> listIM = uniDB.searchByQuery(query1);
            for (UserRoleHasSystemInterface cc_im : listIM) {
                System.out.println("awa1");
                List<InterfaceList> il = new ArrayList();

                String query3 = "SELECT g FROM UserRoleHasSystemInterface g where g.userRoleId.id='" + userRole + "' and g.systemInterfaceId.id in (" + orgSysIds + ") and g.systemInterfaceId.interfaceMenuId.id='" + cc_im.getSystemInterfaceId().getInterfaceMenuId().getId() + "'  order by g.systemInterfaceId.interfaceSubMenuId.id ASC";
                List<UserRoleHasSystemInterface> listI = uniDB.searchByQuery(query3);
                for (UserRoleHasSystemInterface cc_i : listI) {

                    if (i > 17) {

                        i = 0;
                    }
 System.out.println("awa2");
                    String color = getColors()[i];

                    il.add(new InterfaceList(cc_i.getSystemInterfaceId().getDisplayName(), cc_i.getSystemInterfaceId().getUrl(), color, cc_i.getSystemInterfaceId().getDescription()));

                    i++;
                }

                this.getMi_list().add(new MainInterface(cc_im.getSystemInterfaceId().getInterfaceMenuId().getMenuName(), il));

            }
        }
    }

    public String SaveUpdateContactDetails() {

        FacesMessage msg;
        String retString = null;
        boolean result = comDiv.SaveUpdateContactDetails(ls, this.nameWithIn, this.mobileNo, this.email, this.address1, this.address2, this.address3);

        if (result == true) {

            this.nameWithIn = "";
            this.mobileNo = "";
            this.email = "";
            this.address1 = " ";
            this.address2 = "";
            this.address3 = "";

            retString = getNavigationBean().redirectToWelcome();

//            RequestContext requestContext = RequestContext.getCurrentInstance();
//
//            requestContext.execute("PF('CheckContactDetailsDLG').hide()");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully Saved !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Something went wrong !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }

        return retString;
    }

    public List<MainInterface> getMi_list() {
        return mi_list;
    }

    public void setMi_list(List<MainInterface> mi_list) {
        this.mi_list = mi_list;
    }

    public String[] getColors() {
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public String getNameWithIn() {
        return nameWithIn;
    }

    public void setNameWithIn(String nameWithIn) {
        this.nameWithIn = nameWithIn;
    }

    public class MainInterface implements Serializable {

        private String menuname;
        private List<InterfaceList> interfaceList;

        public MainInterface(String menuname, List<InterfaceList> interfaceList) {
            this.menuname = menuname;
            this.interfaceList = interfaceList;
        }

        public String getMenuname() {
            return menuname;
        }

        public void setMenuname(String menuname) {
            this.menuname = menuname;
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
        private String menuUrl;
        private String colorName;
        private String description;

        public InterfaceList(String menuname, String menuUrl, String colorName, String description) {
            this.menuname = menuname;
            this.menuUrl = menuUrl;
            this.colorName = colorName;
            this.description = description;
        }

        public String getMenuname() {
            return menuname;
        }

        public void setMenuname(String menuname) {
            this.menuname = menuname;
        }

        public String getMenuUrl() {
            return menuUrl;
        }

        public void setMenuUrl(String menuUrl) {
            this.menuUrl = menuUrl;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }
}
