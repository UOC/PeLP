package uoc.edu.model.bussines;

import java.util.List;

import uoc.edu.model.vo.ActivityData;
import uoc.edu.model.vo.DeliverData;
import uoc.edu.model.vo.DeliverReport;

/**
 * @author jsanchezramos
 */
public class PelpBussinesImpl implements PelpBussines {
	
	/* (non-Javadoc)
	 * @see uoc.edu.model.bussines.PelpBussines#setDeliver(uoc.edu.model.vo.DeliverData, uoc.edu.model.vo.ActivityData)
	 * Analitza un lliurament i retorna toat la informaci— possible.
	 */
	public DeliverReport setDeliver(DeliverData objDeliver, ActivityData objActivity){
		/*Aqui feu la logica*/
		DeliverReport objDelivery = new DeliverReport();
		return objDelivery;
	}

	/* (non-Javadoc)
	 * @see uoc.edu.model.bussines.PelpBussines#getDeliverInfo(java.lang.String, java.lang.Boolean, uoc.edu.model.vo.ActivityData)
	 * Retorna informaci— disponible sobre un o varis lliuraments.
	 */
	public List<DeliverReport> getDeliverInfo(String campusSession,
			Boolean incBinari, ActivityData objActivityData) {
		return null;
	}

	/* (non-Javadoc)
	 * @see uoc.edu.model.bussines.PelpBussines#getDeliverInfoById(java.lang.String, java.lang.Boolean, int)
	 * Retorna informaci— disponible sobre un o varis lliuraments by Id.
	 */
	public List<DeliverReport> getDeliverInfoById(String campusSession,
			Boolean incBinari, int deliverId) {
		return null;
	}
}
