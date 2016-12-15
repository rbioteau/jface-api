/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.jface.api.validator;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;


/**
 * @author Romain Bioteau
 *
 */
public class InputLengthValidator implements IValidator {

    private final int maxChar;
    private final String errorMessage;
	private final int minChar;

    public InputLengthValidator(final String errorMessage, final int maxChar) {
        this(errorMessage, 0, maxChar);
    }

    public InputLengthValidator(final String errorMessage, final int minChar, final int maxChar) {
        this.maxChar = maxChar ;
        this.errorMessage = errorMessage;
        this.minChar = minChar;
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
     */
    @Override
    public IStatus validate(final Object input) {
    	 if(input != null && input.toString().trim().length() < minChar){
            return ValidationStatus.error(errorMessage);
         }
        if (input != null && input.toString().length() > maxChar) {
            return ValidationStatus.error(errorMessage);
        }
        return ValidationStatus.ok() ;
    }

}
