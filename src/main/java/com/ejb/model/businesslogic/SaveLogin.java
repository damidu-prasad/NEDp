/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.businesslogic;

import com.ejb.model.common.Security;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.UserLoginGroup;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Thilini
 */
@Stateless
@LocalBean

public class SaveLogin {

    @EJB
    private UniDBLocal uniDB;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public ArrayList<String> ValidateUserLogin(String username, String password, HttpServletRequest request) {

        String response = "";
        String ls_id = "";
        ArrayList<String> l_s = new ArrayList<String>();
        try {

            List<UserLoginGroup> ul_list = uniDB.searchByQuery("SELECT u FROM UserLoginGroup u WHERE u.userLoginId.username='" + username + "' and u.projectsId.id='1'");
            if (ul_list.isEmpty()) {

                response = "Invalid Username !";

            } else {
                UserLoginGroup ulg = ul_list.iterator().next();

                if (password.equals(Security.decrypt(ulg.getUserLoginId().getPassword()))) {

                    if (ulg.getIsActive() == 0) {
                        response = "Your account has suspended !";
                    } else {

                        LoginSession ls = new LoginSession();
                        ls.setGeneralOrganizationProfileId(ulg.getGeneralOrganizationProfileId());
                        ls.setUserLoginId(ulg.getUserLoginId());
                        ls.setUserLoginGroupId(ulg);
                        ls.setStartTime(new Date());
                        ls.setIp(request.getRemoteAddr());
                        uniDB.create(ls);

                        ls_id = ls.getId() + "";

                        response = "OK";
                    }

                } else {
                    response = "Invalid Password !";
                }
            }

            l_s.add(response);
            l_s.add(ls_id);
        } catch (Exception e) {
            response = "Error Occured !";
            l_s.add(response);
            e.printStackTrace();
        }

//        Object userlogin = uniDB.searchByQuerySingle("SELECT u FROM UserLogin u WHERE u.username='" + username + "' ");
        return l_s;
    }

}
