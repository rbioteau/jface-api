/**
 * Copyright (C) 2014-2015 Bonitasoft S.A.
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

// github.com/bonitasoft/bonita-studio.git
import org.eclipse.core.databinding.validation.IValidator;

/**
 * @author Romain Bioteau
 */
public class ValidatorFactory {

    public static MultiValidatorFactory multiValidator() {
        return new MultiValidatorFactory(new MultiValidator());
    }

    public static IValidator maxLengthValidator(final String inputName, final int maxLength) {
        return new InputLengthValidator(inputName, maxLength);
    }

    public static IValidator minMaxLengthValidator(final String inputName, final int minLength, final int maxLength) {
        return new InputLengthValidator(inputName, minLength, maxLength);
    }

    public static IValidator mandatoryValidator(final String inputName) {
        return new EmptyInputValidator(inputName);
    }

    public static IValidator regExpValidator(final String errorMessage, final String regExp) {
        return new RegExpValidator(errorMessage, regExp);
    }


}
