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
package org.bonitasoft.jface.api.widget;

import org.bonitasoft.jface.api.databinding.ControlMessageSupport;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class TextWithLabel extends Composite {

    private static final RGB ERROR_RGB = new RGB(214, 77, 77);
    private static final RGB WARNING_RGB = new RGB(155, 170, 20);
    private final Label label;
    private final Text text;
    private final Label messageLabel;
    private final LocalResourceManager resourceManager;
    private final Color errorColor;
    private Color warningColor;
    private IStatus status = ValidationStatus.ok();
    private String message;



    /**
     * @param parent
     * @param style Supports all {@link Text} style in addition to SWT.TOP for the label position
     */
    public TextWithLabel(Composite parent, int style) {
        super(parent, SWT.NONE);
        this.resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
        errorColor = resourceManager.createColor(ERROR_RGB);
        warningColor = resourceManager.createColor(WARNING_RGB);
        setLayout(GridLayoutFactory.fillDefaults().numColumns(topLabel(style) ? 1 : 2)
                .spacing(LayoutConstants.getSpacing().x, topLabel(style) ? 1 : LayoutConstants.getSpacing().y).create());

        label = new Label(this, SWT.NONE);
        label.setLayoutData(GridDataFactory.swtDefaults().align(topLabel(style) ? SWT.LEFT : SWT.RIGHT, SWT.CENTER).create());

        final Composite textContainer = new Composite(this, SWT.NONE);
        textContainer.setLayout(GridLayoutFactory.fillDefaults().margins(1, 3).create());
        textContainer.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
        textContainer.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        textContainer.addListener(SWT.Paint, e -> drawBorder(textContainer, e));

        int textStyle = 0;
        if ((style | SWT.BORDER) == 0) {
            textStyle = style ^ SWT.BORDER;
        }

        text = new Text(textContainer, SWT.SINGLE | textStyle);
        text.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
        text.addListener(SWT.FocusIn, event -> redraw(textContainer));
        text.addListener(SWT.FocusOut, event -> redraw(textContainer));


        if (!topLabel(style)) {
            final Label filler = new Label(this, SWT.NONE);
            filler.setLayoutData(GridDataFactory.swtDefaults().create());
        }

        messageLabel = new Label(this, SWT.NONE);
        messageLabel.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.FILL).create());
        messageLabel.setForeground(getStatusColor(status));
    }

    private void drawBorder(final Composite textContainer, Event e) {
        final GC gc = e.gc;
        final Display display = e.display;
        if (display != null && gc != null && !gc.isDisposed()) {
            final Control focused = display.getFocusControl();
            gc.setAdvanced(true);
            gc.setForeground(getBorderColor(focused, textContainer));
            gc.setLineWidth(1);
            final Rectangle r = textContainer.getBounds();
            gc.drawRectangle(0, 0, r.width - 1, r.height - 1);
        }
    }

    private void redraw(final Composite toRedraw) {
        toRedraw.getDisplay().asyncExec(() -> toRedraw.redraw());
    }

    protected Color getBorderColor(Control focused, Control textContainer) {
        if (status.isOK() || status.getSeverity() == IStatus.INFO) {
            if (focused != null && focused.getParent() != null && focused.getParent().equals(textContainer)) {
                return focused.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BORDER);
            } else {
                return focused.getDisplay().getSystemColor(SWT.COLOR_GRAY);
            }
        }
        return getStatusColor(status);
    }

    protected Color getStatusColor(IStatus status) {
        return status.getSeverity() == IStatus.WARNING ? warningColor
                : status.getSeverity() == IStatus.ERROR ? errorColor : Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
    }

    private boolean topLabel(int style) {
        return (style & SWT.TOP) != 0;
    }

    public TextWithLabel withLabel(String labelText) {
        label.setText(labelText);
        return this;
    }

    public TextWithLabel withMessage(String message) {
        this.message = message;
        messageLabel.setText(message);
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
                TextWithLabel.this.status = status;
                if (status == null || status.isOK()) {
                    messageLabel.setText(message != null ? message : "");
                } else {
                    messageLabel.setText(status.getMessage());
                }
                messageLabel.setForeground(getStatusColor(status));
                text.getParent().redraw();
                messageLabel.getParent().layout();
            }
        };
    }


}
