package org.bonitasoft.jface.api;

import static org.bonitasoft.jface.api.WizardBuilder.newWizard;
import static org.bonitasoft.jface.api.WizardPageBuilder.newPage;
import static org.bonitasoft.jface.api.databinding.UpdateStrategyFactory.updateValueStrategy;

import org.bonitasoft.jface.api.widget.TextWithLabel;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;


public class WizardBuilderTest {

    class Person {

        private String name;

        Person(String name) {
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void should_create_a_new_wizard() throws Exception {
        Realm.runWithDefault(new Realm() {
            
            @Override
            public boolean isCurrent() {
                return true;
            }
        }, () -> {
            final Person person = new Person(null);
            newWizard("My Wizard Window", () -> {
                return MessageDialog.openConfirm(Display.getDefault().getActiveShell(), String.format("Create %s ?", person.getName()),
                        "A new person will be added into the contact list.");
            }, newPage("Page1", "Desc1", person, (parent, ctx, model) -> {

                final Composite container = new Composite(parent, SWT.NONE);
                container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(15, 10).create());

                final TextWithLabel nameControl = new TextWithLabel(container, SWT.TOP).withLabel("Name");
                nameControl.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

                nameControl.bindText(ctx, PojoObservables.observeValue(model, "name"), updateValueStrategy().withValidator(value -> {
                    return value == null || ((String) value).isEmpty() ? ValidationStatus.error("Name is mandatory") : ValidationStatus.ok();
                }).create(), null);

                final TextWithLabel title = new TextWithLabel(container, SWT.TOP).withLabel("Title").withMessage("Example: Mr, Ms..");
                title.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
                title.bindText(ctx, PojoObservables.observeValue(model, "name"), updateValueStrategy().withValidator(value -> {
                    return value == null || ((String) value).isEmpty() ? ValidationStatus.warning("Title is missing") : ValidationStatus.ok();
                }).create(), null);

                return container;
            })).open();

        });

    }

}
