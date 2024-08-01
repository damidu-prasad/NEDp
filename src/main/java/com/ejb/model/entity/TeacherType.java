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

@Entity
@Table(name = "teacher_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeacherType.findAll", query = "SELECT t FROM TeacherType t"),
    @NamedQuery(name = "TeacherType.findById", query = "SELECT t FROM TeacherType t WHERE t.id = :id"),
    @NamedQuery(name = "TeacherType.findByType", query = "SELECT t FROM TeacherType t WHERE t.type = :type")
})
public class TeacherType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Size(max = 45)
    @Column(name = "type")
    private String type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherType")
    private Collection<GradeClassStreamManager> gradeClassStreamManagerCollection;

    public TeacherType() {
    }

    public TeacherType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        if (!(object instanceof TeacherType)) {
            return false;
        }
        TeacherType other = (TeacherType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.TeacherType[ id=" + id + " ]";
    }
}
