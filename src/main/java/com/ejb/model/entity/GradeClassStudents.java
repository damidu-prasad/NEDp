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
@Table(name = "grade_class_students")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GradeClassStudents.findAll", query = "SELECT g FROM GradeClassStudents g")
    , @NamedQuery(name = "GradeClassStudents.findById", query = "SELECT g FROM GradeClassStudents g WHERE g.id = :id")})
public class GradeClassStudents implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassStudentsId")
    private Collection<StudentMean> studentMeanCollection;

    @Column(name = "is_removed")
    private Integer isRemoved;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassStudentsId")
    private Collection<GradeClassStudentsHasSubjects> gradeClassStudentsHasSubjectsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "grade_class_stream_manager_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassStreamManager gradeClassStreamManagerId;
    @JoinColumn(name = "students_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Students studentsId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassStudentsId")
    private Collection<StudentMarks> studentMarksCollection;

    public GradeClassStudents() {
    }

    public GradeClassStudents(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GradeClassStreamManager getGradeClassStreamManagerId() {
        return gradeClassStreamManagerId;
    }

    public void setGradeClassStreamManagerId(GradeClassStreamManager gradeClassStreamManagerId) {
        this.gradeClassStreamManagerId = gradeClassStreamManagerId;
    }

    public Students getStudentsId() {
        return studentsId;
    }

    public void setStudentsId(Students studentsId) {
        this.studentsId = studentsId;
    }

    @XmlTransient
    public Collection<StudentMarks> getStudentMarksCollection() {
        return studentMarksCollection;
    }

    public void setStudentMarksCollection(Collection<StudentMarks> studentMarksCollection) {
        this.studentMarksCollection = studentMarksCollection;
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
        if (!(object instanceof GradeClassStudents)) {
            return false;
        }
        GradeClassStudents other = (GradeClassStudents) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.GradeClassStudents[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<GradeClassStudentsHasSubjects> getGradeClassStudentsHasSubjectsCollection() {
        return gradeClassStudentsHasSubjectsCollection;
    }

    public void setGradeClassStudentsHasSubjectsCollection(Collection<GradeClassStudentsHasSubjects> gradeClassStudentsHasSubjectsCollection) {
        this.gradeClassStudentsHasSubjectsCollection = gradeClassStudentsHasSubjectsCollection;
    }

    public Integer getIsRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(Integer isRemoved) {
        this.isRemoved = isRemoved;
    }

    @XmlTransient
    public Collection<StudentMean> getStudentMeanCollection() {
        return studentMeanCollection;
    }

    public void setStudentMeanCollection(Collection<StudentMean> studentMeanCollection) {
        this.studentMeanCollection = studentMeanCollection;
    }
    
}
