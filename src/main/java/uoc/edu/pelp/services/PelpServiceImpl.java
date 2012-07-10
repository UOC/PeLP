/**
 * 
 */
package uoc.edu.pelp.services;

import java.util.List;

import uoc.edu.pelp.bussines.PelpBussines;
import uoc.edu.pelp.model.vo.ActivityData;
import uoc.edu.pelp.model.vo.DeliverData;
import uoc.edu.pelp.model.vo.DeliverReport;

/**
 * @author jsanchezramos
 */
public class PelpServiceImpl implements PelpService {

	private PelpBussines objPelpBussines;

	/* (non-Javadoc)
	 * @see uoc.edu.services.PelpService#setDeliver(uoc.edu.model.vo.DeliverData, uoc.edu.model.vo.ActivityData)
	 * Analitza un lliurament i retorna toat la informaci— possible.
	 */
	public DeliverReport setDeliver(DeliverData objDeliver,
			ActivityData objActivity) {
		return objPelpBussines.setDeliver(objDeliver, objActivity);
	}

	/* (non-Javadoc)
	 * @see uoc.edu.services.PelpService#getDeliverInfo(java.lang.String, java.lang.Boolean, uoc.edu.model.vo.ActivityData)
	 * Retorna informaci— disponible sobre un o varis lliuraments.
	 */
	public List<DeliverReport> getDeliverInfo(String campusSession,
			Boolean incBinari, ActivityData objActivityData) {
		return objPelpBussines.getDeliverInfo(campusSession, incBinari, objActivityData);
	}
	
	/* (non-Javadoc)
	 * @see uoc.edu.services.PelpService#getDeliverInfoById(java.lang.String, java.lang.Boolean, int)
	 * Retorna informaci— disponible sobre un o varis lliuraments by Id.
	 */
	public List<DeliverReport> getDeliverInfoById(String campusSession,
			Boolean incBinari, int deliverId) {
		return objPelpBussines.getDeliverInfoById(campusSession, incBinari, deliverId);
	}	
	
	/* (non-Javadoc)
	 * @see uoc.edu.pelp.services.PelpService#getActivityInfo(uoc.edu.pelp.model.vo.ActivityData)
	 * Obtenir la informaci— d'una tasca
	 */
	public ActivityData[] getActivityInfo(ActivityData objActivityData,String campusSession){
		return objPelpBussines.getActivityInfo(objActivityData,campusSession);
	}
	
	/* (non-Javadoc)
	 * @see uoc.edu.pelp.services.PelpService#getActivityInfoById(int)
	 * Obtenir la informaci— d'una tasca per activityId
	 */
	public ActivityData[] getActivityInfoById(int activityId,String campusSession){
		return objPelpBussines.getActivityInfoById(activityId,campusSession);
	}
	
	/* (non-Javadoc)
	 * @see uoc.edu.pelp.services.PelpService#setActivityInfo(uoc.edu.pelp.model.vo.ActivityData)
	 * Crear una nova tasca o modificar-se una d'existent
	 */
	public ActivityData setActivityInfo(ActivityData objActivityData,String campusSession){
		return objPelpBussines.setActivityInfo(objActivityData,campusSession);
	}

	
	
	
	
	public void setObjPelpBussines(PelpBussines objPelBussines) {
		this.objPelpBussines = objPelBussines;
	}

	public PelpBussines getObjPelpBussines() {
		return objPelpBussines;
	}

}
