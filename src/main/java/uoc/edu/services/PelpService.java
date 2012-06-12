/**
 * 
 */
package uoc.edu.services;

import uoc.edu.model.vo.ActivityData;
import uoc.edu.model.vo.DeliverData;
import uoc.edu.model.vo.DeliverReport;

/**
 * @author jsanchezramos
 */
public interface PelpService {
	public DeliverReport setDeliver(DeliverData objDeliver, ActivityData objActivity);	
}
