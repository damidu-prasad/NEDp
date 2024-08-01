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
@Table(name = "school")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "School.findAll", query = "SELECT s FROM School s")
    , @NamedQuery(name = "School.findById", query = "SELECT s FROM School s WHERE s.id = :id")
    , @NamedQuery(name = "School.findBySchoolId", query = "SELECT s FROM School s WHERE s.schoolId = :schoolId")
    , @NamedQuery(name = "School.findByStudentsCount", query = "SELECT s FROM School s WHERE s.studentsCount = :studentsCount")})
public class School implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schoolId")
    private Collection<Periods> periodsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schoolId")
    private Collection<Buildings> buildingsCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schoolId")
    private Collection<Students> studentsCollection;

    @JoinColumn(name = "education_division_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EducationDivision educationDivisionId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "school_id")
    private String schoolId;
    @Column(name = "students_count")
    private Integer studentsCount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schoolId")
    private Collection<GradeClassStream> gradeClassStreamCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schoolId")
    private Collection<Teacher> teacherCollection;
    @JoinColumn(name = "general_organization_profile_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralOrganizationProfile generalOrganizationProfileId;
    @JoinColumn(name = "grade_description_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeDescription gradeDescriptionId;
    @JoinColumn(name = "school_category_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SchoolCategory schoolCategoryId;
    @JoinColumn(name = "school_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SchoolType schoolTypeId;
    @JoinColumn(name = "type_based_on_grade_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TypeBasedOnGrade typeBasedOnGradeId;

    public School() {
    }

    public School(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(Integer studentsCount) {
        this.studentsCount = studentsCount;
    }

    @XmlTransient
    public Collection<GradeClassStream> getGradeClassStreamCollection() {
        return gradeClassStreamCollection;
    }

    public void setGradeClassStreamCollection(Collection<GradeClassStream> gradeClassStreamCollection) {
        this.gradeClassStreamCollection = gradeClassStreamCollection;
    }

    @XmlTransient
    public Collection<Teacher> getTeacherCollection() {
        return teacherCollection;
    }

    public void setTeacherCollection(Collection<Teacher> teacherCollection) {
        this.teacherCollection = teacherCollection;
    }

    public GeneralOrganizationProfile getGeneralOrganizationProfileId() {
        return generalOrganizationProfileId;
    }

    public void setGeneralOrganizationProfileId(GeneralOrganizationProfile generalOrganizationProfileId) {
        this.generalOrganizationProfileId = generalOrganizationProfileId;
    }

    public GradeDescription getGradeDescriptionId() {
        return gradeDescriptionId;
    }

    public void setGradeDescriptionId(GradeDescription gradeDescriptionId) {
        this.gradeDescriptionId = gradeDescriptionId;
    }

    public SchoolCategory getSchoolCategoryId() {
        return schoolCategoryId;
    }

    public void setSchoolCategoryId(SchoolCategory schoolCategoryId) {
        this.schoolCategoryId = schoolCategoryId;
    }

    public SchoolType getSchoolTypeId() {
        return schoolTypeId;
    }

    public void setSchoolTypeId(SchoolType schoolTypeId) {
        this.schoolTypeId = schoolTypeId;
    }

    public TypeBasedOnGrade getTypeBasedOnGradeId() {
        return typeBasedOnGradeId;
    }

    public void setTypeBasedOnGradeId(TypeBasedOnGrade typeBasedOnGradeId) {
        this.typeBasedOnGradeId = typeBasedOnGradeId;
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
        if (!(object instanceof School)) {
            return false;
        }
        School other = (School) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.School[ id=" + id + " ]";
    }

    public EducationDivision getEducationDivisionId() {
        return educationDivisionId;
    }

    public void setEducationDivisionId(EducationDivision educationDivisionId) {
        this.educationDivisionId = educationDivisionId;
    }

    @XmlTransient
    public Collection<Students> getStudentsCollection() {
        return studentsCollection;
    }

    public void setStudentsCollection(Collection<Students> studentsCollection) {
        this.studentsCollection = studentsCollection;
    }

    @XmlTransient
    public Collection<Periods> getPeriodsCollection() {
        return periodsCollection;
    }

    public void setPeriodsCollection(Collection<Periods> periodsCollection) {
        this.periodsCollection = periodsCollection;
    }

    @XmlTransient
    public Collection<Buildings> getBuildingsCollection() {
        return buildingsCollection;
    }

    public void setBuildingsCollection(Collection<Buildings> buildingsCollection) {
        this.buildingsCollection = buildingsCollection;
    }
    
}
