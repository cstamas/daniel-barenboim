package org.sonatype.plugins.sessiontracker;

import org.apache.maven.execution.ExecutionEvent;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.plugins.daniel.Player;
import org.sonatype.plugins.daniel.players.SessionLifecyclePlayerSupport;

/**
 * A player that listens for session related events, and drives {@link SessionManager} component.
 * 
 * @author cstamas
 */
@Component( role = Player.class, hint = "org.sonatype.plugins.sessiontracker.SessionTrackerPlayer" )
public class SessionTrackerPlayer
    extends SessionLifecyclePlayerSupport
{
    @Requirement
    private SessionManager sessionManager;

    @Override
    protected void onSessionStarted( final ExecutionEvent event )
    {
        sessionManager.started( event.getSession() );
    }

    @Override
    protected void onSessionEnded( final ExecutionEvent event )
    {
        sessionManager.stopped( event.getSession() );
    }
}
