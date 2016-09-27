/**
 * 
 */
package com.training.guice.manager;

import java.util.List;

/**
 * @author Mina
 *
 */
public interface UserManager {

	public List<Long> getRanking(final List<Long> userIds);
}
