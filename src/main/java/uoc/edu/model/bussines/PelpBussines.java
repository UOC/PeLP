package uoc.edu.model.bussines;

import java.util.List;

import uoc.edu.model.vo.ActivityData;
import uoc.edu.model.vo.DeliverData;
import uoc.edu.model.vo.DeliverReport;

/**
 * @author jsanchezramos
 */
public interface PelpBussines {
	public DeliverReport setDeliver(DeliverData objDeliver, ActivityData objActivity);
	public List<DeliverReport> getDeliverInfo (String campusSession,Boolean incBinari, ActivityData objActivityData);
	public List<DeliverReport> getDeliverInfoById (String campusSession,Boolean incBinari, int deliverId);
}

