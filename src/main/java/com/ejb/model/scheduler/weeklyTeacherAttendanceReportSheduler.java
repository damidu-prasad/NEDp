package com.ejb.model.scheduler;

import com.ejb.model.businesslogic.mailsend;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.mail.MessagingException;

@Stateless
@Startup
public class weeklyTeacherAttendanceReportSheduler {

    @Schedule(dayOfWeek = "Sun", hour = "00", minute = "03", second = "0", persistent = false)
    public void sendWeeklyAttendance() throws MessagingException {

        try {
            System.out.println("done");

            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Calculate the start and end dates of the week
            LocalDate startDate = currentDate.minusDays(currentDate.getDayOfWeek().getValue() - 1);
            LocalDate endDate = startDate.plusDays(6);

            // Format the dates for the email
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String formattedStartDate = startDate.format(dateFormatter);
            String formattedEndDate = endDate.format(dateFormatter);

            // Get the current date and time for the email generation timestamp
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String formattedCurrentDateTime = currentDateTime.format(dateTimeFormatter);

            // Construct the email content
            String emailBody = "<!DOCTYPE html>\n"
                    + "<html lang=\"en\">\n"
                    + "\n"
                    + "<head>\n"
                    + "\n"
                    + "</head>\n"
                    + "\n"
                    + "<body style=\"background-color: rgb(255, 255, 255); width: 500px; border: 5px;\">\n"
                    + "    <h3>Dear Teacher </h3>\n"
                    + "    <div\n"
                    + "        style=\"color:#121010;direction:ltr;font-family:'Helvetica Neue', Helvetica, Arial, sans-serif;font-size:16px;font-weight:400;letter-spacing:0px;line-height:120%;text-align:left;mso-line-height-alt:19.2px;\">\n"
                    + "        <p style=\"margin: 0; margin-bottom: 16px;\">Hear are Your Attendance Details\n"
                    + "            from <span style=\"word-break: break-word; color: #0000ff;\"><strong><span\n"
                    + "                        style=\"word-break: break-word; background-color: #ffffff;\">2024/08/03</span></strong></span>\n"
                    + "            to <span style=\"word-break: break-word; color: #0000ff;\"><strong>2024/08/08</strong>.</span></p>\n"
                    + "        <p style=\"margin: 0;\">If there is any issue. Please feel free to contact us.</p>\n"
                    + "\n"
                    + "    </div>\n"
                    + "    <table style=\"width: 100%;\">\n"
                    + "        <tr>\n"
                    + "            <td class=\"column column-1\" width=\"50%\"><span>ID :</span><span>000125</span></td>\n"
                    + "            <td class=\"column column-2\" width=\"50%\"><span>Created at : </span><span>2024/08/03</span>&nbsp;<span>00:00:00</span></td>\n"
                    + "        </tr>\n"
                    + "    </table>\n"
                    + "    <table style=\"width: 100%; margin-top: 10px; background-color: azure;\">\n"
                    + "        <tr style=\"background-color: #0000ff; color: #ffffff; text-align: center; \">\n"
                    + "            <td class=\"column column-1\" width=\"40%\"></td>\n"
                    + "            <td class=\"column column-2\" width=\"30%\"><h4>In Time</h4></td>\n"
                    + "            <td class=\"column column-3\" width=\"30%\"><h4>Out Time</h4></td>\n"
                    + "        </tr>\n"
                    + "        <tr>\n"
                    + "            <td class=\"column column-1\" width=\"40%\"><h5>Monday</h5></td>\n"
                    + "            <td class=\"column column-2\" style=\"text-align: center;\" width=\"30%\"><span>07:00:05</span></td>\n"
                    + "            <td class=\"column column-3\" style=\"text-align: center;\"  width=\"30%\"><span>14:00:05</span></td>\n"
                    + "        </tr>\n"
                    + "        <tr>\n"
                    + "            <td class=\"column column-1\" width=\"40%\"><h5>Tuesday</h5></td>\n"
                    + "            <td class=\"column column-2\" style=\"text-align: center;\"  width=\"30%\"><span>07:00:05</span></td>\n"
                    + "            <td class=\"column column-3\" style=\"text-align: center;\"  width=\"30%\"><span>14:00:05</span></td>\n"
                    + "        </tr>\n"
                    + "        <tr>\n"
                    + "            <td class=\"column column-1\" width=\"40%\"><h5>Wednesday</h5></td>\n"
                    + "            <td class=\"column column-2\" style=\"text-align: center;\"  width=\"30%\"><span>07:00:05</span></td>\n"
                    + "            <td class=\"column column-3\" style=\"text-align: center;\"  width=\"30%\"><span>14:00:05</span></td>\n"
                    + "        </tr>\n"
                    + "        <tr>\n"
                    + "            <td class=\"column column-1\" width=\"40%\"><h5>Thursday</h5></td>\n"
                    + "            <td class=\"column column-2\" style=\"text-align: center;\"  width=\"30%\"><span>07:00:05</span></td>\n"
                    + "            <td class=\"column column-3\" style=\"text-align: center;\"  width=\"30%\"><span>14:00:05</span></td>\n"
                    + "        </tr>\n"
                    + "        <tr>\n"
                    + "            <td class=\"column column-1\" width=\"40%\"><h5>Friday</h5></td>\n"
                    + "            <td class=\"column column-2\" style=\"text-align: center;\"  width=\"30%\"><span>07:00:05</span></td>\n"
                    + "            <td class=\"column column-3\" style=\"text-align: center;\"  width=\"30%\"><span>14:00:05</span></td>\n"
                    + "        </tr>\n"
                    + "    </table><br>\n"
                    + "    <div style=\"text-align: center; width: 100%;\"><h3>Thank You</h3></div>\n"
                    + "    \n"
                    + "    \n"
                    + "</body>\n"
                    + "\n"
                    + "</html>";

            mailsend.Send1("noreply@srilankasoftwarevalley.lk", "SLsvnorep@jiat2022", "damiduprasad2022@gmail.com", "damiduprasadjayarathna@gmail.com", "Weekly Attendance Report", emailBody, null);

        } catch (MessagingException e) {
            e.printStackTrace(); // Print the stack trace of the caught exception
        }
    }
}
