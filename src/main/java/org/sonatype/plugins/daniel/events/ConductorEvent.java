package org.sonatype.plugins.daniel.events;

import org.apache.maven.eventspy.EventSpy.Context;
import org.sonatype.plugins.daniel.DanielBarenboim;

import com.google.common.base.Preconditions;

public abstract class ConductorEvent
{
    private final DanielBarenboim conductor;

    public ConductorEvent( final DanielBarenboim conductor )
    {
        this.conductor = Preconditions.checkNotNull( conductor, "Passed in conductor is null!" );
    }

    public DanielBarenboim getConductor()
    {
        return conductor;
    }

    public Context getContext()
    {
        return getConductor().getContext();
    }
}
