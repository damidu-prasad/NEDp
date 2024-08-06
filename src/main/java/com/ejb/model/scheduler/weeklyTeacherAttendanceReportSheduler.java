package com.ejb.model.scheduler;

import com.ejb.model.businesslogic.mailsend;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.FingerPrintRegionUser;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TeacherAttendance;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class weeklyTeacherAttendanceReportSheduler {

    @EJB
    private UniDBLocal uni;

    @Schedule(dayOfWeek = "Tue", hour = "06", minute = "25", second = "00", persistent = false)
    public void sendWeeklyAttendance() throws MessagingException {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate endDate = startDate.plusDays(4); 

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedStartDate = startDate.format(dateFormatter);
        String formattedEndDate = endDate.format(dateFormatter);

        
        String getTeachersQuery = "SELECT g FROM Teacher g WHERE g.isActive='1'";
        List<Teacher> teacherList = uni.searchByQuery(getTeachersQuery);

        for (Teacher teacher : teacherList) {
//            String teacherEmail = teacher.getGeneralUserProfileId().getEmail();
            String teacherEmail = "damiduprasadjayarathna@gmail.com , damiduprasad2022@gmail.com";

            String ccEmails = "cc1@example.com, cc2@example.com";

            StringBuilder emailBody = new StringBuilder();
            emailBody.append("<!DOCTYPE html>\n")
                    .append("<html lang=\"en\">\n")
                    .append("<head></head>\n")
                    .append("<body style=\"background-color: rgb(255, 255, 255); width: 500px; border: 5px;\">\n")
                    .append("<h3>Dear ").append(teacher.getGeneralUserProfileId().getNameWithIn()).append("</h3>\n")
                    .append("<div style=\"color:#121010;direction:ltr;font-family:'Helvetica Neue', Helvetica, Arial, sans-serif;font-size:16px;font-weight:400;letter-spacing:0px;line-height:120%;text-align:left;mso-line-height-alt:19.2px;\">\n")
                    .append("<p style=\"margin: 0; margin-bottom: 16px;\">Here are Your Attendance Details from <span style=\"word-break: break-word; color: #0000ff;\"><strong>").append(formattedStartDate)
                    .append("</strong></span> to <span style=\"word-break: break-word; color: #0000ff;\"><strong>").append(formattedEndDate).append("</strong></span>.</p>\n")
                    .append("<p style=\"margin: 0;\">If there is any issue, please feel free to contact us.</p>\n")
                    .append("</div>\n")
                    .append("<table style=\"width: 100%;\">\n")
                    .append("<tr>\n")
                    .append("<td class=\"column column-1\" width=\"50%\"><span>ID :</span><span>").append(teacher.getId()).append("</span></td>\n")
                    .append("<td class=\"column column-2\" width=\"50%\"><span>Created at :</span><span>").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))).append("</span></td>\n")
                    .append("</tr>\n")
                    .append("</table>\n")
                    .append("<table style=\"width: 100%; margin-top: 10px; background-color:black;\">\n")
                    .append("<tr style=\"background-color: #0000ff; color: #ffffff; text-align: center;\">\n")
                    .append("<td class=\"column column-1\" width=\"40%\"></td>\n")
                    .append("<td class=\"column column-2\" width=\"30%\"><h4>In Time</h4></td>\n")
                    .append("<td class=\"column column-3\" width=\"30%\"><h4>Out Time</h4></td>\n")
                    .append("</tr>\n");

            
            for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
                String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                String getAttendanceQuery = "SELECT g FROM TeacherAttendance g WHERE g.date= '" + dateStr + "' AND g.teacherId.id = '" + teacher.getId() + "'";
                List<TeacherAttendance> attendanceList = uni.searchByQuery(getAttendanceQuery);

                String inTime = "N/A";
                String outTime = "N/A";
                if (!attendanceList.isEmpty()) {
                    TeacherAttendance attendance = attendanceList.get(0);
                    inTime = attendance.getIn_time() != null ? attendance.getIn_time().toString() : "N/A";
                    outTime = attendance.getOut_time() != null ? attendance.getOut_time().toString() : "N/A";
                }

                emailBody.append("<tr style=\"background-color: azure;\">\n")
                        .append("<td class=\"column column-1\" style=\"display: flex; flex-direction: row;\" width=\"40%\">\n")
                        .append("<h5>").append(date.getDayOfWeek().name()).append("</h5>\n")
                        .append("&nbsp;<h6>(").append(date.format(DateTimeFormatter.ofPattern("MM/dd"))).append(")</h6>\n")
                        .append("</td>\n")
                        .append("<td class=\"column column-2\" style=\"text-align: center;\" width=\"30%\"><span>").append(inTime).append("</span></td>\n")
                        .append("<td class=\"column column-3\" style=\"text-align: center;\" width=\"30%\"><span>").append(outTime).append("</span></td>\n")
                        .append("</tr>\n");
            }

            emailBody.append("</table><br>\n")
                    .append("<div style=\"text-align: center; width: 100%;\">\n")
                    .append("<h3>Thank You</h3>\n")
                    .append("</div>\n")
                    .append("</body>\n")
                    .append("</html>");

            mailsend.Send1("noreply@srilankasoftwarevalley.lk", "SLsvnorep@jiat2022", teacherEmail, ccEmails, "Weekly Attendance Report", emailBody.toString(), null);
            System.out.println("send mail sucess");
        }
    }
}
