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
@Table(name = "teacher_class_subject_mean")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeacherClassSubjectMean.findAll", query = "SELECT t FROM TeacherClassSubjectMean t")
    , @NamedQuery(name = "TeacherClassSubjectMean.findById", query = "SELECT t FROM TeacherClassSubjectMean t WHERE t.id = :id")
    , @NamedQuery(name = "TeacherClassSubjectMean.findByMeanValue", query = "SELECT t FROM TeacherClassSubjectMean t WHERE t.meanValue = :meanValue")})
public class TeacherClassSubjectMean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "mean_value")
    private Double meanValue;
    @JoinColumn(name = "grade_class_stream_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassStream gradeClassStreamId;
    @JoinColumn(name = "subjects_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subjects subjectsId;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Teacher teacherId;
    @JoinColumn(name = "terms_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Terms termsId;
    @JoinColumn(name = "year_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Year yearId;

    public TeacherClassSubjectMean() {
    }

    public TeacherClassSubjectMean(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMeanValue() {
        return meanValue;
    }

    public void setMeanValue(Double meanValue) {
        this.meanValue = meanValue;
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

    public Teacher getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Teacher teacherId) {
        this.teacherId = teacherId;
    }

    public Terms getTermsId() {
        return termsId;
    }

    public void setTermsId(Terms termsId) {
        this.termsId = termsId;
    }

    public Year getYearId() {
        return yearId;
    }

    public void setYearId(Year yearId) {
        this.yearId = yearId;
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
        if (!(object instanceof TeacherClassSubjectMean)) {
            return false;
        }
        TeacherClassSubjectMean other = (TeacherClassSubjectMean) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.TeacherClassSubjectMean[ id=" + id + " ]";
    }
    
}
