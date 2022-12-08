package org.vaadin.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * Polymer-component for page ping
 */
@Tag("ping-component")
@JsModule("./src/ping-component.js")
public class PingComponent extends PolymerTemplate<TemplateModel> {

    private final transient Runnable updatePingTsRunnable;

    public PingComponent(Runnable updatePingTsRunnable) {
        this.updatePingTsRunnable = updatePingTsRunnable;
    }

    @EventHandler
    private void invokePingEvent() {
        //System.out.println("Ping received");
        updatePingTsRunnable.run();
    }
}