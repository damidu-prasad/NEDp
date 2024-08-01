/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.businesslogic;

import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.LoginSession;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author Thilini Madagama
 */
@Stateless
@LocalBean
public class SaveLogout {

    @EJB
    private UniDBLocal uni;

    public void validateLogout(int ls_id) {
        System.out.println("lsssssssssss "+ls_id);
        LoginSession ls = (LoginSession) uni.find(ls_id, LoginSession.class);
        if (ls != null) {

            ls.setEndTime(new Date());
            uni.update(ls);
        }

    }

}
