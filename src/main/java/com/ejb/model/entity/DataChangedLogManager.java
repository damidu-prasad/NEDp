/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "data_changed_log_manager")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataChangedLogManager.findAll", query = "SELECT d FROM DataChangedLogManager d")
    , @NamedQuery(name = "DataChangedLogManager.findById", query = "SELECT d FROM DataChangedLogManager d WHERE d.id = :id")
    , @NamedQuery(name = "DataChangedLogManager.findByDate", query = "SELECT d FROM DataChangedLogManager d WHERE d.date = :date")
    , @NamedQuery(name = "DataChangedLogManager.findByAttributeName", query = "SELECT d FROM DataChangedLogManager d WHERE d.attributeName = :attributeName")
    , @NamedQuery(name = "DataChangedLogManager.findByOldData", query = "SELECT d FROM DataChangedLogManager d WHERE d.oldData = :oldData")
    , @NamedQuery(name = "DataChangedLogManager.findByNewData", query = "SELECT d FROM DataChangedLogManager d WHERE d.newData = :newData")
    , @NamedQuery(name = "DataChangedLogManager.findByReference", query = "SELECT d FROM DataChangedLogManager d WHERE d.reference = :reference")})
public class DataChangedLogManager implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 255)
    @Column(name = "attribute_name")
    private String attributeName;
    @Size(max = 255)
    @Column(name = "old_data")
    private String oldData;
    @Size(max = 255)
    @Column(name = "new_data")
    private String newData;
    @Column(name = "reference")
    private Integer reference;
    @Lob
    @Size(max = 65535)
    @Column(name = "comment")
    private String comment;
    @JoinColumn(name = "table_manager_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TableManager tableManagerId;
    @JoinColumn(name = "user_login_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserLogin userLoginId;

    public DataChangedLogManager() {
    }

    public DataChangedLogManager(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TableManager getTableManagerId() {
        return tableManagerId;
    }

    public void setTableManagerId(TableManager tableManagerId) {
        this.tableManagerId = tableManagerId;
    }

    public UserLogin getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(UserLogin userLoginId) {
        this.userLoginId = userLoginId;
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
        if (!(object instanceof DataChangedLogManager)) {
            return false;
        }
        DataChangedLogManager other = (DataChangedLogManager) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.DataChangedLogManager[ id=" + id + " ]";
    }
    
}
