package org.sonatype.plugins.daniel.events;

import org.apache.maven.eventspy.EventSpy;
import org.sonatype.plugins.daniel.DanielBarenboim;

/**
 * Event sent out by the conductor when {@link EventSpy#close()} was invoked on it. Players might (but does not have to)
 * listen to this event to perform some cleanup step.
 * 
 * @author cstamas
 */
public class PutDownInstrumentEvent
    extends ConductorEvent
{
    public PutDownInstrumentEvent( final DanielBarenboim conductor )
    {
        super( conductor );
    }
}
