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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "employee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e")
    , @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id")
    , @NamedQuery(name = "Employee.findByNumber", query = "SELECT e FROM Employee e WHERE e.number = :number")
    , @NamedQuery(name = "Employee.findByOldRegNo", query = "SELECT e FROM Employee e WHERE e.oldRegNo = :oldRegNo")
    , @NamedQuery(name = "Employee.findByRegDate", query = "SELECT e FROM Employee e WHERE e.regDate = :regDate")
    , @NamedQuery(name = "Employee.findByVerifiedDate", query = "SELECT e FROM Employee e WHERE e.verifiedDate = :verifiedDate")
    , @NamedQuery(name = "Employee.findByEpfNo", query = "SELECT e FROM Employee e WHERE e.epfNo = :epfNo")
    , @NamedQuery(name = "Employee.findByDayOff", query = "SELECT e FROM Employee e WHERE e.dayOff = :dayOff")
    , @NamedQuery(name = "Employee.findByStartTime", query = "SELECT e FROM Employee e WHERE e.startTime = :startTime")
    , @NamedQuery(name = "Employee.findByEndTime", query = "SELECT e FROM Employee e WHERE e.endTime = :endTime")
    , @NamedQuery(name = "Employee.findByIgnoreStartTimePeriod", query = "SELECT e FROM Employee e WHERE e.ignoreStartTimePeriod = :ignoreStartTimePeriod")
    , @NamedQuery(name = "Employee.findByAnnualLeaves", query = "SELECT e FROM Employee e WHERE e.annualLeaves = :annualLeaves")
    , @NamedQuery(name = "Employee.findByIsValidLoan", query = "SELECT e FROM Employee e WHERE e.isValidLoan = :isValidLoan")
    , @NamedQuery(name = "Employee.findByPettyCashLevel", query = "SELECT e FROM Employee e WHERE e.pettyCashLevel = :pettyCashLevel")
    , @NamedQuery(name = "Employee.findByIsActive", query = "SELECT e FROM Employee e WHERE e.isActive = :isActive")
    , @NamedQuery(name = "Employee.findByBasicSalary", query = "SELECT e FROM Employee e WHERE e.basicSalary = :basicSalary")
    , @NamedQuery(name = "Employee.findByInsuranceId", query = "SELECT e FROM Employee e WHERE e.insuranceId = :insuranceId")
    , @NamedQuery(name = "Employee.findByResignDate", query = "SELECT e FROM Employee e WHERE e.resignDate = :resignDate")
    , @NamedQuery(name = "Employee.findByFpId", query = "SELECT e FROM Employee e WHERE e.fpId = :fpId")})
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "number")
    private String number;
    @Size(max = 45)
    @Column(name = "old_reg_no")
    private String oldRegNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reg_date")
    @Temporal(TemporalType.DATE)
    private Date regDate;
    @Column(name = "verified_date")
    @Temporal(TemporalType.DATE)
    private Date verifiedDate;
    @Size(max = 255)
    @Column(name = "epf_no")
    private String epfNo;
    @Size(max = 255)
    @Column(name = "day_off")
    private String dayOff;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIME)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIME)
    private Date endTime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ignore_start_time_period")
    private Double ignoreStartTimePeriod;
    @Column(name = "annual_leaves")
    private Integer annualLeaves;
    @Column(name = "is_valid_loan")
    private Integer isValidLoan;
    @Column(name = "petty_cash_level")
    private Double pettyCashLevel;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    @Column(name = "is_active")
    private Integer isActive;
    @Column(name = "basic_salary")
    private Double basicSalary;
    @Size(max = 45)
    @Column(name = "insurance_id")
    private String insuranceId;
    @Column(name = "resign_date")
    @Temporal(TemporalType.DATE)
    private Date resignDate;
    @Column(name = "fp_id")
    private Integer fpId;
    @JoinColumn(name = "designation_id", referencedColumnName = "id")
    @ManyToOne
    private Designation designationId;
    @JoinColumn(name = "designation_grade_id", referencedColumnName = "id")
    @ManyToOne
    private DesignationGrade designationGradeId;
    @JoinColumn(name = "employee_category_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EmployeeCategory employeeCategoryId;
    @JoinColumn(name = "employee_permanent_ststus_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EmployeePermanentStstus employeePermanentStstusId;
    @JoinColumn(name = "general_organization_profile_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralOrganizationProfile generalOrganizationProfileId;
    @JoinColumn(name = "employee_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EmployeeType employeeTypeId;
    @JoinColumn(name = "general_user_profile_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralUserProfile generalUserProfileId;

    public Employee() {
    }

    public Employee(Integer id) {
        this.id = id;
    }

    public Employee(Integer id, Date regDate) {
        this.id = id;
        this.regDate = regDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOldRegNo() {
        return oldRegNo;
    }

    public void setOldRegNo(String oldRegNo) {
        this.oldRegNo = oldRegNo;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getEpfNo() {
        return epfNo;
    }

    public void setEpfNo(String epfNo) {
        this.epfNo = epfNo;
    }

    public String getDayOff() {
        return dayOff;
    }

    public void setDayOff(String dayOff) {
        this.dayOff = dayOff;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getIgnoreStartTimePeriod() {
        return ignoreStartTimePeriod;
    }

    public void setIgnoreStartTimePeriod(Double ignoreStartTimePeriod) {
        this.ignoreStartTimePeriod = ignoreStartTimePeriod;
    }

    public Integer getAnnualLeaves() {
        return annualLeaves;
    }

    public void setAnnualLeaves(Integer annualLeaves) {
        this.annualLeaves = annualLeaves;
    }

    public Integer getIsValidLoan() {
        return isValidLoan;
    }

    public void setIsValidLoan(Integer isValidLoan) {
        this.isValidLoan = isValidLoan;
    }

    public Double getPettyCashLevel() {
        return pettyCashLevel;
    }

    public void setPettyCashLevel(Double pettyCashLevel) {
        this.pettyCashLevel = pettyCashLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Date getResignDate() {
        return resignDate;
    }

    public void setResignDate(Date resignDate) {
        this.resignDate = resignDate;
    }

    public Integer getFpId() {
        return fpId;
    }

    public void setFpId(Integer fpId) {
        this.fpId = fpId;
    }

    public Designation getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Designation designationId) {
        this.designationId = designationId;
    }

    public DesignationGrade getDesignationGradeId() {
        return designationGradeId;
    }

    public void setDesignationGradeId(DesignationGrade designationGradeId) {
        this.designationGradeId = designationGradeId;
    }

    public EmployeeCategory getEmployeeCategoryId() {
        return employeeCategoryId;
    }

    public void setEmployeeCategoryId(EmployeeCategory employeeCategoryId) {
        this.employeeCategoryId = employeeCategoryId;
    }

    public EmployeePermanentStstus getEmployeePermanentStstusId() {
        return employeePermanentStstusId;
    }

    public void setEmployeePermanentStstusId(EmployeePermanentStstus employeePermanentStstusId) {
        this.employeePermanentStstusId = employeePermanentStstusId;
    }

    public GeneralOrganizationProfile getGeneralOrganizationProfileId() {
        return generalOrganizationProfileId;
    }

    public void setGeneralOrganizationProfileId(GeneralOrganizationProfile generalOrganizationProfileId) {
        this.generalOrganizationProfileId = generalOrganizationProfileId;
    }

    public EmployeeType getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(EmployeeType employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public GeneralUserProfile getGeneralUserProfileId() {
        return generalUserProfileId;
    }

    public void setGeneralUserProfileId(GeneralUserProfile generalUserProfileId) {
        this.generalUserProfileId = generalUserProfileId;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.Employee[ id=" + id + " ]";
    }
    
}
