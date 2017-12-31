package io.renren.modules.generator.entity;

import java.util.List;
import java.util.Set;

public class BallReportTable {
	private String title;
	private String[] name;
	private String[] company;
	private String[] date;
	private String[] leader;
	private String[] contact;
	private String[] phoneNum;
	private String[] coach1;
	private String[] coach2;
	private String[] coach3;
	private List<AthleteEntity> athletes;
	private List<AthleteEntity> athletesOfWoman;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String[] getName() {
		return name;
	}
	public void setName(String[] name) {
		this.name = name;
	}
	public String[] getCompany() {
		return company;
	}
	public void setCompany(String[] company) {
		this.company = company;
	}
	public String[] getDate() {
		return date;
	}
	public void setDate(String[] date) {
		this.date = date;
	}
	public String[] getLeader() {
		return leader;
	}
	public void setLeader(String[] leader) {
		this.leader = leader;
	}
	public String[] getContact() {
		return contact;
	}
	public void setContact(String[] contact) {
		this.contact = contact;
	}
	public String[] getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String[] phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String[] getCoach1() {
		return coach1;
	}
	public void setCoach1(String[] coach1) {
		this.coach1 = coach1;
	}
	public String[] getCoach2() {
		return coach2;
	}
	public void setCoach2(String[] coach2) {
		this.coach2 = coach2;
	}
	public String[] getCoach3() {
		return coach3;
	}
	public void setCoach3(String[] coach3) {
		this.coach3 = coach3;
	}
	public List<AthleteEntity> getAthletes() {
		return athletes;
	}
	public void setAthletes(List<AthleteEntity> athletes) {
		this.athletes = athletes;
	}
	public List<AthleteEntity> getAthletesOfWoman() {
		return athletesOfWoman;
	}
	public void setAthletesOfWoman(List<AthleteEntity> athletesOfWoman) {
		this.athletesOfWoman = athletesOfWoman;
	}
	
	
	
	

}
