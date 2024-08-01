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
@Table(name = "table_manager")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TableManager.findAll", query = "SELECT t FROM TableManager t")
    , @NamedQuery(name = "TableManager.findById", query = "SELECT t FROM TableManager t WHERE t.id = :id")
    , @NamedQuery(name = "TableManager.findByName", query = "SELECT t FROM TableManager t WHERE t.name = :name")})
public class TableManager implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tableManagerId")
    private Collection<DataChangedLogManager> dataChangedLogManagerCollection;
    @JoinColumn(name = "package_manager_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PackageManager packageManagerId;

    public TableManager() {
    }

    public TableManager(Integer id) {
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
    public Collection<DataChangedLogManager> getDataChangedLogManagerCollection() {
        return dataChangedLogManagerCollection;
    }

    public void setDataChangedLogManagerCollection(Collection<DataChangedLogManager> dataChangedLogManagerCollection) {
        this.dataChangedLogManagerCollection = dataChangedLogManagerCollection;
    }

    public PackageManager getPackageManagerId() {
        return packageManagerId;
    }

    public void setPackageManagerId(PackageManager packageManagerId) {
        this.packageManagerId = packageManagerId;
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
        if (!(object instanceof TableManager)) {
            return false;
        }
        TableManager other = (TableManager) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.TableManager[ id=" + id + " ]";
    }
    
}
