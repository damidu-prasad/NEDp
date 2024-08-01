/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.printbeans;

import com.ejb.model.common.ComLib;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Students;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.Year;
import com.webapps.controller.managebeans.DataInputScore;
import com.webapps.controller.managebeans.DataInputScore.SchoolNameList;
import com.webapps.controller.utilities.SortArraysDataInputScore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped
public class PrintDataInputScore implements Serializable {

    HttpServletResponse response;
    HttpServletRequest request;

    private String provinceName = "0";
    private String zoneName = "0";
    private String divisionName = "0";
    private String schoolName = "0";
    
    private String date = "";

    String province;
    String zone;
    String division;

    private String topic;
    private String termsTopic = "";

    private List<SchoolNameList> scoreSchoolList = new ArrayList();

    @EJB
    UniDBLocal uni;
    private ComLib comlib;

    @PostConstruct
    public void init() {

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        setDate(comlib.getDate(new Date()));
        try {

            if (request.getParameter("school") != null) {
                provinceName = request.getParameter("province");
                zoneName = request.getParameter("zone");
                divisionName = request.getParameter("division");
                schoolName = request.getParameter("school");

                String jsn = request.getParameter("json_array").trim();
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(jsn);
                JSONObject job = (JSONObject) obj;
                JSONArray jsnarr = (JSONArray) job.get("jsn");

                getScoreSchoolList().clear();

                List<School> school_list = new ArrayList();

                List<SchoolNameList> sc_list = new ArrayList();

                try {

                    if (!provinceName.equals("0")) {

                        Province p = (Province) uni.find(Integer.parseInt(provinceName), Province.class);

                        province = p.getName() + " Province";
                    }
                    if (!zoneName.equals("0")) {
                        EducationZone p = (EducationZone) uni.find(Integer.parseInt(zoneName), EducationZone.class);

                        zone = " - " + p.getName();
                    }
                    if (!divisionName.equals("0")) {
                        EducationDivision p = (EducationDivision) uni.find(Integer.parseInt(divisionName), EducationDivision.class);

                        division = " - " + p.getName();
                    }

                    if (provinceName.equals("0")) {

                        String query = "SELECT g FROM School g  order by g.id ASC";

                        school_list = uni.searchByQuery(query);

                        province = "All Provinces";
                        zone = "";
                        division = "";

                    } else if (zoneName.equals("0")) {
                        String query = "SELECT g FROM School g where g.educationDivisionId.educationZoneId.provinceId.id='" + Integer.parseInt(provinceName) + "' order by g.id ASC";

                        school_list = uni.searchByQuery(query);

                        zone = " - All Zones";
                        division = "";
                    } else if (divisionName.equals("0")) {
                        String query = "SELECT g FROM School g where g.educationDivisionId.educationZoneId.id='" + Integer.parseInt(zoneName) + "' order by g.id ASC";

                        school_list = uni.searchByQuery(query);

                        division = " - All Divisions";
                    } else if (schoolName.equals("0")) {
                        String query = "SELECT g FROM School g where g.educationDivisionId.id='" + Integer.parseInt(divisionName) + "' order by g.id ASC";

                        school_list = uni.searchByQuery(query);

                    } else if (!schoolName.equals("0")) {
                        String query = "SELECT g FROM School g where g.id='" + Integer.parseInt(schoolName) + "'";

                        school_list = uni.searchByQuery(query);

                    }

                    int k = 1;

                    for (School v : school_list) {

                        double val = 0;

                        int selectedTerms = 0;
                        int enteredCount = 0;
                        for (Iterator it = jsnarr.iterator(); it.hasNext();) {
                            System.out.println("awg");
                            JSONObject json_object = (JSONObject) it.next();

                            String yearName = json_object.get("yearName").toString().trim();
                            String term = json_object.get("term").toString().trim();

                            if (selectedTerms == 0) {

                                termsTopic = yearName + "-Term " + term;
                            } else {
                                termsTopic += ", " + yearName + "-Term " + term;
                            }

                            selectedTerms++;

                            List<StudentMarks> sm_list0 = uni.searchByQuery("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + v.getId() + "' and im.termsId.id='" + term + "' and im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + yearName + "'  and im.isRemoved='0' group by im.gradeClassStudentsId.studentsId.id");
                            enteredCount += sm_list0.size();

                        }

                        if (v.getStudentsCount() != 0 && selectedTerms != 0) {
                            System.out.println(v.getStudentsCount());
                            val = (enteredCount * 100) / (v.getStudentsCount() * selectedTerms);

                        }

                        // teacher count
                        String query = "SELECT g FROM Teacher g where g.schoolId.id='" + v.getId() + "' and g.isActive='1' order by g.generalUserProfileId.nameWithIn ASC";
                        List<Teacher> listAS = uni.searchByQuery(query);
                        int loggedCount = 0;
                        for (Teacher t : listAS) {
                            List<LoginSession> listAS1 = uni.searchByQuery("SELECT g FROM LoginSession g where g.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and g.userLoginGroupId.projectsId.id='1'");
                            if (listAS1.size() > 0) {
                                loggedCount++;

                            }

                        }

                        double logg_perc = 0;
                        if (listAS.size() > 0) {

                            logg_perc = ((loggedCount * 1.0) / listAS.size()) * 100;

                        }

                        sc_list.add(new DataInputScore.SchoolNameList(0, v.getId(), v.getGeneralOrganizationProfileId().getName(), comlib.getDouble(val), val, listAS.size() + "", loggedCount + "", comlib.getDouble(logg_perc)));
                        k++;
                    }

                    SchoolNameList[] addressArray2 = new SchoolNameList[sc_list.size()];
                    for (int i = 0; i < sc_list.size(); i++) {

                        addressArray2[i] = sc_list.get(i);
                    }

                    SchoolNameList[] s2 = SortArraysDataInputScore.GetArray(addressArray2);

                    int order2 = 1;
                    for (int i = (s2.length - 1); i >= 0; i--) {
                        getScoreSchoolList().add(new SchoolNameList(order2, s2[i].getSid(), s2[i].getSname(), s2[i].getMarks_enter_perc(), s2[i].getMarksEnter(), s2[i].getNo_of_teachers(), s2[i].getNo_of_active(), s2[i].getActive_perc()));
                        order2++;
                    }
                    topic = province + zone + division;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public List<SchoolNameList> getScoreSchoolList() {
        return scoreSchoolList;
    }

    public void setScoreSchoolList(List<SchoolNameList> scoreSchoolList) {
        this.scoreSchoolList = scoreSchoolList;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTermsTopic() {
        return termsTopic;
    }

    public void setTermsTopic(String termsTopic) {
        this.termsTopic = termsTopic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
