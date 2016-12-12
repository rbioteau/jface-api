package org.bonitasoft.jface.api;

import static org.bonitasoft.jface.api.Wizard.newWizard;
import static org.bonitasoft.jface.api.WizardPage.newPage;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;


public class WizardTest {

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
            final Wizard wizard = newWizard("My Wizard Window", () -> {
                return MessageDialog.openConfirm(Display.getDefault().getActiveShell(), String.format("Create %s ?", person.getName()),
                        "A new person will be added into the contact list.");
            }, newPage("Page1", "Desc1", person, (parent, ctx, model) -> {
                final Composite container = new Composite(parent, SWT.NONE);
                container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(15, 10).create());
                final TextWithLabel nameControl = new TextWithLabel(container, SWT.BORDER).withLabel("Name");
                nameControl.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
                final UpdateValueStrategy updateValueStrategy = new UpdateValueStrategy();
                updateValueStrategy.setAfterConvertValidator(value -> {
                    return value == null || ((String) value).isEmpty() ? ValidationStatus.error("Name is mandatory") : ValidationStatus.ok();
                });
                nameControl.bindText(ctx, PojoObservables.observeValue(model, "name"), updateValueStrategy, null);

                return container;
            }));

            new WizardDialog(Display.getDefault().getActiveShell(), wizard.asWizard()).open();
        });

    }

}
