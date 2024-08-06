package com.ejb.model.entity;

import com.ejb.model.entity.EducationalAreaMeanType;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(EducationalAreaMean.class)
public class EducationalAreaMean_ { 

    public static volatile SingularAttribute<EducationalAreaMean, Integer> reference;
    public static volatile SingularAttribute<EducationalAreaMean, EducationalAreaMeanType> educationalAreaMeanTypeId;
    public static volatile SingularAttribute<EducationalAreaMean, Terms> termsId;
    public static volatile SingularAttribute<EducationalAreaMean, Double> meanValue;
    public static volatile SingularAttribute<EducationalAreaMean, Integer> id;
    public static volatile SingularAttribute<EducationalAreaMean, Year> yearId;

}