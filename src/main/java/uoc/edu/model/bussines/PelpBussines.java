package uoc.edu.model.bussines;

import uoc.edu.model.vo.ActivityData;
import uoc.edu.model.vo.DeliverData;
import uoc.edu.model.vo.DeliverReport;

/**
 * @author jsanchezramos
 */
public interface PelpBussines {
	public DeliverReport setDeliver(DeliverData objDeliver, ActivityData objActivity);	
}

