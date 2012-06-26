/**
 * 
 */
package uoc.edu.services;

import java.util.List;

import uoc.edu.model.bussines.PelpBussines;
import uoc.edu.model.vo.ActivityData;
import uoc.edu.model.vo.DeliverData;
import uoc.edu.model.vo.DeliverReport;

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

	
	
	
	
	public void setObjPelpBussines(PelpBussines objPelBussines) {
		this.objPelpBussines = objPelBussines;
	}

	public PelpBussines getObjPelpBussines() {
		return objPelpBussines;
	}

}
