/**
 * 
 */
package uoc.edu.services;

import uoc.edu.model.bussines.PelpBussines;
import uoc.edu.model.vo.ActivityData;
import uoc.edu.model.vo.DeliverData;
import uoc.edu.model.vo.DeliverReport;

/**
 * @author jsanchezramos
 */
public class PelpServiceImpl implements PelpService {
	
	private PelpBussines objPelpBussines;
	
	public DeliverReport setDeliver(DeliverData objDeliver, ActivityData objActivity){
		return objPelpBussines.setDeliver(objDeliver, objActivity);
	}

	public void setObjPelpBussines(PelpBussines objPelBussines) {
		this.objPelpBussines = objPelBussines;
	}

	public PelpBussines getObjPelpBussines() {
		return objPelpBussines;
	}
}
