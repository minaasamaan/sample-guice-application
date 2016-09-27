package com.training.guice.rest;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.training.guice.dto.exercise.ExerciseDto;
import com.training.guice.jpa.domain.Enums;
import com.training.guice.jpa.domain.Exercise;

import io.swagger.annotations.Api;

/**
 * @author Mina
 *
 */
/**
 * @author Mina
 *
 */
@Path("/api/v1/exercise")
@Api(value = "Exercise Service")
public interface ExerciseService {

	/**
	 * Get the exercise for a given exerciseId.
	 *
	 * @param exerciseId
	 *            id to search
	 * @return the exercise for the given exerciseId
	 */
	@GET
	@Path("/{exerciseId}")
	@Nonnull
	@Produces(MediaType.APPLICATION_JSON)
	Exercise getExerciseById(@Nonnull @PathParam("exerciseId") Long exerciseId);

	/**
	 * Get the exercises with the given description.
	 *
	 * @param description
	 *            description to search
	 * @return the exercises for the given description
	 */
	@GET
	@Path("/")
	@Nonnull
	@Produces(MediaType.APPLICATION_JSON)
	List<Exercise> getExerciseByDescription(@Nullable @QueryParam("description") String description);

	/**
	 * List all existing exercises for a given user id. Adding the ability to
	 * filter them by type and/or by date.
	 * 
	 * @param userId
	 * @param type
	 * @param date
	 * @return
	 */
	@GET
	@Path("/user/{userId}")
	@Nonnull
	@Produces(MediaType.APPLICATION_JSON)
	List<ExerciseDto> getExercises(@Nonnull @PathParam("userId") Long userId,
			@QueryParam("type") Enums.ExerciseType type, @QueryParam("date") String date);

	/**
	 * Rank a list of user ids by the users' achieved points
	 * 
	 * @param userIds
	 * @return list of user ids. Ordered according to the users' points in
	 *         descending order
	 */
	@GET
	@Path("/ranks/users")
	@Nonnull
	@Produces(MediaType.APPLICATION_JSON)
	public List<Long> getRanking(@QueryParam("id") final List<Long> userIds);

	/**
	 * - Insert new exercises. All exercise fields should be mandatory for
	 * inserting (except the id). Provided description should has a valid syntax
	 * (only alphnum + spaces) . Exercise persisting is being checked that there
	 * is no other exercise already present for the user id, in the period
	 * (start + duration) where the new exercise will take place. If this is the
	 * case, an HTTP status code `Conflict` returned.
	 * 
	 * @param exerciseDto
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	ExerciseDto createExercise(ExerciseDto exerciseDto);

	/**
	 * Update exercise. All exercise fields should be mandatory for updating.
	 * Also the user id and type shouldn't change. Provided description should
	 * has a valid syntax (only alphnum + spaces). Exercise persisting is being
	 * checked that there is no other exercise already present for the user id,
	 * in the period (start + duration) where the new exercise will take place.
	 * If this is the case, an HTTP status code `Conflict` returned.
	 * 
	 * @param exerciseDto
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	ExerciseDto updateExercise(ExerciseDto exerciseDto);

	/**
	 * Delete the exercise for a given exerciseId.
	 * 
	 * @param exerciseId
	 */
	@DELETE
	@Path("/{exerciseId}")
	@Produces(MediaType.APPLICATION_JSON)
	void deleteExercise(@NotNull @PathParam("exerciseId") Long exerciseId);
}
