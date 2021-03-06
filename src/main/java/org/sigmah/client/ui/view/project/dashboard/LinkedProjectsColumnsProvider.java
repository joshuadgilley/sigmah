package org.sigmah.client.ui.view.project.dashboard;

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

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.ui.res.icon.dashboard.funding.FundingIconProvider;
import org.sigmah.client.util.NumberUtils;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.ProjectFundingDTO;
import org.sigmah.shared.dto.ProjectFundingDTO.LinkedProjectType;
import org.sigmah.shared.dto.referential.ProjectModelType;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Provides linked projects columns configuration.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public final class LinkedProjectsColumnsProvider extends LinkedProjectsAbstractProvider {

	private ProjectDTO project;
	
	/**
	 * Initializes the linked projects columns provider.
	 * 
	 * @param view
	 *          See {@link #view}.
	 * @param projectType
	 *          See {@link #projectType}.
	 */
	public LinkedProjectsColumnsProvider(final ProjectDashboardView view, final LinkedProjectType projectType) {
		super(view, projectType);
	}

	public void setProject(ProjectDTO project) {
		this.project = project;
	}
	
	/**
	 * Builds the columns for the funding/funded projects grids.
	 * 
	 * @return The columns for the funding/funded projects grids.
	 */
	public ColumnConfig[] getLinkedProjectsColumnModel() {

		// Icon.
		final ColumnConfig iconColumn = new ColumnConfig();
		iconColumn.setId("icon");
		iconColumn.setSortable(false);
		iconColumn.setWidth(15);
		iconColumn.setAlignment(HorizontalAlignment.CENTER);
		iconColumn.setRenderer(new GridCellRenderer<ProjectFundingDTO>() {

			@Override
			public Object render(ProjectFundingDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ProjectFundingDTO> store,
					Grid<ProjectFundingDTO> grid) {

				final ProjectModelType type = view.getPresenterHandler().getProjectModelType(getProject(model));

				return FundingIconProvider.getProjectTypeIcon(type).createImage();
			}
		});

		// Name.
		final ColumnConfig nameColumn = new ColumnConfig();
		nameColumn.setId(ProjectDTO.NAME);
		nameColumn.setHeaderText(I18N.CONSTANTS.projectName());
		nameColumn.setWidth(80);
		nameColumn.setRenderer(new GridCellRenderer<ProjectFundingDTO>() {

			@Override
			public Object render(final ProjectFundingDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ProjectFundingDTO> store,
					Grid<ProjectFundingDTO> grid) {

				final com.google.gwt.user.client.ui.Label label = new com.google.gwt.user.client.ui.Label(getProject(model).getName());
				// BUGFIX: #770 Using the right style name to display project name like an hyperlink.
				label.addStyleName("hyperlink-label");

				label.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						view.getPresenterHandler().onLinkedProjectClickEvent(getProject(model));
					}
				});

				return label;
			}
		});

		// Full name.
		final ColumnConfig fullNameColumn = new ColumnConfig();
		fullNameColumn.setId(ProjectDTO.FULL_NAME);
		fullNameColumn.setHeaderText(I18N.CONSTANTS.projectFullName());
		fullNameColumn.setWidth(130);
		fullNameColumn.setRenderer(new GridCellRenderer<ProjectFundingDTO>() {

			@Override
			public Object render(ProjectFundingDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ProjectFundingDTO> store,
					Grid<ProjectFundingDTO> grid) {

				return new Label(getProject(model).getFullName());
			}
		});

		// Amount.
		final ColumnConfig amountColumn = new ColumnConfig();
		amountColumn.setId(ProjectFundingDTO.PERCENTAGE);
		if (this.projectType == LinkedProjectType.FUNDING_PROJECT) {
			amountColumn.setHeaderHtml(I18N.CONSTANTS.projectFinances() + " (" + I18N.CONSTANTS.currencyEuro() + ')');
		} else {
			amountColumn.setHeaderHtml(I18N.CONSTANTS.projectFundedBy() + " (" + I18N.CONSTANTS.currencyEuro() + ')');
		}
		amountColumn.setWidth(120);

		// Percentage.
		final ColumnConfig percentageColumn = new ColumnConfig();
		percentageColumn.setId("percentage2");
		percentageColumn.setHeaderText(I18N.CONSTANTS.createProjectPercentage());
		percentageColumn.setWidth(60);
		percentageColumn.setRenderer(new GridCellRenderer<ProjectFundingDTO>() {

			@Override
			public Object render(ProjectFundingDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ProjectFundingDTO> store,
					Grid<ProjectFundingDTO> grid) {

				// The amount of the funding.
				final Double amount = model.getPercentage();

				// The current project budget.
				final Double budget = projectType == LinkedProjectType.FUNDING_PROJECT ?
						project.getPlannedBudget() :
						getProject(model).getPlannedBudget();
				
				return new Label(NumberUtils.ratioAsString(amount, budget));
			}
		});

		// Edit icon.
		final ColumnConfig editButtonColumn = new ColumnConfig();
		editButtonColumn.setId("editButton");
		editButtonColumn.setSortable(false);
		editButtonColumn.setWidth(30);
		editButtonColumn.setAlignment(HorizontalAlignment.LEFT);
		editButtonColumn.setRenderer(new LinkedProjectsEditButtonCellRender(view, projectType));

		return new ColumnConfig[] {
																iconColumn,
																nameColumn,
																fullNameColumn,
																amountColumn,
																percentageColumn,
																editButtonColumn
		};
	}

}
