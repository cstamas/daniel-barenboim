package org.sonatype.plugins.daniel.events;

import org.apache.maven.eventspy.EventSpy;
import org.sonatype.plugins.daniel.DanielBarenboim;

/**
 * Event sent out by the conductor when {@link EventSpy#init(org.apache.maven.eventspy.EventSpy.Context)} was invoked on
 * it. Players might (but does not have to) listen to this event to perform some init step.
 * 
 * @author cstamas
 */
public class PickUpInstrumentEvent
    extends ConductorEvent
{
    public PickUpInstrumentEvent( final DanielBarenboim conductor )
    {
        super( conductor );
    }
}
