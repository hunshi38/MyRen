package io.renren.modules.generator.entity.param;

import java.util.List;

public class PublicSignParam {
	Integer competitionId;
	Integer	eventId;
	Integer groupId;
	PersonInfoParam persons[];
	public Integer getCompetitionId() {
		return competitionId;
	}
	public void setCompetitionId(Integer competitionId) {
		this.competitionId = competitionId;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public PersonInfoParam[] getPersons() {
		return persons;
	}
	public void setPersons(PersonInfoParam[] persons) {
		this.persons = persons;
	}
	
		

}
