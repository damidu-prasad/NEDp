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
@Table(name = "terms")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Terms.findAll", query = "SELECT t FROM Terms t")
    , @NamedQuery(name = "Terms.findById", query = "SELECT t FROM Terms t WHERE t.id = :id")
    , @NamedQuery(name = "Terms.findByName", query = "SELECT t FROM Terms t WHERE t.name = :name")})
public class Terms implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "termsId")
    private Collection<EducationalAreaMean> educationalAreaMeanCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "termsId")
    private Collection<StudentMean> studentMeanCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "termsId")
    private Collection<TeacherClassSubjectMean> teacherClassSubjectMeanCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "termsId")
    private Collection<DevTarget> devTargetCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "termsId")
    private Collection<StudentMarks> studentMarksCollection;

    public Terms() {
    }

    public Terms(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof Terms)) {
            return false;
        }
        Terms other = (Terms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.Terms[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<DevTarget> getDevTargetCollection() {
        return devTargetCollection;
    }

    public void setDevTargetCollection(Collection<DevTarget> devTargetCollection) {
        this.devTargetCollection = devTargetCollection;
    }

    @XmlTransient
    public Collection<EducationalAreaMean> getEducationalAreaMeanCollection() {
        return educationalAreaMeanCollection;
    }

    public void setEducationalAreaMeanCollection(Collection<EducationalAreaMean> educationalAreaMeanCollection) {
        this.educationalAreaMeanCollection = educationalAreaMeanCollection;
    }

    @XmlTransient
    public Collection<StudentMean> getStudentMeanCollection() {
        return studentMeanCollection;
    }

    public void setStudentMeanCollection(Collection<StudentMean> studentMeanCollection) {
        this.studentMeanCollection = studentMeanCollection;
    }

    @XmlTransient
    public Collection<TeacherClassSubjectMean> getTeacherClassSubjectMeanCollection() {
        return teacherClassSubjectMeanCollection;
    }

    public void setTeacherClassSubjectMeanCollection(Collection<TeacherClassSubjectMean> teacherClassSubjectMeanCollection) {
        this.teacherClassSubjectMeanCollection = teacherClassSubjectMeanCollection;
    }
    
}
