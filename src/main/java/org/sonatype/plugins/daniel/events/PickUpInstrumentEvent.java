package org.sonatype.plugins.daniel.events;

import org.sonatype.plugins.daniel.DanielBarenboim;

public class PickUpInstrumentEvent
    extends ConductorEvent
{
    public PickUpInstrumentEvent( final DanielBarenboim conductor )
    {
        super( conductor );
    }
}
