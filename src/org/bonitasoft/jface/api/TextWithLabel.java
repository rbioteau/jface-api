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
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TextWithLabel extends Composite {

    private final Label label;
    private final Text text;
    private final Label messageLabel;
    private final LocalResourceManager resourceManager;
    private final Color errorColor;

    public TextWithLabel(Composite parent, int textStyle) {
        super(parent, SWT.NONE);
        this.resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
        errorColor = resourceManager.createColor(new RGB(214, 77, 77));

        setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(0, 0).create());

        label = new Label(this, SWT.NONE);
        label.setLayoutData(GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).create());

        text = new Text(this, textStyle);
        text.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

        final Label filler = new Label(this, SWT.NONE);
        filler.setLayoutData(GridDataFactory.swtDefaults().create());

        messageLabel = new Label(this, SWT.NONE);
        messageLabel.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).indent(0, -5).create());
        messageLabel.setForeground(errorColor);
    }

    public TextWithLabel withLabel(String labelText) {
        label.setText(labelText);
        return this;
    }

    public ISWTObservableValue observeText(int event) {
        return SWTObservables.observeText(text, event);
    }

    public ControlMessageSupport bindText(DataBindingContext ctx, IObservableValue modelObservable, UpdateValueStrategy targetToModel,
            UpdateValueStrategy modelToTarget) {
        return new ControlMessageSupport(ctx.bindValue(observeText(SWT.Modify), modelObservable,
                targetToModel, modelToTarget)) {

            @Override
            protected void statusChanged(IStatus status) {
                if (status == null || status.isOK()) {
                    text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_WHITE));
                    messageLabel.setText("");
                } else {
                    text.setBackground(errorColor);
                    messageLabel.setText(status.getMessage());
                }
            }
        };
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        resourceManager.dispose();
    }

}
