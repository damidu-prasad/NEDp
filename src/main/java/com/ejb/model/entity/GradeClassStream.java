/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "grade_class_stream")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GradeClassStream.findAll", query = "SELECT g FROM GradeClassStream g")
    , @NamedQuery(name = "GradeClassStream.findById", query = "SELECT g FROM GradeClassStream g WHERE g.id = :id")})
public class GradeClassStream implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassStreamId")
    private Collection<TeacherClassSubjectMean> teacherClassSubjectMeanCollection;

    @Column(name = "is_active")
    private Integer isActive;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassStreamId")
    private Collection<GradeClassHasSubjects> gradeClassHasSubjectsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "grade_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Grade gradeId;
    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private School schoolId;
    @JoinColumn(name = "classes_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Classes classesId;
    @JoinColumn(name = "streams_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Streams streamsId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassStreamId")
    private Collection<GradeClassStreamManager> gradeClassStreamManagerCollection;

    public GradeClassStream() {
    }

    public GradeClassStream(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Grade getGradeId() {
        return gradeId;
    }

    public void setGradeId(Grade gradeId) {
        this.gradeId = gradeId;
    }

    public School getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(School schoolId) {
        this.schoolId = schoolId;
    }

    public Classes getClassesId() {
        return classesId;
    }

    public void setClassesId(Classes classesId) {
        this.classesId = classesId;
    }

    public Streams getStreamsId() {
        return streamsId;
    }

    public void setStreamsId(Streams streamsId) {
        this.streamsId = streamsId;
    }

    @XmlTransient
    public Collection<GradeClassStreamManager> getGradeClassStreamManagerCollection() {
        return gradeClassStreamManagerCollection;
    }

    public void setGradeClassStreamManagerCollection(Collection<GradeClassStreamManager> gradeClassStreamManagerCollection) {
        this.gradeClassStreamManagerCollection = gradeClassStreamManagerCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GradeClassStream)) {
            return false;
        }
        GradeClassStream other = (GradeClassStream) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.GradeClassStream[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<GradeClassHasSubjects> getGradeClassHasSubjectsCollection() {
        return gradeClassHasSubjectsCollection;
    }

    public void setGradeClassHasSubjectsCollection(Collection<GradeClassHasSubjects> gradeClassHasSubjectsCollection) {
        this.gradeClassHasSubjectsCollection = gradeClassHasSubjectsCollection;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    @XmlTransient
    public Collection<TeacherClassSubjectMean> getTeacherClassSubjectMeanCollection() {
        return teacherClassSubjectMeanCollection;
    }

    public void setTeacherClassSubjectMeanCollection(Collection<TeacherClassSubjectMean> teacherClassSubjectMeanCollection) {
        this.teacherClassSubjectMeanCollection = teacherClassSubjectMeanCollection;
    }
    
}
