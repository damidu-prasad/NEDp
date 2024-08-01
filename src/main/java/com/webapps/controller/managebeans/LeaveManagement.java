/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.ComPath;
import com.ejb.model.common.StoredProcedures;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.School;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
@ManagedBean(name = "LeaveManagement")
@ViewScoped
public class LeaveManagement {
    
    @EJB
    private StoredProcedures storedProcedures;
    
    private List<String> gradeList;
    
    private int loginUserSchoolId;
    
    HttpServletResponse response;
    HttpServletRequest request;
    private ComLib comlib;
    private ComPath comPath;
    
    @EJB
    private UniDBLocal uni;
    LoginSession ls;
    
    @EJB
    private ComDev comDiv;
    
    @Resource(lookup = "java:app/ds_education_db")
    private javax.sql.DataSource dataSource;
    
    @PostConstruct
    public void init() {
        
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
        ls = (LoginSession) uni.find(Integer.parseInt(request.getSession().getAttribute("LS").toString()), LoginSession.class);
        
        initializeData();
        
    }
    
    public void initializeData() {
        int ls_id = Integer.parseInt(request.getSession().getAttribute("LS").toString());
        LoginSession lss = (LoginSession) uni.find(ls_id, LoginSession.class);
        int gopi = lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getId();
        String query = "SELECT g from School g where g.generalOrganizationProfileId.id = '" + gopi + "'";
        List<School> schoolList = uni.searchByQuery(query);
        for (School school : schoolList) {
            setLoginUserSchoolId(school.getId());
        }
        
        getSchoolAllGrades();
        
    }
    
    public void getSchoolAllGrades() {
        
        List<String> getGradeList = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection(); CallableStatement cs = connection.prepareCall("{call GetGradesBySchoolId(?)}")) {
            
            cs.setInt(1, loginUserSchoolId);
            
            boolean hasResults = cs.execute();
            
            System.out.println("hasResults" + hasResults);
            
            ResultSet rs = cs.getResultSet();
            
            while (rs.next()) {
                
                System.out.println("get_school_grades");
                
                String grade = rs.getString("name");
                
                System.out.println("grade" + grade);
                
                getGradeList.add(grade);
                
            }
            
            rs.close();
            
            setGradeList(getGradeList);
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
    }
    
    public List<ClassListHeader> getClassesByHeader(String grade) {
        
        List<ClassListHeader> clh = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection(); CallableStatement cs = connection.prepareCall("{call GetClassesByGrade(?,?)}")) {
            
            cs.setInt(1, Integer.valueOf(grade));
            cs.setInt(2, loginUserSchoolId);
            
            boolean hasResults = cs.execute();
            
            System.out.println("hasResults" + hasResults);
            
            ResultSet rs = cs.getResultSet();
            
            while (rs.next()) {
                
                System.out.println("get_school_grades");
                
                int id = rs.getInt("id");
                
                int cls = rs.getInt("classes_id");
                
                String name = rs.getString("name");
                
                ClassListHeader c = new ClassListHeader(id, cls, name);
                
                clh.add(c);
            }
            
            rs.close();
            
            return clh;
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
            return clh;
        }
        
    }
    
    public class GradeList implements Serializable {
        
        private int gcs_id;
        private int gcs_grade_id;
        private String gcs_grade_name;
        
        public GradeList(int gcs_id, int gcs_grade_id, String gcs_grade_name) {
            this.gcs_id = gcs_id;
            this.gcs_grade_id = gcs_grade_id;
            this.gcs_grade_name = gcs_grade_name;
        }
        
        public int getGcs_id() {
            return gcs_id;
        }
        
        public void setGcs_id(int gcs_id) {
            this.gcs_id = gcs_id;
        }
        
        public int getGcs_grade_id() {
            return gcs_grade_id;
        }
        
        public void setGcs_grade_id(int gcs_grade_id) {
            this.gcs_grade_id = gcs_grade_id;
        }
        
        public String getGcs_grade_name() {
            return gcs_grade_name;
        }
        
        public void setGcs_grade_name(String gcs_grade_name) {
            this.gcs_grade_name = gcs_grade_name;
        }
        
    }
    
    public class ClassListHeader implements Serializable {
        
        private int gcs_id;
        private int gcs_class_id;
        private String gcs_class_name;
        
        public ClassListHeader(int gcs_id, int gcs_class_id, String gcs_class_name) {
            this.gcs_id = gcs_id;
            this.gcs_class_id = gcs_class_id;
            this.gcs_class_name = gcs_class_name;
        }
        
        public int getGcs_id() {
            return gcs_id;
        }
        
        public void setGcs_id(int gcs_id) {
            this.gcs_id = gcs_id;
        }
        
        public int getGcs_class_id() {
            return gcs_class_id;
        }
        
        public void setGcs_class_id(int gcs_class_id) {
            this.gcs_class_id = gcs_class_id;
        }
        
        public String getGcs_class_name() {
            return gcs_class_name;
        }
        
        public void setGcs_class_name(String gcs_class_name) {
            this.gcs_class_name = gcs_class_name;
        }
        
    }
    
    public List<String> getGradeList() {
        return gradeList;
    }
    
    public void setGradeList(List<String> gradeList) {
        this.gradeList = gradeList;
    }
    
    public int getLoginUserSchoolId() {
        return loginUserSchoolId;
    }
    
    public void setLoginUserSchoolId(int loginUserSchoolId) {
        this.loginUserSchoolId = loginUserSchoolId;
    }
    
}
