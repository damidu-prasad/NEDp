/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "grade_class_students_has_subjects")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GradeClassStudentsHasSubjects.findAll", query = "SELECT g FROM GradeClassStudentsHasSubjects g")
    , @NamedQuery(name = "GradeClassStudentsHasSubjects.findById", query = "SELECT g FROM GradeClassStudentsHasSubjects g WHERE g.id = :id")})
public class GradeClassStudentsHasSubjects implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "grade_class_has_subjects_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassHasSubjects gradeClassHasSubjectsId;
    @JoinColumn(name = "grade_class_students_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassStudents gradeClassStudentsId;

    public GradeClassStudentsHasSubjects() {
    }

    public GradeClassStudentsHasSubjects(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GradeClassHasSubjects getGradeClassHasSubjectsId() {
        return gradeClassHasSubjectsId;
    }

    public void setGradeClassHasSubjectsId(GradeClassHasSubjects gradeClassHasSubjectsId) {
        this.gradeClassHasSubjectsId = gradeClassHasSubjectsId;
    }

    public GradeClassStudents getGradeClassStudentsId() {
        return gradeClassStudentsId;
    }

    public void setGradeClassStudentsId(GradeClassStudents gradeClassStudentsId) {
        this.gradeClassStudentsId = gradeClassStudentsId;
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
        if (!(object instanceof GradeClassStudentsHasSubjects)) {
            return false;
        }
        GradeClassStudentsHasSubjects other = (GradeClassStudentsHasSubjects) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.GradeClassStudentsHasSubjects[ id=" + id + " ]";
    }
    
}
