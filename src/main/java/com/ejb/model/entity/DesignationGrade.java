/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.entity;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "designation_grade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesignationGrade.findAll", query = "SELECT d FROM DesignationGrade d")
    , @NamedQuery(name = "DesignationGrade.findById", query = "SELECT d FROM DesignationGrade d WHERE d.id = :id")
    , @NamedQuery(name = "DesignationGrade.findByName", query = "SELECT d FROM DesignationGrade d WHERE d.name = :name")})
public class DesignationGrade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "designation_level_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DesignationLevel designationLevelId;
    @OneToMany(mappedBy = "designationGradeId")
    private Collection<Employee> employeeCollection;
    @OneToMany(mappedBy = "designationGradeId")
    private Collection<Designation> designationCollection;

    public DesignationGrade() {
    }

    public DesignationGrade(Integer id) {
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

    public DesignationLevel getDesignationLevelId() {
        return designationLevelId;
    }

    public void setDesignationLevelId(DesignationLevel designationLevelId) {
        this.designationLevelId = designationLevelId;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }

    @XmlTransient
    public Collection<Designation> getDesignationCollection() {
        return designationCollection;
    }

    public void setDesignationCollection(Collection<Designation> designationCollection) {
        this.designationCollection = designationCollection;
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
        if (!(object instanceof DesignationGrade)) {
            return false;
        }
        DesignationGrade other = (DesignationGrade) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.DesignationGrade[ id=" + id + " ]";
    }
    
}
