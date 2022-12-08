/**
 * Browser-side part for page ping
 *
 * @see https://polymer-library.polymer-project.org/3.0/docs/devguide/data-binding
 * @see https://polymer-library.polymer-project.org/3.0/docs/devguide/events
 *
 */
import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';

class PingComponent extends PolymerElement {

    constructor() {
        super();
        /* ping send period 5000 ms */
        setInterval(() => this.invokePingEvent(), 5000);
    }

    static get template() {
        return html`
            <div></div>
        `;
    }

    static get is() {
        return 'ping-component';
    }

    // Linked to backend method PingComponent#invokePingEvent
    invokePingEvent() {
    }
}

customElements.define(PingComponent.is, PingComponent);
