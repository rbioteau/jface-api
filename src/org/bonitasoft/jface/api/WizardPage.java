/**
 * Copyright (C) 2016 Bonitasoft S.A.
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
package org.bonitasoft.jface.api;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.widgets.Composite;

public class WizardPage {

    public static WizardPage newPage(final String title, final String description, Object model, final ControlSupplier controlSupplier) {
        return new WizardPage(title, description,model, controlSupplier);
    }

    private final String title;
    private final String description;
    private final ControlSupplier controlSupplier;
    private final DataBindingContext ctx;
    private final Object model;

    private WizardPage(String title, String description, Object model, final ControlSupplier controlSupplier) {
        this.title = title;
        this.description = description;
        this.controlSupplier = controlSupplier;
        this.model = model;
        ctx = new DataBindingContext();
    }

    public org.eclipse.jface.wizard.WizardPage asPage() {
        final org.eclipse.jface.wizard.WizardPage page = new org.eclipse.jface.wizard.WizardPage(title, title, null) {

            @Override
            public void createControl(Composite parent) {
                NoMessageWizardPageSupport.create(this, ctx);
                setControl(controlSupplier.get(parent, ctx, model));
            }

            /*
             * (non-Javadoc)
             * @see org.eclipse.jface.dialogs.DialogPage#dispose()
             */
            @Override
            public void dispose() {
                ctx.dispose();
                super.dispose();
            }
        };
        page.setDescription(description);
        return page;
    }


}
