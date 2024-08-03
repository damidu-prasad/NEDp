package com.ejb.model.entity;

import com.ejb.model.entity.GradeClassStream;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(Streams.class)
public class Streams_ { 

    public static volatile SingularAttribute<Streams, String> name;
    public static volatile CollectionAttribute<Streams, GradeClassStream> gradeClassStreamCollection;
    public static volatile SingularAttribute<Streams, Integer> id;

}