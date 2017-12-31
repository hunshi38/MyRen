package io.renren.modules.generator.entity;

import java.util.Date;

public class Student {
    private Integer id;
    private String sname;
    private String sex; 
    private StudentMajor studentmajor;
    
//    public Student() {
//        super();
//    }
//    public Student(Integer id, String sname, String sex,  StudentMajor major) {
//        super();
//     this.id = id;
//     this.sex = sex;
//     this.sname = sname;
//     this.studentmajor = major;
//    }
    @Override
    public String toString() {
        return "Student [id=" + id + ", sname=" + sname + ", sex=" + sex + ", studentmajor="
                + studentmajor + "]";
    }
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public StudentMajor getMajor() {
		return studentmajor;
	}
	public void setMajor(StudentMajor major) {
		this.studentmajor = major;
	}
   
}