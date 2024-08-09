package com.ejb.model.scheduler;

import com.ejb.model.businesslogic.mailsend;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.FingerPrintRegionUser;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TeacherAttendance;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class weeklyTeacherAttendanceReportSheduler {

    @EJB
    private UniDBLocal uni;

    @Schedule(dayOfWeek = "Fri", hour = "11", minute = "35", second = "00", persistent = false)
    public void sendWeeklyAttendance() throws Exception {

        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate endDate = startDate.plusDays(4);

//        LocalDate startDate = LocalDate.of(2024, 6, 10);
//        LocalDate endDate = LocalDate.of(2024, 6, 14);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedStartDate = startDate.format(dateFormatter);
        String formattedEndDate = endDate.format(dateFormatter);

        String get_finger_print_users = "SELECT g FROM FingerPrintRegionUser g WHERE g.isActive='1' ";
        List<FingerPrintRegionUser> fpru_list = uni.searchByQuery(get_finger_print_users);
        if (fpru_list != null) {

            for (FingerPrintRegionUser fingerPrintRegionUser : fpru_list) {
                String getTeachersQuery = "SELECT g FROM Teacher g WHERE g.generalUserProfileId.id='" + fingerPrintRegionUser.getGeneralUserProfileGupId().getId() + "' AND g.schoolId.id='100' AND g.isActive='1'";
                List<Teacher> teacherList = uni.searchByQuery(getTeachersQuery);

                for (Teacher teacher : teacherList) {
                    String teacherEmail = teacher.getGeneralUserProfileId().getEmail();
                    String ccEmails = "cc1@example.com, cc2@example.com";
                    String teacherName = teacher.getGeneralUserProfileId().getNameWithIn();

                    StringBuilder emailBody = new StringBuilder();
                    emailBody.append("<!DOCTYPE html>\n")
                            .append("<html lang=\"en\">\n")
                            .append("<head></head>\n")
                            .append("<body style=\"background-color: rgb(255, 255, 255); width: 500px; border: 5px;\">\n")
                            .append("<h3>Dear ").append(teacherName).append("</h3>\n")
                            .append("<div style=\"color:#121010;direction:ltr;font-family:'Helvetica Neue', Helvetica, Arial, sans-serif;font-size:16px;font-weight:400;letter-spacing:0px;line-height:120%;text-align:left;mso-line-height-alt:19.2px;\">\n")
                            .append("<p style=\"margin: 0; margin-bottom: 16px;\">Here are Your Attendance Details from <span style=\"word-break: break-word; color: #0000ff;\"><strong>").append(formattedStartDate)
                            .append("</strong></span> to <span style=\"word-break: break-word; color: #0000ff;\"><strong>").append(formattedEndDate).append("</strong></span>.</p>\n")
                            .append("<p style=\"margin: 0;\">If there is any issue, please feel free to contact us.</p>\n")
                            .append("</div>\n")
                            .append("<table style=\"width: 100%;\">\n")
                            .append("<tr>\n")
                            .append("<td class=\"column column-1\" width=\"50%\"><span>Signed ID :</span><span>").append(teacher.getTeacherId()).append("</span></td>\n")
                            .append("</tr>\n")
                            .append("</table>\n")
                            .append("<table style=\"width: 100%; margin-top: 10px; background-color:black;\">\n")
                            .append("<tr style=\"background-color: #b2beb5; color: #ffffff; text-align: center;\">\n")
                            .append("<td class=\"column column-1\" width=\"40%\"></td>\n")
                            .append("<td class=\"column column-2\" width=\"30%\"><h4>In Time</h4></td>\n")
                            .append("<td class=\"column column-3\" width=\"30%\"><h4>Out Time</h4></td>\n")
                            .append("</tr>\n");

                 
                    String attendanceRows = generateAttendanceRows(teacher, startDate, endDate);
                    emailBody.append(attendanceRows);

                    emailBody.append("</table><br>\n")
                            .append("<div style=\"text-align: center; width: 100%;\">\n")
                            .append("<h3>Thank You</h3>\n")
                            .append("</div>\n")
                            .append("</body>\n")
                            .append("</html>");

//                    String subject = "Monthly Attendance Report - " + teacherName;

                    mailsend.Send1("noreply@srilankasoftwarevalley.lk", "SLsvnorep@jiat2022", teacherEmail, ccEmails, "Weekly Attendance Report '"+teacherName+"'",emailBody.toString(), null);
                    System.out.println("Send Mail successfully");
                }
            }
        }

    }

    private String generateAttendanceRows(Teacher teacher, LocalDate startDate, LocalDate endDate) {
        StringBuilder rows = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            String dateStr = date.format(dateFormatter);

            String getAttendanceQuery = "SELECT g FROM TeacherAttendance g WHERE g.date = '" + dateStr + "' AND g.teacherId.id = '" + teacher.getId() + "'  ";
            List<TeacherAttendance> attendanceList = uni.searchByQuery(getAttendanceQuery);

            System.out.println("Query: " + getAttendanceQuery);
            System.out.println("Attendance List: " + (attendanceList != null ? attendanceList.size() : "null"));

            String inTime = "N/A";
            String outTime = "N/A";
            if (attendanceList != null && !attendanceList.isEmpty()) {
                TeacherAttendance attendance = attendanceList.get(0);
                inTime = attendance.getIn_time() != null ? timeFormat.format(attendance.getIn_time()) : "N/A";
                outTime = attendance.getOut_time() != null ? timeFormat.format(attendance.getOut_time()) : "N/A";

            }

            rows.append("<tr style=\"background-color: azure; text-align: center;\">\n")
                    .append("<td class=\"column column-1\" style=\"display: flex; flex-direction: row; text-align: center;\" width=\"40%\">\n")
                    .append("<h4 style=\"text-align: center;\">").append(date.getDayOfWeek().name()).append("</h4>\n")
                    .append("&nbsp;<h4>(").append(date.format(DateTimeFormatter.ofPattern("MM/dd"))).append(")</h4>\n")
                    .append("</td>\n")
                    .append("<td class=\"column column-2\" style=\"text-align: center;\" width=\"30%\"><span>").append(inTime).append("</span></td>\n")
                    .append("<td class=\"column column-3\" style=\"text-align: center;\" width=\"30%\"><span>").append(outTime).append("</span></td>\n")
                    .append("</tr>\n");
        }

        return rows.toString();
    }
}
