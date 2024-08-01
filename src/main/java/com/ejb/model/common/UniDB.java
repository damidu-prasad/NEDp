/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.common;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Sameera
 */
@Stateless
public class UniDB implements UniDBLocal {

//    @Resource
//    javax.transaction.UserTransaction utx;

    @PersistenceContext(unitName = "com.mycompany_contex_war_1.0-SNAPSHOTPU")
    EntityManager em;

    @Override

    public void create(Object obj) {

        try {
            em.persist(obj);
            em.flush();
        } catch (ConstraintViolationException e) {
            System.out.println("e is>>>>>>>>>>>>>>>>>>>>>>>>>" + e);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);

            if (constraintViolations.size() > 0) {
                System.out.println("Constraint Violations occurred..");
                for (ConstraintViolation<Object> contraints : constraintViolations) {
                    System.out.println(contraints.getRootBeanClass().getSimpleName()
                            + "." + contraints.getPropertyPath() + " " + contraints.getMessage());
                }
            }

        }

//        StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("sales_tax");
//// set parameters
//storedProcedure.registerStoredProcedureParameter("subtotal", Double.class, ParameterMode.IN);
//storedProcedure.registerStoredProcedureParameter("tax", Double.class, ParameterMode.OUT);
//storedProcedure.setParameter("subtotal", 1f);
//// execute SP
//storedProcedure.execute();
//// get result
//Double tax = (Double)storedProcedure.getOutputParameterValue("tax");
    }

    @Override
    public void update(Object obj) {
        em.merge(obj);
    }

    @Override
    public List findAll(Class c) {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(c));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public Object find(int id, Class c) {
        try {

            return em.find(c.newInstance().getClass(), id);
        } catch (Exception e) {
            System.out.println("error2>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void remove(int id, Class c) {
        em.remove(find(id, c));
    }

    @Override
    public List searchByQuery(String query) {
        try {
            return em.createQuery(query).getResultList();
        } catch (Exception e) {
            System.out.println("error>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List searchByQuerySingle(String query) {
        return em.createQuery(query).setMaxResults(1).getResultList();
    }

    @Override
    public List searchByQueryLimit(String query, int limit) {

        return em.createQuery(query).setMaxResults(limit).getResultList();
    }

    public void persist(Object object) {
        try {
//            em.begin();
            em.persist(object);
//            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }

}
