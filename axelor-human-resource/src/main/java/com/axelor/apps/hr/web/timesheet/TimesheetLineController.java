/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2018 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.hr.web.timesheet;

import java.math.BigDecimal;

import com.axelor.apps.hr.db.Timesheet;
import com.axelor.apps.hr.db.TimesheetLine;
import com.axelor.apps.hr.service.timesheet.TimesheetLineService;
import com.axelor.exception.service.TraceBackService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;

public class TimesheetLineController {

    private final String HOURS_DURATION_FIELD = "hoursDuration";

    /**
     * Called from timesheet line editable grid or form.
     * Get the timesheet corresponding to timesheetline and call
     * {@link TimesheetLineService#computeHoursDuration(Timesheet, BigDecimal, boolean)}
     *
     * @param request
     * @param response
     */
    public void setStoredDuration(ActionRequest request, ActionResponse response) {
        try {
            TimesheetLine timesheetLine = request.getContext().asType(TimesheetLine.class);
            Timesheet timesheet;
            Context parent = request.getContext().getParent();
            if (parent != null && parent.getContextClass().equals(Timesheet.class)) {
                timesheet = parent.asType(Timesheet.class);
            } else {
                timesheet = timesheetLine.getTimesheet();
            }
            BigDecimal hoursDuration = Beans.get(TimesheetLineService.class)
                    .computeHoursDuration(timesheet, timesheetLine.getDuration(), true);

            response.setValue(HOURS_DURATION_FIELD, hoursDuration);

        } catch (Exception e) {
            TraceBackService.trace(response, e);
        }
    }
}