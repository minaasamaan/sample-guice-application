/**
 * 
 */
package com.training.guice.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for Jpql related stuff
 * 
 * @author Mina
 *
 */
public final class JpqlHelper {

	/**
	 * @param userIds
	 * @param queryBuilder
	 * @param paramName
	 * @return
	 */
	public static final Map<String, List<Long>> handleInClauseLimitation(List<Long> userIds, StringBuilder queryBuilder,
			String paramName) {
		final int PARAMETER_LIMIT = 999;

		int listSize = userIds.size();
		Map<String, List<Long>> idParams = new HashMap<String, List<Long>>();

		for (int i = 0; i < listSize; i += PARAMETER_LIMIT) {
			List<Long> subList;
			if (listSize > i + PARAMETER_LIMIT) {
				subList = userIds.subList(i, (i + PARAMETER_LIMIT));
			} else {
				subList = userIds.subList(i, listSize);
			}
			if (idParams.size() != 0) {
				queryBuilder.append(" OR ").append(paramName).append(" IN (:").append(paramName).append(i).append(") ");
			} else {
				queryBuilder.append(paramName).append(" IN (:").append(paramName).append(i).append(") ");
			}
			idParams.put(paramName + i, subList);
		}
		return idParams;
	}
}
