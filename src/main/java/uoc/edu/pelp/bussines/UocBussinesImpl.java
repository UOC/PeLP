package uoc.edu.pelp.bussines;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import edu.uoc.pelp.bussines.UOC.UOCPelpBussinesImpl;
import edu.uoc.pelp.bussines.UOC.vo.UOCSubject;
import edu.uoc.pelp.engine.activity.Activity;
import edu.uoc.pelp.engine.campus.Classroom;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.exception.PelpException;



/**
 * @author jsanchezramos
 */
public class UocBussinesImpl implements UocBussines {

	private UOCPelpBussinesImpl engine;

	/*
	 * (non-Javadoc)
	 * 
	 * @see uoc.edu.pelp.bussines.UocBussines#getSubjects()
	 */
	public UOCSubject[] getSubjects() throws Exception {
		return (UOCSubject[])engine.getUserSubjects();
	}

	public Classroom[] getClassroomSubjects(String idSubject) throws Exception {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Classroom[] arrayClass = null;
//
//		if (idSubject != null) {
//			String[] subjectArray = idSubject.split("-");
//			System.out.println("ASSIGNATURA : " + idSubject);
//
//			Semester objSemester = new Semester(subjectArray[1],
//					df.parse("21/09/2011"), df.parse("15/01/2012"));
//
//			SubjectID objSubject = new SubjectID(subjectArray[0], objSemester);
//
//			arrayClass = engine.getSubjectClassrooms(objSubject);
//		}

		return arrayClass;
	}

	public Map<String, String> getActivitiClassroom(String idClassroom)
			throws Exception, PelpException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Activity[] arrayActiviy = null;
		//String[] arrayString = null;
		
		Map<String,String> arrayString = new HashMap<String,String>();

		
//		
//		if (idClassroom != null && idClassroom.length() != 0) {
//			String[] classroomArray = idClassroom.split("-");
//			Semester objSemester = new Semester(classroomArray[1],
//					df.parse("21/09/2011"), df.parse("15/01/2012"));
//
//			SubjectID objSubject = new SubjectID(classroomArray[0], objSemester);
//
//			IActivityManager _activityManager = (IActivityManager) new LocalActivityManager();
//			// Create time objects
//			Date now = new Date();
//			Calendar c = Calendar.getInstance();
//			c.setTime(now);
//			c.add(Calendar.DATE, -1);
//			Date yesterday = c.getTime();
//			c.setTime(now);
//			c.add(Calendar.DATE, 1);
//			Date tomorrow = c.getTime();
//
//			// Add a new active activity without datesCAT for Catalan, ES for
//			// Spanish, ENG for English
//			ActivityID activityID = _activityManager.addActivity(objSubject,
//					null, null);
//			Activity activity = _activityManager.getActivity(activityID);
//			activity.setDescription("CAT", "GHOLAHOLA1");
//			activity.setLanguage("JAVA");
//			_activityManager.editActivity(activity);
//
//			// Add a future activity
//			activityID = _activityManager.addActivity(objSubject, tomorrow,
//					null);
//			activity = _activityManager.getActivity(activityID);
//			activity.setDescription("CAT", "GHOLAHOLA2");
//			activity.setLanguage("JAVA");
//			_activityManager.editActivity(activity);
//
//			// Add a past activity
//			activityID = _activityManager.addActivity(objSubject, null,
//					yesterday);
//			activity = _activityManager.getActivity(activityID);
//			activity.setDescription("CAT", "GHOLAHOLA3");
//			activity.setLanguage("JAVA");
//			
//			_activityManager.editActivity(activity);
//
//			// Add an active activity with dates
//			activityID = _activityManager.addActivity(objSubject, yesterday,
//					tomorrow);
//			activity = _activityManager.getActivity(activityID);
//			activity.setDescription("CAT", "GHOLAHOLA4");
//			activity.setLanguage("JAVA");
//			_activityManager.editActivity(activity);
//
//			engine.setActivityManager(_activityManager);
//
//			arrayActiviy = engine.getSubjectActivity(objSubject, false);
//
//			for (int i = 0; i < arrayActiviy.length; i++) {
//				arrayString.put(arrayActiviy[i].getActivity().index+"-"+((SubjectID) arrayActiviy[i].getActivity().subjectID).getCode()+"-"+((SubjectID) arrayActiviy[i].getActivity().subjectID).getSemester().getID(), arrayActiviy[i].getDescription("CAT"));
//			}
//		}
		
		return arrayString;
	}
	
	public Deliver[] getDelivers(String activity) throws PelpException, Exception{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Deliver[] listDelivers = null;
//		
//		if(activity != null && activity.length()>0){
//			String[] activityArray = activity.split("-");
//			Semester objSemester = new Semester(activityArray[2],
//					df.parse("21/09/2011"), df.parse("15/01/2012"));
//	
//			SubjectID objSubject = new SubjectID(activityArray[1], objSemester);
//			ActivityID objActivityID = new ActivityID(objSubject,Long.parseLong(activityArray[0]));
//			
//			DeliverID objDeliverId = new DeliverID(engine.getUserInfo().getUserID(),objActivityID, 1);  
//			File tmpPath=new File("/temp/pelp/");  
//			Deliver obj = new Deliver(objDeliverId, tmpPath);
//			
//			// Create dummy files
//	        DeliverFile file1=new DeliverFile(new File("/temp/pelp/hola.java"),FileType.Code);        
//
//	        DeliverFile file2=new DeliverFile(new File("file2.txt"),FileType.Report);
//
//	        DeliverFile file3=new DeliverFile(new File("file3.txt"),FileType.Report);
//
//			obj.addFile(file1);
//			obj.addFile(file2);
//			obj.addFile(file3);
//			
//			engine.createNewDeliver(obj, objActivityID);
//			
//			listDelivers = engine.getActivityDelivers(engine.getUserInfo().getUserID(), objActivityID);
			
//		}
		
		return listDelivers;
	}

	public UOCPelpBussinesImpl getEngine() {
		return engine;
	}

	public void setEngine(UOCPelpBussinesImpl engine) {
		this.engine = engine;
	}

}
