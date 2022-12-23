package org.vaadin.example;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.vaadin.components.PersonComboBoxOnCallbacks;
import org.vaadin.components.PersonComboBoxOnList;
import org.vaadin.components.PingComponent;
import org.vaadin.entities.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main view contains a button and a click listener.
 * + PingComponent
 */
@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@Push
public class MainView extends VerticalLayout {

    // Session => Timestamp mapping
    private static final Map<VaadinSession, Long> sessionTsMap = new ConcurrentHashMap<>();
    private static final Long PING_MAX_TIMEOUT_MS = 10_000L;
    static {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
                MainView::detectDeadSessions, 1, 1, TimeUnit.SECONDS
        );
    }


    public MainView() {
        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");


        PingComponent pingComponent = new PingComponent(this::listenPingReceived);
        PersonComboBoxOnCallbacks personComboBoxOnCallbacks = new PersonComboBoxOnCallbacks();
        PersonComboBoxOnList personComboBoxOnList = new PersonComboBoxOnList();

        add(
                pingComponent,
                personComboBoxOnCallbacks.getComponent(),
                personComboBoxOnList.getComponent()
        );

    }


    /**
     * Inform all UIs about new ping event from one of PingComponents
     * + update time marker of Ping overall all VaadinSession's UIs
     */
    private void listenPingReceived() {
        UI currentUI = UI.getCurrent();
        VaadinSession vaadinSession = currentUI.getSession();
        sessionTsMap.put(vaadinSession, System.currentTimeMillis());
        vaadinSession.getUIs().forEach(ui -> {
            Notification.show("Ping event received from UI: " + ui.getUIId(), 10_000, Notification.Position.BOTTOM_END);
        });
    }


    private static void detectDeadSessions() {
        try {
            if (sessionTsMap.isEmpty()) {
                System.out.println("No active sessions...");
                return;
            }

            sessionTsMap.forEach((session, ts) -> {
                session.access(() -> {
                    System.out.println("VaadinSession=" + session.hashCode()
                            + ", UIs count=" + session.getUIs().size()
                            + ", LastPingAge=" + (System.currentTimeMillis() - ts));
                });
            });

            sessionTsMap.forEach((session, ts) -> {
                long pingTimeout = System.currentTimeMillis() - ts;
                if (pingTimeout > PING_MAX_TIMEOUT_MS) {
                    session.access(() -> session.getUIs().forEach(UI::close));
                    session.access(session::close);
                    System.out.println("VaadinSession " + session.hashCode() + " and UIs closed");
                    sessionTsMap.remove(session);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
