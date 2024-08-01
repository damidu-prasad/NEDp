/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.common.ComDev;
import com.ejb.model.common.Security;
import com.ejb.model.common.StoredProcedures;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.Country;
import com.ejb.model.entity.DataChangedLogManager;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.Employee;
import com.ejb.model.entity.EmployeeCategory;
import com.ejb.model.entity.EmployeePermanentStstus;
import com.ejb.model.entity.EmployeeType;
import com.ejb.model.entity.Gender;
import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.GradeDescription;
import com.ejb.model.entity.OrganizationType;
import com.ejb.model.entity.OrganizationTypeManager;
import com.ejb.model.entity.Projects;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.RegistrationType;
import com.ejb.model.entity.School;
import com.ejb.model.entity.SchoolCategory;
import com.ejb.model.entity.SchoolType;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Subjects;
import com.ejb.model.entity.SystemInterface;
import com.ejb.model.entity.TableManager;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TeacherClassSubjectMean;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.TypeBasedOnGrade;
import com.ejb.model.entity.UserLogin;
import com.ejb.model.entity.UserLoginGroup;
import com.ejb.model.entity.UserRole;
import com.ejb.model.entity.Year;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped

public class Run {

//    HttpServletResponse response;
    HttpServletRequest request;

    @EJB
    private ComDev comdiv;

    private StreamedContent exportFile;

    @EJB
    UniDBLocal uni;

    @EJB
    StoredProcedures stored;

    @PostConstruct
    public void init() {

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

    }

    public void saveGradeClassStudentsHasSubjects() {
        System.out.println("awa");
        List<StudentMarks> sm_list = uni.searchByQuery("SELECT u FROM StudentMarks u where u.isMandatory=true");
        if (sm_list.size() > 0) {
            for (StudentMarks sm : sm_list) {
                List<GradeClassStudentsHasSubjects> gcshs_list0 = uni.searchByQuerySingle("SELECT im FROM GradeClassStudentsHasSubjects im WHERE im.gradeClassHasSubjectsId.id='" + sm.getGradeClassHasSubjectsId().getId() + "' and im.gradeClassStudentsId.id='" + sm.getGradeClassStudentsId().getId() + "'  ");
                if (gcshs_list0.isEmpty()) {
                    System.out.println("awa1");
                    GradeClassStudentsHasSubjects gcshs = new GradeClassStudentsHasSubjects();
                    gcshs.setGradeClassHasSubjectsId(sm.getGradeClassHasSubjectsId());
                    gcshs.setGradeClassStudentsId(sm.getGradeClassStudentsId());
                    uni.create(gcshs);
                }
            }
        }
        FacesMessage msg = null;
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Saved !", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void updateMarksEnterUser() {

        List<StudentMarks> sm_list = uni.searchByQuery("SELECT u FROM StudentMarks u where u.enteredBy=null");
        System.out.println("sm " + sm_list.size());

    }

    public void createUserLoginGroups() {

        List<UserLogin> sm_list = uni.searchByQuery("SELECT u FROM UserLogin u");
        for (UserLogin ul : sm_list) {

            List<UserLoginGroup> gop_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.id='" + ul.getId() + "' and  im.projectsId.id='1'");

            if (gop_list.isEmpty()) {

                UserLoginGroup ulg = new UserLoginGroup();
                ulg.setCountAttempt(0);
                ulg.setGeneralOrganizationProfileId(ul.getGeneralOrganizationProfileId());
                ulg.setIsActive(1);
                ulg.setIsFirstTime(1);
                ulg.setMaxLoginAttempt(3);
                ulg.setProjectsId((Projects) uni.find(1, Projects.class));
                ulg.setUserLoginId(ul);
                ulg.setUserRoleId(ul.getUserRoleId());
                uni.create(ulg);
            }
        }

    }

