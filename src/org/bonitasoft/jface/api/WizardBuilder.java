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

import java.util.stream.Stream;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

public class WizardBuilder {

    public static WizardBuilder newWizard(String windowTitle, FinishHandler finishHandler, WizardPageBuilder... pages) {
        return new WizardBuilder(windowTitle, finishHandler, pages);
    }

    private final String windowTitle;
    private final WizardPageBuilder[] pages;
    private final FinishHandler finishHandler;

    private WizardBuilder(String windowTitle, FinishHandler finishHandler, WizardPageBuilder... pages) {
        this.windowTitle = windowTitle;
        this.pages = pages;
        this.finishHandler = finishHandler;
    }

    public Wizard asWizard() {
        final Wizard wizard = new Wizard() {

            @Override
            public boolean performFinish() {
                return finishHandler.finish();
            }
        };
        Stream.of(pages).forEachOrdered(page -> {
            wizard.addPage(page.asPage());
        });
        wizard.setWindowTitle(windowTitle);
        return wizard;
    }

    public int open() {
        return new WizardDialog(Display.getDefault().getActiveShell(), asWizard()).open();
    }
}
