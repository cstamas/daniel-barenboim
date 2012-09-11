package org.sonatype.plugins.daniel.events;

import org.sonatype.plugins.daniel.DanielBarenboim;

public class PutDownInstrumentEvent
    extends ConductorEvent
{
    public PutDownInstrumentEvent( final DanielBarenboim conductor )
    {
        super( conductor );
    }
}
