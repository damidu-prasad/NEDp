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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "teacher")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Teacher.findAll", query = "SELECT t FROM Teacher t")
    , @NamedQuery(name = "Teacher.findById", query = "SELECT t FROM Teacher t WHERE t.id = :id")
    , @NamedQuery(name = "Teacher.findByTeacherId", query = "SELECT t FROM Teacher t WHERE t.teacherId = :teacherId")
    , @NamedQuery(name = "Teacher.findByIsActive", query = "SELECT t FROM Teacher t WHERE t.isActive = :isActive")})
public class Teacher implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherId")
    private Collection<TeacherMonthlyScores> teacherMonthlyScoresCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherId")
    private Collection<TeacherClassSubjectMean> teacherClassSubjectMeanCollection;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherId")
    private Collection<GradeClassSubjectTeacher> gradeClassSubjectTeacherCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "teacher_id")
    private String teacherId;
    @Column(name = "is_active")
    private Integer isActive;
    @JoinColumn(name = "general_user_profile_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralUserProfile generalUserProfileId;
    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private School schoolId;
    @JoinColumn(name = "teacher_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TeacherType teacherTypeId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherId")
    private Collection<GradeClassStreamManager> gradeClassStreamManagerCollection;

    public Teacher() {
    }

    public TeacherType getTeacherTypeId() {
        return teacherTypeId;
    }

    public void setTeacherTypeId(TeacherType teacherTypeId) {
        this.teacherTypeId = teacherTypeId;
    }

    public Teacher(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public GeneralUserProfile getGeneralUserProfileId() {
        return generalUserProfileId;
    }

    public void setGeneralUserProfileId(GeneralUserProfile generalUserProfileId) {
        this.generalUserProfileId = generalUserProfileId;
    }

    public School getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(School schoolId) {
        this.schoolId = schoolId;
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
        if (!(object instanceof Teacher)) {
            return false;
        }
        Teacher other = (Teacher) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.Teacher[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<GradeClassSubjectTeacher> getGradeClassSubjectTeacherCollection() {
        return gradeClassSubjectTeacherCollection;
    }

    public void setGradeClassSubjectTeacherCollection(Collection<GradeClassSubjectTeacher> gradeClassSubjectTeacherCollection) {
        this.gradeClassSubjectTeacherCollection = gradeClassSubjectTeacherCollection;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    @XmlTransient
    public Collection<TeacherClassSubjectMean> getTeacherClassSubjectMeanCollection() {
        return teacherClassSubjectMeanCollection;
    }

    public void setTeacherClassSubjectMeanCollection(Collection<TeacherClassSubjectMean> teacherClassSubjectMeanCollection) {
        this.teacherClassSubjectMeanCollection = teacherClassSubjectMeanCollection;
    }

    @XmlTransient
    public Collection<TeacherMonthlyScores> getTeacherMonthlyScoresCollection() {
        return teacherMonthlyScoresCollection;
    }

    public void setTeacherMonthlyScoresCollection(Collection<TeacherMonthlyScores> teacherMonthlyScoresCollection) {
        this.teacherMonthlyScoresCollection = teacherMonthlyScoresCollection;
    }

    public Object get(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
