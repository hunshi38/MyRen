package io.renren.modules.generator.entity;

import java.util.List;

public class StudentMajor {
    private Integer id;
   private String  name;
  //  private List<Student> students;
//    public StudentMajor() {
//        super();
//    }
//    public StudentMajor(Integer id,String name, List<Student> students) {
//        super();
//        this.id = id;
//       
//        this.name = name;
//        this.students = students;
//    }
    @Override
    public String toString() {
        return "StudentMajor [id=" + id +", name=" + name + ", students=" + "]";
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
//	public List<Student> getStudents() {
//		return students;
//	}
//	public void setStudents(List<Student> students) {
//		this.students = students;
//	}
  

}