    public void createTeacherAccounts() {
        List<Teacher> sm_list = uni.searchByQuery("SELECT u FROM Teacher u");
        for (Teacher t : sm_list) {

            UserLogin ul = null;
            List<UserLogin> gop1_list = uni.searchByQuerySingle("SELECT im FROM UserLogin im WHERE im.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' ");
            if (gop1_list.size() > 0) {
                ul = gop1_list.iterator().next();
            } else {
                try {

                    if (!t.getGeneralUserProfileId().getNic().equals("")) {

                        String username = t.getGeneralUserProfileId().getNic();

                        int min = t.getGeneralUserProfileId().getNic().length() - 4;

                        String password = t.getGeneralUserProfileId().getNic().substring(min, t.getGeneralUserProfileId().getNic().length());
                        System.out.println("awawa");
                        ul = new UserLogin();
                        ul.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                        ul.setGeneralUserProfileId(t.getGeneralUserProfileId());
                        ul.setGeneratedPassword(Security.encrypt(password));
                        ul.setIsActive(1);
                        ul.setPassword(Security.encrypt(password));
                        ul.setSystemInterfaceId((SystemInterface) uni.find(1, SystemInterface.class));
                        ul.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                        ul.setUsername(username);
                        uni.create(ul);

                        List<UserLoginGroup> ulg_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");
                        if (ulg_list.size() == 0) {
                            UserLoginGroup ulg = new UserLoginGroup();
                            ulg.setCountAttempt(0);
                            ulg.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                            ulg.setIsActive(1);
                            ulg.setIsFirstTime(1);
                            ulg.setMaxLoginAttempt(3);
                            ulg.setProjectsId((Projects) uni.find(1, Projects.class));
                            ulg.setUserLoginId(ul);
                            ulg.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                            uni.create(ulg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void createProvince() {
        List<Province> sm_list = uni.searchByQuery("SELECT u FROM Province u");
        for (Province p : sm_list) {

            int ot_id_p = 4;
            OrganizationType otp = (OrganizationType) uni.find(ot_id_p, OrganizationType.class);

            String name = p.getName() + " Province - Department of Education";
            System.out.println(name);
            GeneralOrganizationProfile gop = null;
            List<GeneralOrganizationProfile> gop1_list = uni.searchByQuerySingle("SELECT im FROM GeneralOrganizationProfile im WHERE im.name='" + name + "' and im.organizationTypeId.id='" + ot_id_p + "' ");
            if (gop1_list.size() > 0) {
                gop = gop1_list.iterator().next();
            } else {
                gop = new GeneralOrganizationProfile();
                gop.setAddress1(" ");
                gop.setAddress2(" ");
                gop.setAddress3(" ");
                gop.setCode(" ");
                gop.setCountryIdC((Country) uni.find(204, Country.class));
                gop.setEmail("");
                gop.setName(name);
                gop.setOrganizationTypeId(otp);
                gop.setRegistrationTypeId((RegistrationType) uni.find(2, RegistrationType.class));
                gop.setPhone(" ");
                uni.create(gop);

            }

            OrganizationTypeManager otm = null;
            List<OrganizationTypeManager> otm_list = uni.searchByQuerySingle("SELECT im FROM OrganizationTypeManager im WHERE im.generalOrganizationProfileId.id='1' and im.generalOrganizationProfileId1.id='" + gop.getId() + "' and im.organizationTypeId.id='" + ot_id_p + "' ");
            if (otm_list.size() > 0) {
                otm = otm_list.iterator().next();
            } else {
                otm = new OrganizationTypeManager();
                otm.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uni.find(1, GeneralOrganizationProfile.class));
                otm.setGeneralOrganizationProfileId1(gop);
                otm.setOrganizationTypeId(otp);
                uni.create(otm);
            }

            p.setGeneralOrganizationProfileId(gop);
            uni.update(p);

            String query = "SELECT g FROM EducationZone g where g.provinceId.id='" + p.getId() + "' ";
            List<EducationZone> listAS = uni.searchByQuery(query);
            for (EducationZone z : listAS) {

                int ot_id_z = 5;
                OrganizationType otz = (OrganizationType) uni.find(ot_id_z, OrganizationType.class);

                String namez = z.getName() + " - Department of Education";
                System.out.println(namez);
                GeneralOrganizationProfile gopz = null;
                List<GeneralOrganizationProfile> gopz_list = uni.searchByQuerySingle("SELECT im FROM GeneralOrganizationProfile im WHERE im.name='" + namez + "' and im.organizationTypeId.id='" + ot_id_z + "' ");
                if (gopz_list.size() > 0) {
                    gopz = gopz_list.iterator().next();
                } else {
                    gopz = new GeneralOrganizationProfile();
                    gopz.setAddress1(" ");
                    gopz.setAddress2(" ");
                    gopz.setAddress3(" ");
                    gopz.setCode(" ");
                    gopz.setCountryIdC((Country) uni.find(204, Country.class));
                    gopz.setEmail("");
                    gopz.setName(namez);
                    gopz.setOrganizationTypeId(otz);
                    gopz.setRegistrationTypeId((RegistrationType) uni.find(2, RegistrationType.class));
                    gopz.setPhone(" ");
                    uni.create(gopz);

                }

                OrganizationTypeManager otmz = null;
                List<OrganizationTypeManager> otmz_list = uni.searchByQuerySingle("SELECT im FROM OrganizationTypeManager im WHERE im.generalOrganizationProfileId.id='" + gop.getId() + "' and im.generalOrganizationProfileId1.id='" + gopz.getId() + "' and im.organizationTypeId.id='" + ot_id_z + "' ");
                if (otmz_list.size() > 0) {
                    otmz = otmz_list.iterator().next();
                } else {
                    otmz = new OrganizationTypeManager();
                    otmz.setGeneralOrganizationProfileId(gop);
                    otmz.setGeneralOrganizationProfileId1(gopz);
                    otmz.setOrganizationTypeId(otz);
                    uni.create(otmz);
                }
                z.setGeneralOrganizationProfileId(gopz);
                uni.update(z);

                String queryd = "SELECT g FROM EducationDivision g where g.educationZoneId.id='" + z.getId() + "' ";
                List<EducationDivision> listd = uni.searchByQuery(queryd);
                for (EducationDivision d : listd) {

                    int ot_id_d = 6;
                    OrganizationType otd = (OrganizationType) uni.find(ot_id_d, OrganizationType.class);

                    String named = d.getName() + " Division - Department of Education";
                    System.out.println(named);
                    GeneralOrganizationProfile gopd = null;
                    List<GeneralOrganizationProfile> gopd_list = uni.searchByQuerySingle("SELECT im FROM GeneralOrganizationProfile im WHERE im.name='" + named + "' and im.organizationTypeId.id='" + ot_id_d + "' ");
                    if (gopd_list.size() > 0) {
                        gopd = gopd_list.iterator().next();
                    } else {
                        gopd = new GeneralOrganizationProfile();
                        gopd.setAddress1(" ");
                        gopd.setAddress2(" ");
                        gopd.setAddress3(" ");
                        gopd.setCode(" ");
                        gopd.setCountryIdC((Country) uni.find(204, Country.class));
                        gopd.setEmail("");
                        gopd.setName(named);
                        gopd.setOrganizationTypeId(otd);
                        gopd.setRegistrationTypeId((RegistrationType) uni.find(2, RegistrationType.class));
                        gopd.setPhone(" ");
                        uni.create(gopd);

                    }

                    OrganizationTypeManager otmd = null;
                    List<OrganizationTypeManager> otmd_list = uni.searchByQuerySingle("SELECT im FROM OrganizationTypeManager im WHERE im.generalOrganizationProfileId.id='" + gopz.getId() + "' and im.generalOrganizationProfileId1.id='" + gopd.getId() + "' and im.organizationTypeId.id='" + ot_id_d + "' ");
                    if (otmd_list.size() > 0) {
                        otmd = otmd_list.iterator().next();
                    } else {
                        otmd = new OrganizationTypeManager();
                        otmd.setGeneralOrganizationProfileId(gopz);
                        otmd.setGeneralOrganizationProfileId1(gopd);
                        otmd.setOrganizationTypeId(otd);
                        uni.create(otmd);
                    }
                    d.setGeneralOrganizationProfileId(gopd);
                    uni.update(d);

//                    String querys = "SELECT g FROM School g where g.educationDivisionId.id='" + d.getId() + "'";
//                    List<School> lists = uni.searchByQuery(querys);
//                    for (School s : lists) {
//
//                        OrganizationTypeManager otms = null;
//                        List<OrganizationTypeManager> otms_list = uni.searchByQuerySingle("SELECT im FROM OrganizationTypeManager im WHERE  im.generalOrganizationProfileId1.id='" + s.getGeneralOrganizationProfileId().getId() + "' and im.organizationTypeId.id='2' ");
//                        if (otms_list.size() > 0) {
//                            otms = otms_list.iterator().next();
//                            otms.setGeneralOrganizationProfileId(gopd);
//                            uni.update(otms);
//                        }
//
//                        int ot_id_sd = 7;
//                        OrganizationType otsd = (OrganizationType) uni.find(ot_id_sd, OrganizationType.class);
//
//                        String namesd = s.getGeneralOrganizationProfileId().getName() + " - SDS";
//                        namesd = namesd.replace("'", "\u2019");
//                        System.out.println(namesd);
//                        GeneralOrganizationProfile gopsd = null;
//                        List<GeneralOrganizationProfile> gopsd_list = uni.searchByQuerySingle("SELECT im FROM GeneralOrganizationProfile im WHERE im.name='" + namesd + "' and im.organizationTypeId.id='" + ot_id_sd + "' ");
//                        if (gopsd_list.size() > 0) {
//                            gopsd = gopsd_list.iterator().next();
//                        } else {
//                            gopsd = new GeneralOrganizationProfile();
//                            gopsd.setAddress1(" ");
//                            gopsd.setAddress2(" ");
//                            gopsd.setAddress3(" ");
//                            gopsd.setCode(" ");
//                            gopsd.setCountryIdC((Country) uni.find(204, Country.class));
//                            gopsd.setEmail("");
//                            gopsd.setName(namesd);
//                            gopsd.setOrganizationTypeId(otsd);
//                            gopsd.setRegistrationTypeId((RegistrationType) uni.find(2, RegistrationType.class));
//                            gopsd.setPhone(" ");
//                            uni.create(gopsd);
//
//                        }
//
//                        OrganizationTypeManager otmsd = null;
//                        List<OrganizationTypeManager> otmsd_list = uni.searchByQuerySingle("SELECT im FROM OrganizationTypeManager im WHERE im.generalOrganizationProfileId.id='" + s.getGeneralOrganizationProfileId().getId() + "' and im.generalOrganizationProfileId1.id='" + gopsd.getId() + "' and im.organizationTypeId.id='" + ot_id_sd + "' ");
//                        if (otmsd_list.size() > 0) {
//                            otmsd = otmsd_list.iterator().next();
//                        } else {
//                            otmsd = new OrganizationTypeManager();
//                            otmsd.setGeneralOrganizationProfileId(s.getGeneralOrganizationProfileId());
//                            otmsd.setGeneralOrganizationProfileId1(gopsd);
//                            otmsd.setOrganizationTypeId(otsd);
//                            uni.create(otmsd);
//                        }
//
//                    }
                }

            }
            System.out.println("awa1 " + p.getName());
        }
    }

    public void updateUserLogin() {
        String queryd = "SELECT g FROM UserLoginGroup g where g.projectsId.id='2' and g.generalOrganizationProfileId.id!='252' ";
        List<UserLoginGroup> listd = uni.searchByQuery(queryd);
        for (UserLoginGroup d : listd) {
            System.out.println("gg " + d.getGeneralOrganizationProfileId().getId());
            OrganizationTypeManager otmsd = null;
            List<OrganizationTypeManager> otmsd_list = uni.searchByQuerySingle("SELECT im FROM OrganizationTypeManager im WHERE im.generalOrganizationProfileId.id='" + d.getGeneralOrganizationProfileId().getId() + "' and  im.organizationTypeId.id='7' ");
            if (otmsd_list.size() > 0) {
                otmsd = otmsd_list.iterator().next();
            }
            d.setGeneralOrganizationProfileId(otmsd.getGeneralOrganizationProfileId1());
            uni.update(d);

        }

    }

    public void createWorkbook() throws IOException {
        try {
            File fileName = new File("F:\\javainstitute_portal\\DATA_SET.xlsx");
            FileOutputStream fos = new FileOutputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet("DATA_SET");

            Row row = sheet.createRow(0);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue("Province");

            Cell cell1 = row.createCell(1);
            cell1.setCellValue("Zone");

            Cell cell2 = row.createCell(2);
            cell2.setCellValue("Division");

            Cell cell3 = row.createCell(3);
            cell3.setCellValue("School");

            Cell cell4 = row.createCell(4);
            cell4.setCellValue("Year");

            Cell cell5 = row.createCell(5);
            cell5.setCellValue("Class");

            Cell cell6 = row.createCell(6);
            cell6.setCellValue("Student");

            Cell cell7 = row.createCell(7);
            cell7.setCellValue("Subject");

            Cell cell8 = row.createCell(8);
            cell8.setCellValue("Marks");

            int i = 1;
            String query3 = "SELECT g FROM StudentMarks g where g.isMandatory='1' and g.isRemoved='0'";
            List<StudentMarks> listAS3 = uni.searchByQueryLimit(query3, 10);
            for (StudentMarks marks : listAS3) {
                System.out.println("awa1");
                Row row1 = sheet.createRow(i);

                Cell cell0_ = row1.createCell(0);
                cell0_.setCellValue(marks.getGradeClassStudentsId().getStudentsId().getSchoolId().getEducationDivisionId().getEducationZoneId().getProvinceId().getName());

                Cell cell1_ = row1.createCell(1);
                cell1_.setCellValue(marks.getGradeClassStudentsId().getStudentsId().getSchoolId().getEducationDivisionId().getEducationZoneId().getName());

                Cell cell2_ = row1.createCell(2);
                cell2_.setCellValue(marks.getGradeClassStudentsId().getStudentsId().getSchoolId().getEducationDivisionId().getName());

                Cell cell3_ = row1.createCell(3);
                cell3_.setCellValue(marks.getGradeClassStudentsId().getStudentsId().getSchoolId().getGeneralOrganizationProfileId().getName());

                Cell cell4_ = row1.createCell(4);
                cell4_.setCellValue(marks.getGradeClassStudentsId().getGradeClassStreamManagerId().getYearId().getName());

                Cell cell5_ = row1.createCell(5);
                cell5_.setCellValue(marks.getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + "-" + marks.getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName());

                Cell cell6_ = row1.createCell(6);
                cell6_.setCellValue(marks.getGradeClassStudentsId().getStudentsId().getGeneralUserProfileId().getNameWithIn());

                Cell cell7_ = row1.createCell(7);
                cell7_.setCellValue(marks.getGradeClassHasSubjectsId().getSubjectsId().getName());

                Cell cell8_ = row1.createCell(8);
                cell8_.setCellValue(marks.getMarks());

                i++;
            }

//            workbook.write(fos);
//            fos.flush();
//            fos.close();
//            System.out.println("okkkk");
//            response.setHeader("Content-Disposition", "attachment;filename=new-excel-file.xls");
//            OutputStream bos = response.getOutputStream();
//            workbook.write(bos); // write excel data to a byte array
//            bos.flush();
//            bos.close();
//            ResponseBuilder response = Response.ok((Object) fos.getChannel());
//		response.header("Content-Disposition",
//			"attachment; filename=new-excel-file.xls");
//		 response.build();
// Now use your ByteArrayDataSource as
//            DataSource fds = new ByteArrayDataSource(bos.toByteArray(), "application/vnd.ms-excel");
//            String ar[] = {};
//            Com.mailSenderAttachement("thilinimadagama23@gmail.com", ar, "Sample", "bodyy", fds, "filename");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void deleteDuplicateClasses() {
        List<GradeClassStream> removable = new ArrayList();

        List<GradeClassStream> gop_list = uni.searchByQuery("SELECT im FROM GradeClassStream im GROUP BY im.classesId,im.schoolId,im.gradeId HAVING COUNT(im.classesId) > 1 and COUNT(im.schoolId) > 1 and COUNT(im.gradeId) > 1 order by im.classesId,im.schoolId,im.gradeId asc");
        System.out.println("s " + gop_list.size());

        for (GradeClassStream gcs : gop_list) {
            System.out.println(gcs.getSchoolId().getId() + "|" + gcs.getGradeId().getName() + "|" + gcs.getClassesId().getName());

            int i = 0;
            GradeClassStream fix = null;

            List<GradeClassStream> gcs_list = uni.searchByQuery("SELECT im FROM GradeClassStream im where  im.schoolId.id='" + gcs.getSchoolId().getId() + "' and im.gradeId.id='" + gcs.getGradeId().getId() + "' and im.classesId.id='" + gcs.getClassesId().getId() + "'");
            for (GradeClassStream g : gcs_list) {

                if (i == 0) {
                    fix = g;
                    i++;
                } else {

                    List<GradeClassStreamManager> gcsm_list = uni.searchByQuery("SELECT im FROM GradeClassStreamManager im where  im.gradeClassStreamId.id='" + g.getId() + "'");
                    for (GradeClassStreamManager gcsm : gcsm_list) {

                        gcsm.setGradeClassStreamId(fix);
                        uni.update(gcsm);

                    }
                    removable.add(g);

                }

            }

        }
        for (GradeClassStream re : removable) {
            System.out.println("awad " + re.getId());
            uni.remove(re.getId(), GradeClassStream.class);
        }
        System.out.println("done");

        // year classes
        List<GradeClassStreamManager> gcsm_lists = uni.searchByQuery("SELECT im FROM GradeClassStreamManager im GROUP BY im.gradeClassStreamId.classesId,im.gradeClassStreamId.schoolId,im.gradeClassStreamId.gradeId,im.yearId HAVING COUNT(im.gradeClassStreamId.classesId) > 1 and COUNT(im.gradeClassStreamId.schoolId) > 1 and COUNT(im.gradeClassStreamId.gradeId) > 1 and COUNT(im.yearId) > 1 order by im.gradeClassStreamId.classesId,im.gradeClassStreamId.schoolId,im.gradeClassStreamId.gradeId,im.yearId asc");
        for (GradeClassStreamManager dcsmr : gcsm_lists) {

            System.out.println(dcsmr.getGradeClassStreamId().getSchoolId().getId() + "|" + dcsmr.getGradeClassStreamId().getGradeId().getName() + "|" + dcsmr.getGradeClassStreamId().getClassesId().getName() + "|" + dcsmr.getYearId().getName());

            int i = 0;
            GradeClassStreamManager fix = null;
            List<GradeClassStreamManager> gcsm_all = uni.searchByQuery("SELECT im FROM GradeClassStreamManager im where im.gradeClassStreamId.classesId.id='" + dcsmr.getGradeClassStreamId().getClassesId().getId() + "'  and im.gradeClassStreamId.schoolId.id='" + dcsmr.getGradeClassStreamId().getSchoolId().getId() + "' and  im.gradeClassStreamId.gradeId.id='" + dcsmr.getGradeClassStreamId().getGradeId().getId() + "' and  im.yearId.id='" + dcsmr.getYearId().getId() + "'");
            for (GradeClassStreamManager gr : gcsm_all) {

                if (i == 0) {
                    fix = gr;
                    i++;
                } else {
                    List<GradeClassStudents> gcsm_list = uni.searchByQuery("SELECT im FROM GradeClassStudents im where  im.gradeClassStreamManagerId.id='" + gr.getId() + "'");
                    for (GradeClassStudents gcsm : gcsm_list) {

                        gcsm.setGradeClassStreamManagerId(fix);
                        uni.update(gcsm);
                    }
                    uni.remove(gr.getId(), GradeClassStreamManager.class);
                }

            }
        }
        System.out.println("done2");

        // Student
        List<GradeClassStudents> gcst_lists = uni.searchByQuery("SELECT im FROM GradeClassStudents im GROUP BY im.gradeClassStreamManagerId,im.studentsId HAVING COUNT(im.gradeClassStreamManagerId) > 1 and COUNT(im.studentsId) > 1  order by im.gradeClassStreamManagerId,im.studentsId asc");
        for (GradeClassStudents gct : gcst_lists) {
            System.out.println(gct.getGradeClassStreamManagerId().getId() + "|" + gct.getStudentsId().getId());

            int i = 0;
            GradeClassStudents fix = null;

            List<GradeClassStudents> gcs1_lists = uni.searchByQuery("SELECT im FROM GradeClassStudents im where im.gradeClassStreamManagerId.id='" + gct.getGradeClassStreamManagerId().getId() + "' and im.studentsId.id='" + gct.getStudentsId().getId() + "'");
            for (GradeClassStudents gct1 : gcs1_lists) {

                if (i == 0) {
                    fix = gct1;
                    i++;
                } else {
                    List<StudentMarks> gcsm_list = uni.searchByQuery("SELECT im FROM StudentMarks im where  im.gradeClassStudentsId.id='" + gct1.getId() + "'");
                    for (StudentMarks sm : gcsm_list) {

                        sm.setGradeClassStudentsId(fix);
                        uni.update(sm);
                    }

                    List<GradeClassStudentsHasSubjects> gcshsm_list = uni.searchByQuery("SELECT im FROM GradeClassStudentsHasSubjects im where  im.gradeClassStudentsId.id='" + gct1.getId() + "'");
                    for (GradeClassStudentsHasSubjects sm : gcshsm_list) {

                        sm.setGradeClassStudentsId(fix);
                        uni.update(sm);
                    }

                    uni.remove(gct1.getId(), GradeClassStudents.class);
                }

            }

        }

        // marks
        List<StudentMarks> sm_lists1 = uni.searchByQuery("SELECT im FROM StudentMarks im where im.isRemoved='0' GROUP BY im.gradeClassStudentsId,im.gradeClassHasSubjectsId,im.termsId HAVING COUNT(im.gradeClassStudentsId) > 1 and COUNT(im.gradeClassHasSubjectsId) > 1 and COUNT(im.termsId) > 1  order by im.gradeClassStudentsId,im.gradeClassHasSubjectsId,im.termsId asc");
        for (StudentMarks sm1 : sm_lists1) {

            System.out.println(sm1.getGradeClassHasSubjectsId().getId() + "|" + sm1.getGradeClassStudentsId().getId());
            int i = 0;
            List<StudentMarks> sm_lists = uni.searchByQuery("SELECT im FROM StudentMarks im where im.isRemoved='0' and im.gradeClassStudentsId.id='" + sm1.getGradeClassStudentsId().getId() + "' and im.gradeClassHasSubjectsId.id='" + sm1.getGradeClassHasSubjectsId().getId() + "' and im.termsId.id='" + sm1.getTermsId().getId() + "'");
            for (StudentMarks sm : sm_lists) {

                if (i == 0) {

                    i++;
                } else {

                    uni.remove(sm.getId(), StudentMarks.class);
                }
            }
        }

        // subjects
        List<GradeClassStudentsHasSubjects> sm_lists2 = uni.searchByQuery("SELECT im FROM GradeClassStudentsHasSubjects im  GROUP BY im.gradeClassStudentsId,im.gradeClassHasSubjectsId HAVING COUNT(im.gradeClassStudentsId) > 1 and COUNT(im.gradeClassHasSubjectsId) > 1   order by im.gradeClassStudentsId,im.gradeClassHasSubjectsId asc");
        for (GradeClassStudentsHasSubjects sm1 : sm_lists2) {
            int i = 0;
            List<GradeClassStudentsHasSubjects> sm_lists = uni.searchByQuery("SELECT im FROM GradeClassStudentsHasSubjects im where  im.gradeClassStudentsId.id='" + sm1.getGradeClassStudentsId().getId() + "' and im.gradeClassHasSubjectsId.id='" + sm1.getGradeClassHasSubjectsId().getId() + "' ");
            for (GradeClassStudentsHasSubjects sm : sm_lists) {

                if (i == 0) {

                    i++;
                } else {

                    uni.remove(sm.getId(), GradeClassStudentsHasSubjects.class);
                }
            }
        }
        System.out.println("done all");

    }

    public void handleFileUpload(FileUploadEvent event) {
        try {

            School s = (School) uni.find(127, School.class); //127

            UploadedFile up = event.getFile();

            InputStream stream = new ByteArrayInputStream(up.getContents());

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(stream);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator1 = sheet.iterator();

            while (rowIterator1.hasNext()) {
                Row row = rowIterator1.next();
                //For each row, iterate through all the columns
                //  System.out.println(row.getRowNum());

                if (row.getRowNum() > 0) {

                    String title = "";
                    String name = "";
                    String add1 = "";
                    String add2 = "";
                    String add3 = " ";
                    Date dob = new Date();
                    String mobile = "";
                    String email = "";
                    String nic = "";
                    Teacher t = null;

                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        cell.getColumnIndex();

                        if (cell.getColumnIndex() == 0) {
                            if (cell.getCellTypeEnum() == CellType.STRING) {
                                if (cell.getStringCellValue().contains("Mr.")) {

                                    title = "Mr";
                                    name = cell.getStringCellValue().replace("Mr.", "").trim();

                                } else if (cell.getStringCellValue().contains("Mrs.")) {

                                    title = "Mrs";
                                    name = cell.getStringCellValue().replace("Mrs.", "").trim();

                                } else if (cell.getStringCellValue().contains("Miss.")) {

                                    title = "Miss";
                                    name = cell.getStringCellValue().replace("Miss.", "").trim();

                                } else if (cell.getStringCellValue().contains("Ven.")) {

                                    title = "Ven";
                                    name = cell.getStringCellValue().replace("Ven.", "").trim();

                                } else {

                                    System.out.println(">>>>>>>>>>>>>>" + cell.getStringCellValue());
                                }

                            }

                        }
                        if (cell.getColumnIndex() == 1) {
                            add1 = cell.getStringCellValue().trim();
                        }
                        if (cell.getColumnIndex() == 2) {
                            add2 = cell.getStringCellValue().trim();
                        }
                        if (cell.getColumnIndex() == 3) {
                            if (!cell.getStringCellValue().trim().equals("")) {

                                add3 = cell.getStringCellValue().trim();
                            }
                        }
                        if (cell.getColumnIndex() == 4) {
                            if (!cell.getStringCellValue().trim().equals("")) {
                                add3 += ", " + cell.getStringCellValue().trim();
                            }
                        }
                        if (cell.getColumnIndex() == 5) {
                            if (!cell.getStringCellValue().trim().equals("")) {
                                add3 += ", " + cell.getStringCellValue().trim();
                            }
                        }
                        if (cell.getColumnIndex() == 6) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                dob = cell.getDateCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 7) {
                            mobile = cell.getStringCellValue().replace("-", "").trim();
                        }
                        if (cell.getColumnIndex() == 8) {
                            if (cell.getCellTypeEnum() == CellType.STRING) {
                                nic = cell.getStringCellValue().trim();
                            }
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                nic = new BigDecimal(cell.getNumericCellValue()) + "";
                            }
                        }
                        if (cell.getColumnIndex() == 9) {
                            if (cell.getCellTypeEnum() == CellType.STRING) {
                                email = cell.getStringCellValue().trim();
                            }
                        }

                    }
                    System.out.println(title + " | " + name + " | " + add1 + " | " + add2 + " | " + add3 + " | " + dob + " | " + mobile + " | " + nic + " | " + email);

                    GeneralUserProfile si = null;
                    List<GeneralUserProfile> gop_list = uni.searchByQuerySingle("SELECT im FROM GeneralUserProfile im WHERE im.nic='" + nic + "' ");
                    if (gop_list.size() > 0) {
                        si = gop_list.iterator().next();

                    } else {

                        String gender = "1";
                        if (title.equals("Mrs") || title.equals("Miss")) {
                            gender = "2";
                        }

                        si = new GeneralUserProfile();
                        si.setAddress1(add1);
                        si.setAddress2(add2);
                        si.setAddress3(add3);
                        si.setDob(dob);
                        si.setEmail(email);
                        si.setFirstName(" ");
                        si.setGenderId((Gender) uni.find(Integer.parseInt(gender), Gender.class));
                        si.setGeneralOrganizationProfileIdGop(s.getGeneralOrganizationProfileId());
                        si.setIsActive(1);
                        si.setLastName(" ");
                        si.setMobilePhone(mobile);
                        si.setNameWithIn(name);
                        si.setNic(nic);
                        si.setProfileCreatedDate(new Date());
                        si.setTitle(title);
                        uni.create(si);

                    }

                    List<Teacher> tea_list = uni.searchByQuerySingle("SELECT im FROM Teacher im WHERE im.generalUserProfileId.id='" + si.getId() + "' ");
                    if (tea_list.size() > 0) {
                        t = tea_list.iterator().next();
                        Teacher ex = tea_list.iterator().next();
                        if (ex.getSchoolId().getId() == s.getId()) {

                        } else {

                            ex.setIsActive(0);
                            uni.update(ex);

                            DataChangedLogManager gtlm2 = new DataChangedLogManager();
                            gtlm2.setAttributeName("is_active");
                            gtlm2.setComment("");
                            gtlm2.setDate(new Date());
                            gtlm2.setNewData("0");
                            gtlm2.setOldData("1");
                            gtlm2.setReference(ex.getId());
                            gtlm2.setTableManagerId((TableManager) uni.find(6, TableManager.class));
                            gtlm2.setUserLoginId((UserLogin) uni.find(1, UserLogin.class));
                            uni.create(gtlm2);

                            t = new Teacher();
                            t.setGeneralUserProfileId(si);
                            t.setIsActive(1);
                            t.setSchoolId(s);
                            t.setTeacherId("");
                            t.setIsVerified(true);
                            uni.create(t);

                            List<UserLoginGroup> ulg_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "'  and im.projectsId.id='1'");
                            if (ulg_list.size() > 0) {

                                UserLoginGroup ulg = ulg_list.iterator().next();
                                ulg.setGeneralOrganizationProfileId(s.getGeneralOrganizationProfileId());
                                uni.update(ulg);

                            }

                        }

                    } else {
                        t = new Teacher();
                        t.setGeneralUserProfileId(si);
                        t.setIsActive(1);
                        t.setSchoolId(s);
                        t.setTeacherId("");
                        t.setIsVerified(true);
                        uni.create(t);

                    }
                    String username = "";
                    String password = "";

                    List<UserLoginGroup> gop_lists = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");
                    if (gop_lists.size() > 0) {
                        UserLoginGroup ulg = gop_lists.iterator().next();
                        ulg.setIsActive(1);
                        ulg.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                        ulg.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                        uni.update(ulg);
                        username = ulg.getUserLoginId().getUsername();
                        password = Security.decrypt(ulg.getUserLoginId().getPassword());
                    } else {

                        UserLogin ul = null;

                        List<UserLogin> gop1_list = uni.searchByQuerySingle("SELECT im FROM UserLogin im WHERE im.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' ");
                        if (gop1_list.size() > 0) {
                            ul = gop1_list.iterator().next();
                            ul.setIsActive(1);
                            ul.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                            ul.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                            uni.update(ul);

                            username = ul.getUsername();
                            password = Security.decrypt(ul.getPassword());

                        } else {

                            if (!t.getGeneralUserProfileId().getNic().equals("")) {

                                username = t.getGeneralUserProfileId().getNic();

                                int min = t.getGeneralUserProfileId().getNic().length() - 4;

                                password = t.getGeneralUserProfileId().getNic().substring(min, t.getGeneralUserProfileId().getNic().length());
                                System.out.println("awawa");
                                ul = new UserLogin();
                                ul.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                                ul.setGeneralUserProfileId(t.getGeneralUserProfileId());
                                ul.setGeneratedPassword(Security.encrypt(password));
                                ul.setIsActive(1);
                                ul.setPassword(Security.encrypt(password));
                                ul.setSystemInterfaceId((SystemInterface) uni.find(1, SystemInterface.class));
                                ul.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                                ul.setUsername(username);
                                uni.create(ul);

                                List<UserLoginGroup> ulg_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");
                                if (ulg_list.size() == 0) {
                                    UserLoginGroup ulg = new UserLoginGroup();
                                    ulg.setCountAttempt(0);
                                    ulg.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                                    ulg.setIsActive(1);
                                    ulg.setIsFirstTime(1);
                                    ulg.setMaxLoginAttempt(3);
                                    ulg.setProjectsId((Projects) uni.find(1, Projects.class));
                                    ulg.setUserLoginId(ul);
                                    ulg.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                                    uni.create(ulg);
                                }
                            }
                        }

                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String updateXlsx() {

        try {

            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet("DATA_SET");

            Row row = sheet.createRow(0);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue("Province");

            Cell cell1 = row.createCell(1);
            cell1.setCellValue("Zone");

            Cell cell2 = row.createCell(2);
            cell2.setCellValue("Division");

            Cell cell3 = row.createCell(3);
            cell3.setCellValue("School");

//            XSSFWorkbook workbook = new XSSFWorkbook(file.toString());
//
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            Cell cell = sheet.getRow(1).getCell(1);
//
//            cell.setCellValue("value1");
//
//            cell = sheet.getRow(1).getCell(7);
//
//            cell.setCellValue("value1");
//
//            cell = sheet.getRow(4).getCell(1);
//
//            cell.setCellValue("value1");
//
//            cell = sheet.getRow(5).getCell(1);
//
//            cell.setCellValue("value1");
//As given above we can write value at particular index.
            FacesContext fc = FacesContext.getCurrentInstance();

            HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            workbook.write(outByteStream);
            byte[] outArray = outByteStream.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(outArray);
            DefaultStreamedContent file_1_5_sinhala = new DefaultStreamedContent(bis, "application/vnd.ms-excel", "1-5_Sinhala_Medium.xlsx");

//            response.setContentType("application/vnd.ms-excel");
//            response.setContentLength(outArray.length);
//            response.setHeader("Expires:", "0"); // eliminates browser caching
//            response.setHeader("Content-Disposition", "attachment; filename=Leave Details.xlsx");
//            OutputStream outStream = response.getOutputStream();
//            outStream.write(outArray);
//            outStream.flush();
//            outStream.close();
//            
//            fc.responseComplete();
//                fc.renderResponse();
        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    public void lOBExport2Excel() {

        try {

            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet("DATA_SET");

            Row row = sheet.createRow(0);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue("Province");

            Cell cell1 = row.createCell(1);
            cell1.setCellValue("Zone");

            Cell cell2 = row.createCell(2);
            cell2.setCellValue("Division");

            Cell cell3 = row.createCell(3);
            cell3.setCellValue("School");

            String excelFileName = "abc.xlsx";

            FileOutputStream fos = new FileOutputStream(excelFileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            InputStream stream = new BufferedInputStream(new FileInputStream(excelFileName));
            setExportFile(new DefaultStreamedContent(stream, "application/xls", excelFileName));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public StreamedContent getExportFile() {
        return exportFile;
    }

    public void setExportFile(StreamedContent exportFile) {
        this.exportFile = exportFile;
    }

    public void generateGradeClassSubjects() {
        try {

            int i = 1;
            String query = "SELECT g FROM GradeClassStudentsHasSubjects g where  g.gradeClassStudentsId.isRemoved='0' and g.gradeClassHasSubjectsId.subjectsId.isActive='1'";
            List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(query);
            for (GradeClassStudentsHasSubjects l : list) {
                System.out.println("waw1" + l.getId());
                String querysub = "SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.id='" + l.getGradeClassStudentsId().getGradeClassStreamManagerId().getId() + "' and g.gradeClassHasSubjectsId.id='" + l.getGradeClassHasSubjectsId().getId() + "' and g.isActive='1' ";
                List<GradeClassSubjectTeacher> sublist = uni.searchByQuery(querysub);
                if (sublist.size() > 0) {

                } else {
                    System.out.println("-------------------------------i " + i);
                    GradeClassSubjectTeacher gc = new GradeClassSubjectTeacher();
                    gc.setGradeClassHasSubjectsId(l.getGradeClassHasSubjectsId());
                    gc.setGradeClassStreamManagerId(l.getGradeClassStudentsId().getGradeClassStreamManagerId());
                    gc.setIsActive(1);
                    gc.setTeacherId(null);
                    uni.create(gc);
                    i++;
                }
            }

            String nics = "630130042V,737190196V,655544852V,618140750V,797651818V,792833349V,697202382V,720210614V,626110835V,716982955V,195882300660,197801802440,685960737V,795911324V,707161310V,797633470V,865370717V,656881011V,747181675V,815302974V,716471357V,593044181V,592890488V,582220492V,642602632V,596360262V";
            String ar[] = nics.split(",");

            for (int k = 0; k < ar.length; k++) {

                GeneralUserProfile si = null;
                List<GeneralUserProfile> gop_list = uni.searchByQuerySingle("SELECT im FROM GeneralUserProfile im WHERE im.nic='" + ar[k] + "' ");
                if (gop_list.size() > 0) {
                    si = gop_list.iterator().next();

                } else {

                    String gender = "1";

                    si = new GeneralUserProfile();
                    si.setAddress1(" ");
                    si.setAddress2(" ");
                    si.setAddress3(" ");
                    si.setDob(new Date());
                    si.setEmail(" ");
                    si.setFirstName(" ");
                    si.setGenderId((Gender) uni.find(Integer.parseInt(gender), Gender.class));
                    si.setGeneralOrganizationProfileIdGop((GeneralOrganizationProfile) uni.find(1, GeneralOrganizationProfile.class));
                    si.setIsActive(1);
                    si.setLastName(" ");
                    si.setMobilePhone(" ");
                    si.setNameWithIn(" ");
                    si.setNic(ar[k]);
                    si.setProfileCreatedDate(new Date());
                    si.setTitle("Mr");
                    uni.create(si);

                }

                String username = "";
                String password = "";

                List<UserLoginGroup> gop_lists = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + si.getId() + "'  and im.projectsId.id='1'");
                if (gop_lists.size() > 0) {
                    UserLoginGroup ulg = gop_lists.iterator().next();
                    ulg.setIsActive(1);
                    ulg.setUserRoleId((UserRole) uni.find(1, UserRole.class));
                    ulg.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uni.find(252, GeneralOrganizationProfile.class));
                    uni.update(ulg);
                    username = ulg.getUserLoginId().getUsername();
                    password = Security.decrypt(ulg.getUserLoginId().getPassword());
                } else {

                    UserLogin ul = null;

                    List<UserLogin> gop1_list = uni.searchByQuerySingle("SELECT im FROM UserLogin im WHERE im.generalUserProfileId.id='" + si.getId() + "' ");
                    if (gop1_list.size() > 0) {
                        ul = gop1_list.iterator().next();
                        ul.setIsActive(1);
                        ul.setUserRoleId((UserRole) uni.find(1, UserRole.class));
                        ul.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uni.find(252, GeneralOrganizationProfile.class));
                        uni.update(ul);

                        username = ul.getUsername();
                        password = Security.decrypt(ul.getPassword());

                    } else {

                        if (!si.getNic().equals("")) {

                            username = si.getNic();

                            int min = si.getNic().length() - 4;

                            password = si.getNic().substring(min, si.getNic().length());
                            System.out.println("awawa");
                            ul = new UserLogin();
                            ul.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uni.find(252, GeneralOrganizationProfile.class));
                            ul.setGeneralUserProfileId(si);
                            ul.setGeneratedPassword(Security.encrypt(password));
                            ul.setIsActive(1);
                            ul.setPassword(Security.encrypt(password));
                            ul.setSystemInterfaceId((SystemInterface) uni.find(1, SystemInterface.class));
                            ul.setUserRoleId((UserRole) uni.find(1, UserRole.class));
                            ul.setUsername(username);
                            uni.create(ul);

                            List<UserLoginGroup> ulg_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + si.getId() + "' and im.generalOrganizationProfileId.id='252' and im.projectsId.id='1'");
                            if (ulg_list.size() == 0) {
                                UserLoginGroup ulg = new UserLoginGroup();
                                ulg.setCountAttempt(0);
                                ulg.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uni.find(252, GeneralOrganizationProfile.class));
                                ulg.setIsActive(1);
                                ulg.setIsFirstTime(1);
                                ulg.setMaxLoginAttempt(3);
                                ulg.setProjectsId((Projects) uni.find(1, Projects.class));
                                ulg.setUserLoginId(ul);
                                ulg.setUserRoleId((UserRole) uni.find(1, UserRole.class));
                                uni.create(ulg);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerHoranaTeachers(FileUploadEvent event) {
        try {

            UploadedFile up = event.getFile();

            InputStream stream = new ByteArrayInputStream(up.getContents());

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(stream);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator1 = sheet.iterator();

            while (rowIterator1.hasNext()) {
                Row row = rowIterator1.next();
                //For each row, iterate through all the columns
                //  System.out.println(row.getRowNum());

                if (row.getRowNum() > 0) {

                    String s_id = "";
                    String s_name = "";
                    String name = "";
                    String nic = "";
                    int ed_id = 0;

                    Teacher t = null;

                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        cell.getColumnIndex();

                        if (cell.getColumnIndex() == 3) {
                            s_name = cell.getStringCellValue().trim();
                        }
                        if (cell.getColumnIndex() == 2) {

                            name = cell.getStringCellValue().trim();
                        }
                        if (cell.getColumnIndex() == 1) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                int ni = (int) cell.getNumericCellValue();
                                nic = ni + "";
                            } else {

                                nic = cell.getStringCellValue().trim();
                            }
                        }
                        if (cell.getColumnIndex() == 5) {

                            ed_id = (int) cell.getNumericCellValue();
                        }

                    }
                    System.out.println(s_id + " | " + s_name + " | " + name + " | " + nic + " | " + ed_id);
                    if (!s_name.equals("")) {
                        GeneralOrganizationProfile gop = null;
                        List<GeneralOrganizationProfile> gop_list = uni.searchByQuerySingle("SELECT im FROM GeneralOrganizationProfile im WHERE im.name='" + s_name + "' ");
                        if (gop_list.size() > 0) {
                            gop = gop_list.iterator().next();
                            System.out.println("have " + s_name);
                        } else {
                            gop = new GeneralOrganizationProfile();
                            gop.setAddress1(" ");
                            gop.setAddress2(" ");
                            gop.setAddress3(" ");
                            gop.setPhone(" ");
                            gop.setName(s_name);
                            gop.setOrganizationTypeId((OrganizationType) uni.find(2, OrganizationType.class));
                            gop.setRegistrationTypeId((RegistrationType) uni.find(2, RegistrationType.class));
                            uni.create(gop);

                        }

                        School school = null;
                        List<School> sc_list = uni.searchByQuerySingle("SELECT im FROM School im WHERE im.generalOrganizationProfileId.id='" + gop.getId() + "' ");
                        if (sc_list.size() > 0) {
                            school = sc_list.iterator().next();

                        } else {

                            school = new School();
                            school.setEducationDivisionId((EducationDivision) uni.find(ed_id, EducationDivision.class));
                            school.setGeneralOrganizationProfileId(gop);
                            school.setGradeDescriptionId((GradeDescription) uni.find(11, GradeDescription.class));
                            school.setSchoolCategoryId((SchoolCategory) uni.find(1, SchoolCategory.class));
                            school.setSchoolId(s_id);
                            school.setSchoolTypeId((SchoolType) uni.find(1, SchoolType.class));
                            school.setStudentsCount(2000);
                            school.setTypeBasedOnGradeId((TypeBasedOnGrade) uni.find(5, TypeBasedOnGrade.class));
                            uni.create(school);

                        }
                        GeneralUserProfile si = null;
                        List<GeneralUserProfile> gup_list = uni.searchByQuerySingle("SELECT im FROM GeneralUserProfile im WHERE im.nic='" + nic + "' ");
                        if (gup_list.size() > 0) {
                            si = gup_list.iterator().next();

                        } else {

                            String gender = "1";
                            String title = "Mr";

                            si = new GeneralUserProfile();
                            si.setAddress1(" ");
                            si.setAddress2(" ");
                            si.setAddress3(" ");
                            si.setDob(new Date());
                            si.setEmail(" ");
                            si.setFirstName(" ");
                            si.setGenderId((Gender) uni.find(Integer.parseInt(gender), Gender.class));
                            si.setGeneralOrganizationProfileIdGop(gop);
                            si.setIsActive(1);
                            si.setLastName(" ");
                            si.setMobilePhone(" ");
                            si.setNameWithIn(name);
                            si.setNic(nic);
                            si.setProfileCreatedDate(new Date());
                            si.setTitle(title);
                            uni.create(si);

                        }

                        List<Teacher> tea_list = uni.searchByQuerySingle("SELECT im FROM Teacher im WHERE im.generalUserProfileId.id='" + si.getId() + "' ");
                        if (tea_list.size() > 0) {
                            t = tea_list.iterator().next();
                            Teacher ex = tea_list.iterator().next();
                            if (ex.getSchoolId().getId() == school.getId()) {

                            } else {

                                ex.setIsActive(0);
                                uni.update(ex);

                                DataChangedLogManager gtlm2 = new DataChangedLogManager();
                                gtlm2.setAttributeName("is_active");
                                gtlm2.setComment("");
                                gtlm2.setDate(new Date());
                                gtlm2.setNewData("0");
                                gtlm2.setOldData("1");
                                gtlm2.setReference(ex.getId());
                                gtlm2.setTableManagerId((TableManager) uni.find(6, TableManager.class));
                                gtlm2.setUserLoginId((UserLogin) uni.find(1, UserLogin.class));
                                uni.create(gtlm2);

                                t = new Teacher();
                                t.setGeneralUserProfileId(si);
                                t.setIsActive(1);
                                t.setSchoolId(school);
                                t.setTeacherId("");
                                t.setIsVerified(true);
                                uni.create(t);

                                List<UserLoginGroup> ulg_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "'  and im.projectsId.id='1'");
                                if (ulg_list.size() > 0) {

                                    UserLoginGroup ulg = ulg_list.iterator().next();
                                    ulg.setGeneralOrganizationProfileId(school.getGeneralOrganizationProfileId());
                                    uni.update(ulg);

                                }

                            }

                        } else {
                            t = new Teacher();
                            t.setGeneralUserProfileId(si);
                            t.setIsActive(1);
                            t.setSchoolId(school);
                            t.setTeacherId("");
                            t.setIsVerified(true);
                            uni.create(t);

                        }

                        List<Employee> em_list = uni.searchByQuerySingle("SELECT im FROM Employee im WHERE im.generalUserProfileId.id='" + si.getId() + "' and im.isActive='1' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' ");
                        if (em_list.size() > 0) {

                        } else {

                            Employee e = new Employee();
                            e.setRegDate(new Date());
                            e.setGeneralUserProfileId(si);
                            e.setEmployeeTypeId((EmployeeType) uni.find(1, EmployeeType.class));
                            e.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                            e.setEmployeeCategoryId((EmployeeCategory) uni.find(2, EmployeeCategory.class));
                            e.setEmployeePermanentStstusId((EmployeePermanentStstus) uni.find(1, EmployeePermanentStstus.class));
                            e.setIsActive(1);
                            uni.create(e);

                        }

                        String username = "";
                        String password = "";

                        List<UserLoginGroup> gop_lists = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");
                        if (gop_lists.size() > 0) {
                            UserLoginGroup ulg = gop_lists.iterator().next();
                            ulg.setIsActive(1);
                            ulg.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                            ulg.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                            uni.update(ulg);
                            username = ulg.getUserLoginId().getUsername();
                            password = Security.decrypt(ulg.getUserLoginId().getPassword());
                        } else {

                            UserLogin ul = null;

                            List<UserLogin> gop1_list = uni.searchByQuerySingle("SELECT im FROM UserLogin im WHERE im.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' ");
                            if (gop1_list.size() > 0) {
                                ul = gop1_list.iterator().next();
                                ul.setIsActive(1);
                                ul.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                                ul.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                                uni.update(ul);

                                username = ul.getUsername();
                                password = Security.decrypt(ul.getPassword());

                            } else {

                                if (!t.getGeneralUserProfileId().getNic().equals("")) {

                                    username = t.getGeneralUserProfileId().getNic();

                                    int min = t.getGeneralUserProfileId().getNic().length() - 4;

                                    password = t.getGeneralUserProfileId().getNic().substring(min, t.getGeneralUserProfileId().getNic().length());
                                    System.out.println("awawa");
                                    ul = new UserLogin();
                                    ul.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                                    ul.setGeneralUserProfileId(t.getGeneralUserProfileId());
                                    ul.setGeneratedPassword(Security.encrypt(password));
                                    ul.setIsActive(1);
                                    ul.setPassword(Security.encrypt(password));
                                    ul.setSystemInterfaceId((SystemInterface) uni.find(1, SystemInterface.class));
                                    ul.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                                    ul.setUsername(username);
                                    uni.create(ul);

                                    List<UserLoginGroup> ulg_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");
                                    if (ulg_list.size() == 0) {
                                        UserLoginGroup ulg = new UserLoginGroup();
                                        ulg.setCountAttempt(0);
                                        ulg.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                                        ulg.setIsActive(1);
                                        ulg.setIsFirstTime(1);
                                        ulg.setMaxLoginAttempt(3);
                                        ulg.setProjectsId((Projects) uni.find(1, Projects.class));
                                        ulg.setUserLoginId(ul);
                                        ulg.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                                        uni.create(ulg);
                                    }
                                }
                            }

                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void registerNegamboTeachers(FileUploadEvent event) {
//        try {
//
//            UploadedFile up = event.getFile();
//
//            InputStream stream = new ByteArrayInputStream(up.getContents());
//
//            //Create Workbook instance holding reference to .xlsx file
//            XSSFWorkbook workbook = new XSSFWorkbook(stream);
//
//            //Get first/desired sheet from the workbook
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            //Iterate through each rows one by one
//            Iterator<Row> rowIterator1 = sheet.iterator();
//
//            while (rowIterator1.hasNext()) {
//                Row row = rowIterator1.next();
//                //For each row, iterate through all the columns
//                //  System.out.println(row.getRowNum());
//
//                if (row.getRowNum() > 0) {
//
//                    String s_name = "";
//                    String name = "";
//                    String email = "";
//                    String mobile = " ";
//                    String nic = "";
//                    int ed_id = 0;
//                    String title = "Mr";
//
//                    Teacher t = null;
//
//                    Iterator<Cell> cellIterator = row.cellIterator();
//                    while (cellIterator.hasNext()) {
//                        Cell cell = cellIterator.next();
//                        cell.getColumnIndex();
//
//                        if (cell.getColumnIndex() == 2) {
//                            s_name = cell.getStringCellValue().trim();
//                        }
//                        if (cell.getColumnIndex() == 4) {
//
//                            name = cell.getStringCellValue().trim();
//                        }
//                        if (cell.getColumnIndex() == 1) {
//
//                            nic = cell.getStringCellValue().trim();
//                        }
////                        if (cell.getColumnIndex() == 2) {
////
////                            email = cell.getStringCellValue().trim();
////                        }
////                        if (cell.getColumnIndex() == 3) {
////
////                            mobile = "0" + ((int) cell.getNumericCellValue());
////                        }
//                        if (cell.getColumnIndex() == 5) {
//
//                            ed_id = (int) cell.getNumericCellValue();
//                        }
//
//                    }
////                    if (name.contains("Mr.")) {
////
////                        title = "Mr";
////                        name = name.replace("Mr.", "").trim();
////
////                    } else if (name.contains("Mrs.")) {
////
////                        title = "Mrs";
////                        name = name.replace("Mrs.", "").trim();
////
////                    } else if (name.contains("Ms.")) {
////
////                        title = "Miss";
////                        name = name.replace("Ms.", "").trim();
////
////                    }
//                    if (!s_name.equals("")) {
//                        System.out.println(s_name + " | " + title + " | " + name + " | " + nic + " | " + email + " | " + mobile + " | " + ed_id);
//
//                        GeneralOrganizationProfile gop = null;
//                        List<GeneralOrganizationProfile> gop_list = uni.searchByQuerySingle("SELECT im FROM GeneralOrganizationProfile im WHERE im.name='" + s_name + "' ");
//                        if (gop_list.size() > 0) {
//                            gop = gop_list.iterator().next();
//                            System.out.println("have = " + s_name);
//                        } else {
//                            gop = new GeneralOrganizationProfile();
//                            gop.setAddress1(" ");
//                            gop.setAddress2(" ");
//                            gop.setAddress3(" ");
//                            gop.setPhone(" ");
//                            gop.setName(s_name);
//                            gop.setOrganizationTypeId((OrganizationType) uni.find(2, OrganizationType.class));
//                            gop.setRegistrationTypeId((RegistrationType) uni.find(2, RegistrationType.class));
//                            uni.create(gop);
//
//                        }
//
//                        School school = null;
//                        List<School> sc_list = uni.searchByQuerySingle("SELECT im FROM School im WHERE im.generalOrganizationProfileId.id='" + gop.getId() + "' ");
//                        if (sc_list.size() > 0) {
//                            school = sc_list.iterator().next();
//
//                        } else {
//
//                            school = new School();
//                            school.setEducationDivisionId((EducationDivision) uni.find(ed_id, EducationDivision.class));
//                            school.setGeneralOrganizationProfileId(gop);
//                            school.setGradeDescriptionId((GradeDescription) uni.find(11, GradeDescription.class));
//                            school.setSchoolCategoryId((SchoolCategory) uni.find(1, SchoolCategory.class));
//                            school.setSchoolId("");
//                            school.setSchoolTypeId((SchoolType) uni.find(1, SchoolType.class));
//                            school.setStudentsCount(2000);
//                            school.setTypeBasedOnGradeId((TypeBasedOnGrade) uni.find(5, TypeBasedOnGrade.class));
//                            uni.create(school);
//
//                        }
//                        GeneralUserProfile si = null;
//                        List<GeneralUserProfile> gup_list = uni.searchByQuerySingle("SELECT im FROM GeneralUserProfile im WHERE im.nic='" + nic + "' ");
//                        if (gup_list.size() > 0) {
//                            si = gup_list.iterator().next();
//
//                        } else {
//
//                            String gender = "1";
//                            if (!title.equals("Mr")) {
//                                gender = "2";
//                            }
//
//                            si = new GeneralUserProfile();
//                            si.setAddress1(" ");
//                            si.setAddress2(" ");
//                            si.setAddress3(" ");
//                            si.setDob(new Date());
//                            si.setEmail(email);
//                            si.setFirstName(" ");
//                            si.setGenderId((Gender) uni.find(Integer.parseInt(gender), Gender.class));
//                            si.setGeneralOrganizationProfileIdGop(gop);
//                            si.setIsActive(1);
//                            si.setLastName(" ");
//                            si.setMobilePhone(mobile);
//                            si.setNameWithIn(name);
//                            si.setNic(nic);
//                            si.setProfileCreatedDate(new Date());
//                            si.setTitle(title);
//                            uni.create(si);
//
//                        }
//
//                        List<Teacher> tea_list = uni.searchByQuerySingle("SELECT im FROM Teacher im WHERE im.generalUserProfileId.id='" + si.getId() + "' ");
//                        if (tea_list.size() > 0) {
//                            t = tea_list.iterator().next();
//                            Teacher ex = tea_list.iterator().next();
//                            if (ex.getSchoolId().getId() == school.getId()) {
//
//                            } else {
//
//                                ex.setIsActive(0);
//                                uni.update(ex);
//
//                                DataChangedLogManager gtlm2 = new DataChangedLogManager();
//                                gtlm2.setAttributeName("is_active");
//                                gtlm2.setComment("");
//                                gtlm2.setDate(new Date());
//                                gtlm2.setNewData("0");
//                                gtlm2.setOldData("1");
//                                gtlm2.setReference(ex.getId());
//                                gtlm2.setTableManagerId((TableManager) uni.find(6, TableManager.class));
//                                gtlm2.setUserLoginId((UserLogin) uni.find(1, UserLogin.class));
//                                uni.create(gtlm2);
//
//                                t = new Teacher();
//                                t.setGeneralUserProfileId(si);
//                                t.setIsActive(1);
//                                t.setSchoolId(school);
//                                t.setTeacherId("");
//                                t.setIsVerified(true);
//                                uni.create(t);
//
//                                List<UserLoginGroup> ulg_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "'  and im.projectsId.id='1'");
//                                if (ulg_list.size() > 0) {
//
//                                    UserLoginGroup ulg = ulg_list.iterator().next();
//                                    ulg.setGeneralOrganizationProfileId(school.getGeneralOrganizationProfileId());
//                                    uni.update(ulg);
//
//                                }
//
//                            }
//
//                        } else {
//                            t = new Teacher();
//                            t.setGeneralUserProfileId(si);
//                            t.setIsActive(1);
//                            t.setSchoolId(school);
//                            t.setTeacherId("");
//                            t.setIsVerified(true);
//                            uni.create(t);
//
//                        }
//                        String username = "";
//                        String password = "";
//
//                        List<UserLoginGroup> gop_lists = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");
//                        if (gop_lists.size() > 0) {
//                            UserLoginGroup ulg = gop_lists.iterator().next();
//                            ulg.setIsActive(1);
//                            ulg.setUserRoleId((UserRole) uni.find(4, UserRole.class));
//                            ulg.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
//                            uni.update(ulg);
//                            username = ulg.getUserLoginId().getUsername();
//                            password = Security.decrypt(ulg.getUserLoginId().getPassword());
//                        } else {
//
//                            UserLogin ul = null;
//
//                            List<UserLogin> gop1_list = uni.searchByQuerySingle("SELECT im FROM UserLogin im WHERE im.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' ");
//                            if (gop1_list.size() > 0) {
//                                ul = gop1_list.iterator().next();
//                                ul.setIsActive(1);
//                                ul.setUserRoleId((UserRole) uni.find(4, UserRole.class));
//                                ul.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
//                                uni.update(ul);
//
//                                username = ul.getUsername();
//                                password = Security.decrypt(ul.getPassword());
//
//                            } else {
//
//                                if (!t.getGeneralUserProfileId().getNic().equals("")) {
//
//                                    username = t.getGeneralUserProfileId().getNic();
//
//                                    int min = t.getGeneralUserProfileId().getNic().length() - 4;
//
//                                    password = t.getGeneralUserProfileId().getNic().substring(min, t.getGeneralUserProfileId().getNic().length());
//                                    System.out.println("awawa");
//                                    ul = new UserLogin();
//                                    ul.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
//                                    ul.setGeneralUserProfileId(t.getGeneralUserProfileId());
//                                    ul.setGeneratedPassword(Security.encrypt(password));
//                                    ul.setIsActive(1);
//                                    ul.setPassword(Security.encrypt(password));
//                                    ul.setSystemInterfaceId((SystemInterface) uni.find(1, SystemInterface.class));
//                                    ul.setUserRoleId((UserRole) uni.find(4, UserRole.class));
//                                    ul.setUsername(username);
//                                    uni.create(ul);
//
//                                    List<UserLoginGroup> ulg_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");
//                                    if (ulg_list.size() == 0) {
//                                        UserLoginGroup ulg = new UserLoginGroup();
//                                        ulg.setCountAttempt(0);
//                                        ulg.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
//                                        ulg.setIsActive(1);
//                                        ulg.setIsFirstTime(1);
//                                        ulg.setMaxLoginAttempt(3);
//                                        ulg.setProjectsId((Projects) uni.find(1, Projects.class));
//                                        ulg.setUserLoginId(ul);
//                                        ulg.setUserRoleId((UserRole) uni.find(4, UserRole.class));
//                                        uni.create(ulg);
//                                    }
//                                }
//                            }
//
//                        }
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void registerAdmins(FileUploadEvent event) {
        try {

            UploadedFile up = event.getFile();

            InputStream stream = new ByteArrayInputStream(up.getContents());

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(stream);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator1 = sheet.iterator();

            while (rowIterator1.hasNext()) {
                Row row = rowIterator1.next();
                //For each row, iterate through all the columns
                //  System.out.println(row.getRowNum());

                if (row.getRowNum() > 0) {

                    String name = "";
                    String nic = "";
                    int org = 0;

                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        cell.getColumnIndex();

                        if (cell.getColumnIndex() == 1) {

                            name = cell.getStringCellValue().trim();
                        }
                        if (cell.getColumnIndex() == 2) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                int ni = (int) cell.getNumericCellValue();
                                nic = ni + "";
                            } else {

                                nic = cell.getStringCellValue().trim();
                            }
                        }
                        if (cell.getColumnIndex() == 4) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                org = (int) cell.getNumericCellValue();
                            } else {

                                org = Integer.parseInt(cell.getStringCellValue().trim());
                            }
                        }

                    }

//                    
                    if (!nic.equals("")) {
                        System.out.println(name + " | " + nic);
                        GeneralUserProfile si = null;
                        List<GeneralUserProfile> gup_list = uni.searchByQuerySingle("SELECT im FROM GeneralUserProfile im WHERE im.nic='" + nic + "' ");
                        if (gup_list.size() > 0) {
                            si = gup_list.iterator().next();

                        } else {

                            String gender = "1";
                            String title = "Mr";

                            if (name.contains("Mrs.")) {

                                title = "Mrs";
                                name = name.replace("Mrs.", "").trim();
                            }
                            if (name.contains("Mr.")) {

                                title = "Mr";
                                name = name.replace("Mr.", "").trim();
                            }
                            if (name.contains("Miss.")) {

                                title = "Miss";
                                name = name.replace("Miss.", "").trim();
                            }
                            System.out.println("name " + name);
                            si = new GeneralUserProfile();
                            si.setAddress1(" ");
                            si.setAddress2(" ");
                            si.setAddress3(" ");
                            si.setDob(new Date());
                            si.setEmail(" ");
                            si.setFirstName(" ");
                            si.setGenderId((Gender) uni.find(Integer.parseInt(gender), Gender.class));
                            si.setGeneralOrganizationProfileIdGop((GeneralOrganizationProfile) uni.find(org, GeneralOrganizationProfile.class));
                            si.setIsActive(1);
                            si.setLastName(" ");
                            si.setMobilePhone(" ");
                            si.setNameWithIn(name);
                            si.setNic(nic);
                            si.setProfileCreatedDate(new Date());
                            si.setTitle(title);
                            uni.create(si);

                        }
                        List<Employee> em_list = uni.searchByQuerySingle("SELECT im FROM Employee im WHERE im.generalUserProfileId.id='" + si.getId() + "' and im.isActive='1' and im.generalOrganizationProfileId.id='" + org + "' ");
                        if (em_list.size() > 0) {

                        } else {

                            Employee e = new Employee();
                            e.setRegDate(new Date());
                            e.setGeneralUserProfileId(si);
                            e.setEmployeeTypeId((EmployeeType) uni.find(1, EmployeeType.class));
                            e.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uni.find(org, GeneralOrganizationProfile.class));
                            e.setEmployeeCategoryId((EmployeeCategory) uni.find(2, EmployeeCategory.class));
                            e.setEmployeePermanentStstusId((EmployeePermanentStstus) uni.find(1, EmployeePermanentStstus.class));
                            e.setIsActive(1);
                            uni.create(e);

                        }

                        String username = "";
                        String password = "";

                        List<UserLoginGroup> gop_lists = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + si.getId() + "' and im.generalOrganizationProfileId.id='" + org + "' and im.projectsId.id='1'");
                        if (gop_lists.size() > 0) {
                            UserLoginGroup ulg = gop_lists.iterator().next();
                            ulg.setIsActive(1);
                            ulg.setUserRoleId((UserRole) uni.find(1, UserRole.class));
                            ulg.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uni.find(org, GeneralOrganizationProfile.class));
                            uni.update(ulg);
                            username = ulg.getUserLoginId().getUsername();
                            password = Security.decrypt(ulg.getUserLoginId().getPassword());
                        } else {

                            UserLogin ul = null;

                            List<UserLogin> gop1_list = uni.searchByQuerySingle("SELECT im FROM UserLogin im WHERE im.generalUserProfileId.id='" + si.getId() + "' ");
                            if (gop1_list.size() > 0) {
                                ul = gop1_list.iterator().next();
                                ul.setIsActive(1);
                                ul.setUserRoleId((UserRole) uni.find(1, UserRole.class));
                                ul.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uni.find(org, GeneralOrganizationProfile.class));
                                uni.update(ul);

                                username = ul.getUsername();
                                password = Security.decrypt(ul.getPassword());

                            } else {

                                if (!si.getNic().equals("")) {

                                    username = si.getNic();

                                    int min = si.getNic().length() - 4;

                                    password = si.getNic().substring(min, si.getNic().length());
                                    System.out.println("awawa");
                                    ul = new UserLogin();
                                    ul.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uni.find(org, GeneralOrganizationProfile.class));
                                    ul.setGeneralUserProfileId(si);
                                    ul.setGeneratedPassword(Security.encrypt(password));
                                    ul.setIsActive(1);
                                    ul.setPassword(Security.encrypt(password));
                                    ul.setSystemInterfaceId((SystemInterface) uni.find(1, SystemInterface.class));
                                    ul.setUserRoleId((UserRole) uni.find(1, UserRole.class));
                                    ul.setUsername(username);
                                    uni.create(ul);

                                    List<UserLoginGroup> ulg_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + si.getId() + "' and im.generalOrganizationProfileId.id='" + org + "' and im.projectsId.id='1'");
                                    if (ulg_list.size() == 0) {
                                        UserLoginGroup ulg = new UserLoginGroup();
                                        ulg.setCountAttempt(0);
                                        ulg.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uni.find(org, GeneralOrganizationProfile.class));
                                        ulg.setIsActive(1);
                                        ulg.setIsFirstTime(1);
                                        ulg.setMaxLoginAttempt(3);
                                        ulg.setProjectsId((Projects) uni.find(1, Projects.class));
                                        ulg.setUserLoginId(ul);
                                        ulg.setUserRoleId((UserRole) uni.find(1, UserRole.class));
                                        uni.create(ulg);
                                    }
                                }
                            }

                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createEmployees() {
        List<UserLoginGroup> sm_list = uni.searchByQuery("SELECT u FROM UserLoginGroup u where u.isActive='1'");
        for (UserLoginGroup t : sm_list) {

            List<Employee> gop1_list = uni.searchByQuerySingle("SELECT im FROM Employee im WHERE im.generalUserProfileId.id='" + t.getUserLoginId().getGeneralUserProfileId().getId() + "' and im.isActive='1' and im.generalOrganizationProfileId.id='" + t.getGeneralOrganizationProfileId().getId() + "' ");
            if (gop1_list.size() > 0) {

                Employee e = gop1_list.iterator().next();
                e.setEmployeeCategoryId((EmployeeCategory) uni.find(2, EmployeeCategory.class));
                uni.update(e);

            } else {

                Employee e = new Employee();
                e.setRegDate(new Date());
                e.setGeneralUserProfileId(t.getUserLoginId().getGeneralUserProfileId());
                e.setEmployeeTypeId((EmployeeType) uni.find(1, EmployeeType.class));
                e.setGeneralOrganizationProfileId(t.getGeneralOrganizationProfileId());
                e.setEmployeeCategoryId((EmployeeCategory) uni.find(2, EmployeeCategory.class));
                e.setEmployeePermanentStstusId((EmployeePermanentStstus) uni.find(1, EmployeePermanentStstus.class));
                e.setIsActive(1);
                uni.create(e);

            }
        }
        List<Teacher> t_list = uni.searchByQuery("SELECT u FROM Teacher u where u.isActive='1'");
        for (Teacher t : t_list) {

            List<Employee> gop1_list = uni.searchByQuerySingle("SELECT im FROM Employee im WHERE im.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.isActive='1' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' ");
            if (gop1_list.size() > 0) {
                Employee e = gop1_list.iterator().next();
                e.setEmployeeCategoryId((EmployeeCategory) uni.find(1, EmployeeCategory.class));
                uni.update(e);
            } else {

                Employee e = new Employee();
                e.setRegDate(new Date());
                e.setGeneralUserProfileId(t.getGeneralUserProfileId());
                e.setEmployeeTypeId((EmployeeType) uni.find(1, EmployeeType.class));
                e.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                e.setEmployeeCategoryId((EmployeeCategory) uni.find(1, EmployeeCategory.class));
                e.setEmployeePermanentStstusId((EmployeePermanentStstus) uni.find(1, EmployeePermanentStstus.class));
                e.setIsActive(1);
                uni.create(e);

            }
        }
    }

    public void checkUserLogin() {
        List<UserLogin> sm_list = uni.searchByQuery("SELECT u FROM UserLogin u where u.isActive='1' and u.userRoleId.id='4'");
        for (UserLogin t : sm_list) {
            List<UserLoginGroup> sm1_list = uni.searchByQuery("SELECT u FROM UserLoginGroup u where u.userLoginId.id='" + t.getId() + "' and u.projectsId.id='1'");
            if (sm1_list.size() == 0) {

                UserLoginGroup ul = new UserLoginGroup();
                ul.setGeneralOrganizationProfileId(t.getGeneralOrganizationProfileId());
                ul.setIsActive(1);
                ul.setProjectsId((Projects) uni.find(1, Projects.class));
                ul.setUserLoginId(t);
                ul.setUserRoleId((UserRole) uni.find(4, UserRole.class));
                uni.create(ul);
            }
        }

    }

    public void saveTeacherMean(int year, int term) {
        System.out.println("awapp");
        try {

            Terms trm = (Terms) uni.find(term, Terms.class);
            Year yr = (Year) uni.find(year, Year.class);

//            List<TeacherMeanValues> teacherMeanValues = new ArrayList();
            List<GradeClassSubjectTeacher> list_gcst_individualy = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + year + "' and g.teacherId!=null and g.teacherId.isActive='1' and g.teacherId.isActive='1'  group by g.teacherId.id");
            for (GradeClassSubjectTeacher gcst_individualy : list_gcst_individualy) {  // Retrive group of subject Teachers

//            System.out.println("gcst_individualy " + gcst_individualy.getTeacherId().getId());
                List<GradeClassSubjectTeacher> list_gcst_subjects = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + year + "'  and g.teacherId.id='" + gcst_individualy.getTeacherId().getId() + "'  group by g.gradeClassHasSubjectsId.subjectsId.id");
                for (GradeClassSubjectTeacher gcst_sub : list_gcst_subjects) {  // Retrive Selected Teacher's Subjects
                    System.out.println("gcst_sub.getGradeClassHasSubjectsId().getSubjectsId().getId() " + gcst_sub.getGradeClassHasSubjectsId().getId());
                    System.out.println("gcst_individualy.getTeacherId().getId() " + gcst_individualy.getTeacherId().getId());
                    List<GradeClassSubjectTeacher> list_gcst_grades = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + year + "'  and g.teacherId.id='" + gcst_individualy.getTeacherId().getId() + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + gcst_sub.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' group by g.gradeClassStreamManagerId.id "); // 
                    System.out.println("list_gcst_grades " + list_gcst_grades.size());
                    if (list_gcst_grades.size() > 0) {
                        for (GradeClassSubjectTeacher gcst_grade : list_gcst_grades) { // Retrive Selected Subjects's Classes

//                            double meanGrade = stored.get_selected_class_subject_mean(year, term, gcst_grade.getGradeClassStreamManagerId().getId(), gcst_sub.getGradeClassHasSubjectsId().getSubjectsId().getId());
//                    double GradeTotMarks = 0.0;
//                    List<GradeClassStudentsHasSubjects> list_gcshs_grade = uni.searchByQuery("SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassHasSubjectsId.id='" + gcst_sub.getGradeClassHasSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.id='" + gcst_grade.getGradeClassStreamManagerId().getId() + "'  and g.gradeClassStudentsId.isRemoved='0'");
//
//                    for (GradeClassStudentsHasSubjects gcshs : list_gcshs_grade) {
//                        String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcshs.getGradeClassStudentsId().getId() + "' and g.termsId.id='" + term + "' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "'  and g.isRemoved='0' ";
//                        List<StudentMarks> listm = uni.searchByQuerySingle(querym);
//                        if (listm.size() > 0) {
//
//                            GradeTotMarks += listm.iterator().next().getMarks();
//                        }
//                    }
//                    if (list_gcshs_grade.size() > 0) {
//
//                        meanGrade = GradeTotMarks / list_gcshs_grade.size();
//                    }
//                            teacherMeanValues.add(new TeacherMeanValues(gcst_individualy.getTeacherId(), gcst_sub.getGradeClassHasSubjectsId().getSubjectsId(), gcst_grade.getGradeClassStreamManagerId().getGradeClassStreamId(), meanGrade, year, term));
                            TeacherClassSubjectMean ea = new TeacherClassSubjectMean();
                            ea.setGradeClassStreamId(gcst_grade.getGradeClassStreamManagerId().getGradeClassStreamId());
                            ea.setSubjectsId(gcst_sub.getGradeClassHasSubjectsId().getSubjectsId());
                            ea.setTeacherId(gcst_individualy.getTeacherId());
                            ea.setMeanValue(stored.get_selected_class_subject_mean(year, term, gcst_grade.getGradeClassStreamManagerId().getId(), gcst_sub.getGradeClassHasSubjectsId().getSubjectsId().getId()));
                            ea.setTermsId(trm);
                            ea.setYearId(yr);
                            uni.create(ea);

                            System.out.println(gcst_individualy.getTeacherId().getId() + " | " + gcst_grade.getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + "-" + gcst_grade.getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName() + " | " + gcst_sub.getGradeClassHasSubjectsId().getSubjectsId().getName());
                        }
                    }

                }
            }
//            comdiv.saveTeacherMean(teacherMeanValues, year, term);
            System.out.println("iwaraaai " + new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class TeacherMeanValues implements Serializable {

        private Teacher teacher;
        private Subjects subject;
        private GradeClassStream classID;
        private double mean;
        private int year;
        private int term;

        public TeacherMeanValues() {
        }

        public TeacherMeanValues(Teacher teacher, Subjects subject, GradeClassStream classID, double mean, int year, int term) {
            this.teacher = teacher;
            this.subject = subject;
            this.classID = classID;
            this.mean = mean;
            this.year = year;
            this.term = term;

        }

        public double getMean() {
            return mean;
        }

        public void setMean(double mean) {
            this.mean = mean;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getTerm() {
            return term;
        }

        public void setTerm(int term) {
            this.term = term;
        }

        public Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }

        public GradeClassStream getClassID() {
            return classID;
        }

        public void setClassID(GradeClassStream classID) {
            this.classID = classID;
        }

        public Subjects getSubject() {
            return subject;
        }

        public void setSubject(Subjects subject) {
            this.subject = subject;
        }
    }

    public static class YearTermList implements Serializable {

        private int year;
        private int term;

        public YearTermList() {
        }

        public YearTermList(int year, int term) {

            this.year = year;
            this.term = term;

        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getTerm() {
            return term;
        }

        public void setTerm(int term) {
            this.term = term;
        }
    }
}
