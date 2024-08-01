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
@Table(name = "student_marks")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentMarks.findAll", query = "SELECT s FROM StudentMarks s")
    , @NamedQuery(name = "StudentMarks.findById", query = "SELECT s FROM StudentMarks s WHERE s.id = :id")
    , @NamedQuery(name = "StudentMarks.findByMarks", query = "SELECT s FROM StudentMarks s WHERE s.marks = :marks")
    , @NamedQuery(name = "StudentMarks.findByIsMandatory", query = "SELECT s FROM StudentMarks s WHERE s.isMandatory = :isMandatory")})
public class StudentMarks implements Serializable {

    @Column(name = "is_removed")
    private Integer isRemoved;

    @JoinColumn(name = "entered_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralUserProfile enteredBy;

    @Column(name = "is_present")
    private Boolean isPresent;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "marks")
    private Double marks;
    @Column(name = "is_mandatory")
    private Boolean isMandatory;
    @JoinColumn(name = "grade_class_has_subjects_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassHasSubjects gradeClassHasSubjectsId;
    @JoinColumn(name = "grade_class_students_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassStudents gradeClassStudentsId;
    @JoinColumn(name = "terms_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Terms termsId;

    public StudentMarks() {
    }

    public StudentMarks(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
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

    public Terms getTermsId() {
        return termsId;
    }

    public void setTermsId(Terms termsId) {
        this.termsId = termsId;
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
        if (!(object instanceof StudentMarks)) {
            return false;
        }
        StudentMarks other = (StudentMarks) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.StudentMarks[ id=" + id + " ]";
    }

    public Boolean getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    public GeneralUserProfile getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(GeneralUserProfile enteredBy) {
        this.enteredBy = enteredBy;
    }

    public Integer getIsRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(Integer isRemoved) {
        this.isRemoved = isRemoved;
    }
    
}
