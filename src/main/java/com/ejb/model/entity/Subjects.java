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
@Table(name = "subjects")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subjects.findAll", query = "SELECT s FROM Subjects s")
    , @NamedQuery(name = "Subjects.findById", query = "SELECT s FROM Subjects s WHERE s.id = :id")
    , @NamedQuery(name = "Subjects.findByCode", query = "SELECT s FROM Subjects s WHERE s.code = :code")
    , @NamedQuery(name = "Subjects.findByName", query = "SELECT s FROM Subjects s WHERE s.name = :name")
    , @NamedQuery(name = "Subjects.findByIsActive", query = "SELECT s FROM Subjects s WHERE s.isActive = :isActive")})
public class Subjects implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectsId")
    private Collection<TeacherClassSubjectMean> teacherClassSubjectMeanCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectsId")
    private Collection<GradeClassHasSubjects> gradeClassHasSubjectsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "is_active")
    private Integer isActive;
    @JoinColumn(name = "education_level_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EducationLevel educationLevelId;

    public Subjects() {
    }

    public Subjects(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public EducationLevel getEducationLevelId() {
        return educationLevelId;
    }

    public void setEducationLevelId(EducationLevel educationLevelId) {
        this.educationLevelId = educationLevelId;
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
        if (!(object instanceof Subjects)) {
            return false;
        }
        Subjects other = (Subjects) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.Subjects[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<GradeClassHasSubjects> getGradeClassHasSubjectsCollection() {
        return gradeClassHasSubjectsCollection;
    }

    public void setGradeClassHasSubjectsCollection(Collection<GradeClassHasSubjects> gradeClassHasSubjectsCollection) {
        this.gradeClassHasSubjectsCollection = gradeClassHasSubjectsCollection;
    }

    @XmlTransient
    public Collection<TeacherClassSubjectMean> getTeacherClassSubjectMeanCollection() {
        return teacherClassSubjectMeanCollection;
    }

    public void setTeacherClassSubjectMeanCollection(Collection<TeacherClassSubjectMean> teacherClassSubjectMeanCollection) {
        this.teacherClassSubjectMeanCollection = teacherClassSubjectMeanCollection;
    }
    
}
