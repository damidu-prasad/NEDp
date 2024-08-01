/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.common;

import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.UserLogin;

/**
 *
 * @author Medha
 */
public class Login {

    private LoginSession ls;

    public Login(LoginSession ls) {
        this.ls = ls;
    }

    /**
     * @return the ls
     */
    public LoginSession getLoginSession() {
        return ls;
    }

    /**
     * @param ls the ls to set
     */
    public void setLs(LoginSession ls) {
        this.ls = ls;
    }

    public UserLogin getUserLogin() {
        return ls.getUserLoginId();
    }

    public GeneralOrganizationProfile getGOP() {
        return ls.getGeneralOrganizationProfileId();
    }

    public GeneralUserProfile getGUP() {
        return ls.getUserLoginId().getGeneralUserProfileId();
    }

}
