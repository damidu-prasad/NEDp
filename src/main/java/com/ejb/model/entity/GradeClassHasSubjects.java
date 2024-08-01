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
@Table(name = "grade_class_has_subjects")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GradeClassHasSubjects.findAll", query = "SELECT g FROM GradeClassHasSubjects g")
    , @NamedQuery(name = "GradeClassHasSubjects.findById", query = "SELECT g FROM GradeClassHasSubjects g WHERE g.id = :id")
    , @NamedQuery(name = "GradeClassHasSubjects.findByIsActive", query = "SELECT g FROM GradeClassHasSubjects g WHERE g.isActive = :isActive")})
public class GradeClassHasSubjects implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassHasSubjectsId")
    private Collection<GradeClassStudentsHasSubjects> gradeClassStudentsHasSubjectsCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassHasSubjectsId")
    private Collection<GradeClassSubjectTeacher> gradeClassSubjectTeacherCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassHasSubjectsId")
    private Collection<StudentMarks> studentMarksCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "is_active")
    private Integer isActive;
    @JoinColumn(name = "grade_class_stream_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassStream gradeClassStreamId;
    @JoinColumn(name = "subjects_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subjects subjectsId;

    public GradeClassHasSubjects() {
    }

    public GradeClassHasSubjects(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public GradeClassStream getGradeClassStreamId() {
        return gradeClassStreamId;
    }

    public void setGradeClassStreamId(GradeClassStream gradeClassStreamId) {
        this.gradeClassStreamId = gradeClassStreamId;
    }

    public Subjects getSubjectsId() {
        return subjectsId;
    }

    public void setSubjectsId(Subjects subjectsId) {
        this.subjectsId = subjectsId;
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
        if (!(object instanceof GradeClassHasSubjects)) {
            return false;
        }
        GradeClassHasSubjects other = (GradeClassHasSubjects) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.GradeClassHasSubjects[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<StudentMarks> getStudentMarksCollection() {
        return studentMarksCollection;
    }

    public void setStudentMarksCollection(Collection<StudentMarks> studentMarksCollection) {
        this.studentMarksCollection = studentMarksCollection;
    }

    @XmlTransient
    public Collection<GradeClassSubjectTeacher> getGradeClassSubjectTeacherCollection() {
        return gradeClassSubjectTeacherCollection;
    }

    public void setGradeClassSubjectTeacherCollection(Collection<GradeClassSubjectTeacher> gradeClassSubjectTeacherCollection) {
        this.gradeClassSubjectTeacherCollection = gradeClassSubjectTeacherCollection;
    }

    @XmlTransient
    public Collection<GradeClassStudentsHasSubjects> getGradeClassStudentsHasSubjectsCollection() {
        return gradeClassStudentsHasSubjectsCollection;
    }

    public void setGradeClassStudentsHasSubjectsCollection(Collection<GradeClassStudentsHasSubjects> gradeClassStudentsHasSubjectsCollection) {
        this.gradeClassStudentsHasSubjectsCollection = gradeClassStudentsHasSubjectsCollection;
    }
    
}
