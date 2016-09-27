/**
 * 
 */
package com.training.guice.dto.exercise;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import com.training.guice.dto.AbstractDto;
import com.training.guice.jpa.domain.Enums;
import com.training.guice.validation.engine.ValidatableObject;
import com.training.guice.validation.groups.CRUDGroup.Create;
import com.training.guice.validation.groups.CRUDGroup.Update;

/**
 * @author Mina
 *
 */
public class ExerciseDto extends AbstractDto implements ValidatableObject {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "User Id is required", groups = { Create.class, Update.class })
	private Long userId;

	@NotNull(message = "Description is required", groups = { Create.class, Update.class })
	@Pattern(regexp = "^[a-zA-Z0-9 ]{1,255}$", message = "Only alphanumeric values and spaces allowed in description, description characters is min=1, max=255", groups = {
			Create.class, Update.class })
	private String description;

	@NotNull(message = "Exercise type is required", groups = { Create.class, Update.class })
	private Enums.ExerciseType type;

	/**
	 * format: yyyy-MM-dd'T'HH:mm:ss
	 */
	@Past(message = "Start time must be in the past", groups = { Create.class, Update.class })
	// @(pattern = "")
	@NotNull(message = "Start time is required", groups = { Create.class, Update.class })
	private Date startTime;

	/**
	 * in seconds
	 */
	@NotNull(message = "Duration is required", groups = { Create.class, Update.class })
	private Integer duration;

	/**
	 * in meters
	 */
	@NotNull(message = "Distance is required", groups = { Create.class, Update.class })
	private Integer distance;

	/**
	 * in kcal
	 */
	@NotNull(message = "Calories field is required", groups = { Create.class, Update.class })
	private Integer calories;

	public Integer getCalories() {
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Enums.ExerciseType getType() {
		return type;
	}

	public void setType(Enums.ExerciseType type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
