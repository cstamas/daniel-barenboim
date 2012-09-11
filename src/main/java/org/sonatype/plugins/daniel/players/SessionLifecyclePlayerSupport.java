package org.sonatype.plugins.daniel.players;

import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.ExecutionEvent.Type;

public abstract class SessionLifecyclePlayerSupport
    extends ExecutionEventPlayerSupport
{
    public SessionLifecyclePlayerSupport()
    {
        super( Type.SessionStarted, Type.SessionEnded );
    }

    @Override
    protected void processEvent( final ExecutionEvent event )
    {
        if ( Type.SessionStarted.equals( event.getType() ) )
        {
            onSessionStarted( event );
        }
        if ( Type.SessionEnded.equals( event.getType() ) )
        {
            onSessionEnded( event );
        }
    }

    // ==

    protected abstract void onSessionStarted( final ExecutionEvent event );

    protected abstract void onSessionEnded( final ExecutionEvent event );
}
