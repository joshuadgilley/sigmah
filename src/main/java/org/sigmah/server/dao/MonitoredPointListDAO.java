package org.sigmah.server.dao;

/*
 * #%L
 * Sigmah
 * %%
 * Copyright (C) 2010 - 2016 URD
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.sigmah.server.dao.base.DAO;
import org.sigmah.server.domain.reminder.MonitoredPointList;

/**
 * Data Access Object for the {@link org.sigmah.server.domain.reminder.MonitoredPointList} domain class.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public interface MonitoredPointListDAO extends DAO<MonitoredPointList, Integer> {

	/**
	 * Finds the given {@code projectId} related {@link MonitoredPointList}.
	 * 
	 * @param projectId
	 *          The project id.
	 * @return The given {@code projectId} related {@link MonitoredPointList}, or {@code null}.
	 */
	MonitoredPointList findByProjectId(Integer projectId);

}
