package uoc.edu.pelp.model.bussines;

import java.util.List;

import uoc.edu.pelp.model.vo.ActivityData;
import uoc.edu.pelp.model.vo.DeliverData;
import uoc.edu.pelp.model.vo.DeliverReport;

/**
 * @author jsanchezramos
 */
public interface PelpBussines {
	public DeliverReport setDeliver(DeliverData objDeliver, ActivityData objActivity);
	public List<DeliverReport> getDeliverInfo (String campusSession,Boolean incBinari, ActivityData objActivityData);
	public List<DeliverReport> getDeliverInfoById (String campusSession,Boolean incBinari, int deliverId);
	public ActivityData[] getActivityInfo(ActivityData objActivityData,String campusSession);
	public ActivityData[] getActivityInfoById(int activityId,String campusSession);
	public ActivityData setActivityInfo(ActivityData objActivityData,String campusSession);
}

