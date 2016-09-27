/**
 * 
 */
package com.training.guice.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.training.guice.validation.groups.CRUDGroup.Create;
import com.training.guice.validation.groups.CRUDGroup.Update;

/**
 * @author Mina
 *
 */
public abstract class AbstractDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Id value does not exist while operation is 'Update'", groups = { Update.class })
	@Null(message = "Id value exists while operation is 'Create'", groups = { Create.class })
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
