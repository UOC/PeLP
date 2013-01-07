package edu.uoc.pelp.actions;

import javax.annotation.PreDestroy;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.osid.OsidException;

import com.opensymphony.xwork2.ActionSupport;

import edu.uoc.pelp.bussines.UOC.UOCPelpBussines;
import edu.uoc.pelp.bussines.UOC.vo.UOCClassroom;
import edu.uoc.pelp.bussines.UOC.vo.UOCSubject;
import edu.uoc.pelp.bussines.vo.Activity;
import edu.uoc.pelp.bussines.vo.DeliverDetail;
import edu.uoc.pelp.bussines.vo.DeliverSummary;

/**
 * 
 * Main action class for PeLP. Merged code from StudentAction and TeacherAction.
 * 
 * @author oripolles
 * 
 */
@Namespace("/")
@ResultPath(value = "/")
@Results({
		@Result(name = "success", location = "jsp/pelp.jsp"),
		@Result(name = "programming-environment", location = "jsp/deliveries.jsp") })
public class PelpAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3165462908903079864L;
	public static final String TAB_DELIVERIES = "deliveries";
	public static final String TAB_PROGRAMMING_ENVIROMENT = "programming-environment";

	private UOCPelpBussines bUOC;

	private UOCSubject[] listSubjects;
	private UOCClassroom[] listClassroms;
	private Activity[] listActivity;
	private DeliverSummary[] listDeliverSummaries;
	private DeliverDetail[] listDeliverDetails;

	private String s_assign;
	private String s_aula;
	private String s_activ;

	private String username;
	private String password;
	private String imageURL;
	private String fullName;

	private String activeTab;
	private boolean ajaxCall;
	private String selectorToLoad;
	private String filterType;
	private String filterValue;
	private boolean teacher;

	@Override
	public String execute() throws Exception {

		if (bUOC.getUserInformation() != null) {
			listSubjects = bUOC.getUserSubjects();
			if (s_assign != null) {
				String[] infoAssing = s_assign.split("_");
				teacher = bUOC.isTeacher(new UOCSubject(infoAssing[0],
						infoAssing[2]));
				listClassroms = bUOC.getUserClassrooms(new UOCSubject(
						infoAssing[0], infoAssing[2]));
			}
			if (s_aula != null && s_aula.length() > 0 && s_assign != null) {
				String[] infoAssing = s_assign.split("_");
				listActivity = bUOC.getSubjectActivities(new UOCSubject(
						infoAssing[0], infoAssing[2]));
			}
			if (s_aula != null && s_aula.length() > 0 && s_assign != null
					&& s_activ != null && s_activ.length() > 0) {
				Activity objActivity = new Activity();
				for (int j = 0; j < listActivity.length; j++) {
					if (listActivity[j].getIndex() == Integer.parseInt(s_activ)) {
						objActivity = listActivity[j];
					}
				}
				String[] infoAssing = s_assign.split("_");
				listDeliverDetails = bUOC.getUserDeliverDetails(new UOCSubject(
						infoAssing[0], infoAssing[2]), objActivity.getIndex());
				listDeliverSummaries = bUOC.getLastClassroomDeliverSummary(
						objActivity, new UOCSubject(infoAssing[0],
								infoAssing[2]), objActivity.getIndex());
			}
			imageURL = bUOC.getUserInformation().getUserPhoto();
			fullName = bUOC.getUserInformation().getUserFullName();
		} else {
			imageURL = null;
		}

		String toReturn = SUCCESS;

		if (TAB_PROGRAMMING_ENVIROMENT.equals(activeTab)) {
			toReturn = TAB_PROGRAMMING_ENVIROMENT;
		}

		return toReturn;
	}

	@PreDestroy
	public String logout() {
		bUOC.logout();
		return "index";
	}

	public String auth() throws Exception, OsidException {
		//FIXME
		//bUOC.setCampusSession(Utils.authUserForCampus(username, password));
		return "index";
	}

	public UOCPelpBussines getbUOC() {
		return bUOC;
	}

	public void setbUOC(UOCPelpBussines bUOC) {
		this.bUOC = bUOC;
	}

	public UOCSubject[] getListSubjects() {
		return listSubjects;
	}

	public void setListSubjects(UOCSubject[] listSubjects) {
		this.listSubjects = listSubjects;
	}

	public UOCClassroom[] getListClassroms() {
		return listClassroms;
	}

	public void setListClassroms(UOCClassroom[] listClassroms) {
		this.listClassroms = listClassroms;
	}

	public Activity[] getListActivity() {
		return listActivity;
	}

	public void setListActivity(Activity[] listActivity) {
		this.listActivity = listActivity;
	}

	public DeliverSummary[] getListDeliverSummaries() {
		return listDeliverSummaries;
	}

	public void setListDeliverSummaries(DeliverSummary[] listDeliverSummaries) {
		this.listDeliverSummaries = listDeliverSummaries;
	}

	public DeliverDetail[] getListDeliverDetails() {
		return listDeliverDetails;
	}

	public void setListDeliverDetails(DeliverDetail[] listDeliverDetails) {
		this.listDeliverDetails = listDeliverDetails;
	}

	public String getS_assign() {
		return s_assign;
	}

	public void setS_assign(String s_assign) {
		this.s_assign = s_assign;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

	public boolean isAjaxCall() {
		return ajaxCall;
	}

	public void setAjaxCall(boolean ajaxCall) {
		this.ajaxCall = ajaxCall;
	}

	public String getSelectorToLoad() {
		return selectorToLoad;
	}

	public void setSelectorToLoad(String selectorToLoad) {
		this.selectorToLoad = selectorToLoad;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public boolean isTeacher() {
		return teacher;
	}

	public void setTeacher(boolean teacher) {
		this.teacher = teacher;
	}

}
