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

public class Wizard {

    public static Wizard newWizard(String windowTitle, FinishHandler finishHandler, WizardPage... pages) {
        return new Wizard(windowTitle, finishHandler, pages);
    }

    private final String windowTitle;
    private final WizardPage[] pages;
    private final FinishHandler finishHandler;

    private Wizard(String windowTitle, FinishHandler finishHandler, WizardPage... pages) {
        this.windowTitle = windowTitle;
        this.pages = pages;
        this.finishHandler = finishHandler;
    }

    public org.eclipse.jface.wizard.Wizard asWizard() {
        final org.eclipse.jface.wizard.Wizard wizard = new org.eclipse.jface.wizard.Wizard() {

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
}
