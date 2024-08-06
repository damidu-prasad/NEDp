package com.ejb.model.entity;

import com.ejb.model.entity.UserLogin;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(SecurityQuestion.class)
public class SecurityQuestion_ { 

    public static volatile CollectionAttribute<SecurityQuestion, UserLogin> userLoginCollection;
    public static volatile SingularAttribute<SecurityQuestion, String> question;
    public static volatile SingularAttribute<SecurityQuestion, Integer> id;

}