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
@Table(name = "dev_target")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DevTarget.findAll", query = "SELECT d FROM DevTarget d")
    , @NamedQuery(name = "DevTarget.findById", query = "SELECT d FROM DevTarget d WHERE d.id = :id")
    , @NamedQuery(name = "DevTarget.findByTarget", query = "SELECT d FROM DevTarget d WHERE d.target = :target")})
public class DevTarget implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "target")
    private Double target;
    @JoinColumn(name = "dev_target_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DevTargetType devTargetTypeId;
    @JoinColumn(name = "terms_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Terms termsId;
    @JoinColumn(name = "year_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Year yearId;

    public DevTarget() {
    }

    public DevTarget(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public DevTargetType getDevTargetTypeId() {
        return devTargetTypeId;
    }

    public void setDevTargetTypeId(DevTargetType devTargetTypeId) {
        this.devTargetTypeId = devTargetTypeId;
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
        if (!(object instanceof DevTarget)) {
            return false;
        }
        DevTarget other = (DevTarget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.DevTarget[ id=" + id + " ]";
    }
    
}
