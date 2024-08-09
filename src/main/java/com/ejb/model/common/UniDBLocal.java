/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.common;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Sameera
 */
@Local
public interface UniDBLocal {

    void create(Object obj);

    void update(Object obj);

    List findAll(Class c);

    Object find(int id, Class c);

    void remove(int id, Class c);

    List searchByQuery(String query);

    List searchByQuerySingle(String query);
    
    List searchByQueryLimit(String query,int limit);

    public List findAll(String _TeacherType);
}
