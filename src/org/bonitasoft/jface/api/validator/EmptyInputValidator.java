/**
 * Copyright (C) 2012 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.jface.api.validator;


import java.util.Optional;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class EmptyInputValidator extends TypedValidator<String, IStatus> {

    private final String errorMessage;

    public EmptyInputValidator(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    protected IStatus doValidate(String value) {
        return Optional.ofNullable(value).orElse("").trim().isEmpty() ? ValidationStatus.error(errorMessage) : ValidationStatus.ok();
    }

}
