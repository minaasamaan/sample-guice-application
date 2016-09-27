/**
 * 
 */
package com.training.guice.valueobject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.training.guice.jpa.domain.Enums.ExerciseType;

/**
 * @author Mina
 *
 */
public class UserVo implements Comparable<UserVo> {

	private Long userId;

	private BigInteger points = new BigInteger("0");

	Map<ExerciseType, Float> exerciseWeightMatrix = new HashMap<ExerciseType, Float>();

	/**
	 * 
	 */
	public UserVo(Long userId, BigInteger calculatedPoints, ExerciseType exerciseType) {
		this.userId = userId;
		updatePoints(calculatedPoints, exerciseType);
	}

	public void updatePoints(BigInteger calculatedPoints, ExerciseType exerciseType) {
		float factor;

		if (exerciseWeightMatrix.containsKey(exerciseType)) {
			factor = exerciseWeightMatrix.get(exerciseType);
			if (factor == 0) {
				return;// Nothing to do
			}
			factor -= 1;
		} else {
			factor = 10;
		}

		if (factor > 0) {
			points = points
					.add(new BigDecimal(calculatedPoints).multiply(new BigDecimal(factor / 10.0)).toBigInteger());
		}
		exerciseWeightMatrix.put(exerciseType, factor);
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!getClass().equals(obj.getClass())) {
			return false;
		}

		UserVo that = (UserVo) obj;

		return null != this.getUserId() && this.getUserId().equals(that.getUserId());
	}

	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += null == getUserId() ? 0 : getUserId().hashCode() * 31;

		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(UserVo other) {
		return points.compareTo(other.getPoints());
	}

	public BigInteger getPoints() {
		return points;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
}
