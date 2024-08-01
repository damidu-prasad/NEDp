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
@Table(name = "educational_area_mean")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EducationalAreaMean.findAll", query = "SELECT e FROM EducationalAreaMean e")
    , @NamedQuery(name = "EducationalAreaMean.findById", query = "SELECT e FROM EducationalAreaMean e WHERE e.id = :id")
    , @NamedQuery(name = "EducationalAreaMean.findByReference", query = "SELECT e FROM EducationalAreaMean e WHERE e.reference = :reference")
    , @NamedQuery(name = "EducationalAreaMean.findByMeanValue", query = "SELECT e FROM EducationalAreaMean e WHERE e.meanValue = :meanValue")})
public class EducationalAreaMean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "reference")
    private Integer reference;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "mean_value")
    private Double meanValue;
    @JoinColumn(name = "educational_area_mean_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EducationalAreaMeanType educationalAreaMeanTypeId;
    @JoinColumn(name = "terms_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Terms termsId;
    @JoinColumn(name = "year_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Year yearId;

    public EducationalAreaMean() {
    }

    public EducationalAreaMean(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    public Double getMeanValue() {
        return meanValue;
    }

    public void setMeanValue(Double meanValue) {
        this.meanValue = meanValue;
    }

    public EducationalAreaMeanType getEducationalAreaMeanTypeId() {
        return educationalAreaMeanTypeId;
    }

    public void setEducationalAreaMeanTypeId(EducationalAreaMeanType educationalAreaMeanTypeId) {
        this.educationalAreaMeanTypeId = educationalAreaMeanTypeId;
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
        if (!(object instanceof EducationalAreaMean)) {
            return false;
        }
        EducationalAreaMean other = (EducationalAreaMean) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.EducationalAreaMean[ id=" + id + " ]";
    }
    
}
