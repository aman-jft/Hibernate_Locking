package com.jft.goel.db;

import javax.persistence.*;

/**
 * Created by aman on 8/9/14.
 */
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String empName;
    String department;

    public Employee() {
    }

    public Employee(String empName, String department) {
        this.empName = empName;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", empName='" + empName + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
