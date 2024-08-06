package com.ejb.model.entity;

import com.ejb.model.entity.Buildings;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeDescription;
import com.ejb.model.entity.Periods;
import com.ejb.model.entity.SchoolCategory;
import com.ejb.model.entity.SchoolType;
import com.ejb.model.entity.Students;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TypeBasedOnGrade;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(School.class)
public class School_ { 

    public static volatile SingularAttribute<School, TypeBasedOnGrade> typeBasedOnGradeId;
    public static volatile SingularAttribute<School, EducationDivision> educationDivisionId;
    public static volatile SingularAttribute<School, GeneralOrganizationProfile> generalOrganizationProfileId;
    public static volatile CollectionAttribute<School, Students> studentsCollection;
    public static volatile SingularAttribute<School, SchoolCategory> schoolCategoryId;
    public static volatile CollectionAttribute<School, Buildings> buildingsCollection;
    public static volatile SingularAttribute<School, Integer> studentsCount;
    public static volatile SingularAttribute<School, SchoolType> schoolTypeId;
    public static volatile CollectionAttribute<School, Periods> periodsCollection;
    public static volatile SingularAttribute<School, GradeDescription> gradeDescriptionId;
    public static volatile SingularAttribute<School, String> schoolId;
    public static volatile CollectionAttribute<School, GradeClassStream> gradeClassStreamCollection;
    public static volatile SingularAttribute<School, Integer> id;
    public static volatile CollectionAttribute<School, Teacher> teacherCollection;

}