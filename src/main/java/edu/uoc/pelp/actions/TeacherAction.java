/*
	Copyright 2011-2012 Fundació per a la Universitat Oberta de Catalunya

	This file is part of PeLP (Programming eLearning Plaform).

    PeLP is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    PeLP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uoc.pelp.actions;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;

import com.opensymphony.xwork2.ActionSupport;

import edu.uoc.pelp.bussines.UOC.UOCPelpBussines;
import edu.uoc.pelp.bussines.UOC.vo.UOCClassroom;
import edu.uoc.pelp.bussines.UOC.vo.UOCSubject;
import edu.uoc.pelp.bussines.vo.Activity;
import edu.uoc.pelp.bussines.vo.DeliverSummary;

/**
 * @author jsanchezramos
 */

@Namespace("/")
@ResultPath(value = "/")
@Result(name = "success", location = "jsp/teacher.jsp")
public class TeacherAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private UOCPelpBussines bUOC;

	private UOCSubject[] listSubjects;
	private UOCClassroom[] listClassroms;
	private Activity[] listActivity;
	private DeliverSummary[] listDelivers;

	private String s_assign;
	private String s_aula;
	private String s_activ;

    @Override
	public String execute() throws Exception {	
		listSubjects = bUOC.getUserSubjects();
		if(s_assign!=null){
			String[] infoAssing = s_assign.split("_");
			listClassroms = bUOC.getUserClassrooms(new UOCSubject(infoAssing[0],infoAssing[2]));
		}
		if(s_aula!=null && s_aula.length()>0 && s_assign != null){
			String[] infoAssing = s_assign.split("_");
			listActivity = bUOC.getSubjectActivities(new UOCSubject(infoAssing[0],infoAssing[2]));
		}
		if(s_aula!=null && s_aula.length()>0 && s_assign != null && s_activ!= null && s_activ.length()>0){
			Activity objActivity = new Activity();
			for (int j = 0; j < listActivity.length; j++) {
				if(listActivity[j].getIndex() == Integer.parseInt(s_activ)){
					objActivity = listActivity[j];
				}
			}
			String[] infoAssing = s_assign.split("_");
			bUOC.getLastClassroomDeliverSummary(objActivity, new UOCSubject(infoAssing[0],infoAssing[2]), objActivity.getIndex());
		}
		return SUCCESS;
	}

	public UOCSubject[] getListSubjects() {
		return listSubjects;
	}

	public void setListSubjects(UOCSubject[] listSubjects) {
		this.listSubjects = listSubjects;
	}

	public String getS_assign() {
		return s_assign;
	}

	public void setS_assign(String s_assign) {
		this.s_assign = s_assign;
	}

	public UOCClassroom[] getListClassroms() {
		return listClassroms;
	}

	public void setListClassroms(UOCClassroom[] listClassroms) {
		this.listClassroms = listClassroms;
	}

	public String getS_aula() {
		return s_aula;
	}

	public void setS_aula(String s_aula) {
		this.s_aula = s_aula;
	}

	public String getS_activ() {
		return s_activ;
	}

	public void setS_activ(String s_activ) {
		this.s_activ = s_activ;
	}

	public Activity[] getListActivity() {
		return listActivity;
	}

	public void setListActivity(Activity[] listActivity) {
		this.listActivity = listActivity;
	}

	public UOCPelpBussines getbUOC() {
		return bUOC;
	}

	public void setbUOC(UOCPelpBussines bUOC) {
		this.bUOC = bUOC;
	}

	public DeliverSummary[] getListDelivers() {
		return listDelivers;
	}

	public void setListDelivers(DeliverSummary[] listDelivers) {
		this.listDelivers = listDelivers;
	}

}
