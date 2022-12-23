package org.vaadin.example;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.combobox.ComboBox;

import java.io.Serializable;

public class AutoComboBox<T> extends ComboBox<T> {

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        this.getElement().getNode().runWhenAttached((ui) -> {
            ui.beforeClientResponse(this, (context) -> {
                ui.getPage().executeJs("$0.addEventListener('filter-changed', () => {setTimeout(() => {$0._focusedIndex = 0;});});", new Serializable[]{this.getElement()});
            });
        });
    }
}
