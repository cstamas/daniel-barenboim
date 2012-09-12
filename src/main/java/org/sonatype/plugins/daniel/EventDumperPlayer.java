package org.sonatype.plugins.daniel;

import java.util.Stack;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.sonatype.aether.RepositoryEvent;
import org.sonatype.aether.RequestTrace;

import com.google.common.eventbus.Subscribe;

@Component( role = Player.class, hint = "org.sonatype.plugins.daniel.EventDumperPlayer" )
public class EventDumperPlayer
{
    @Requirement
    private Logger logger;

    public EventDumperPlayer()
    {
        this.active = Boolean.getBoolean( "daniel.debug" );
    }

    @Subscribe
    public void onAnyEvent( final Object object )
    {
        if ( !isActive() )
        {
            return;
        }

        logger.info( String.valueOf( object ) + " (" + ( object == null ? "" : object.getClass().getName() ) + ")" );

        if ( object instanceof RepositoryEvent )
        {
            final RepositoryEvent revt = (RepositoryEvent) object;
            final Stack<RequestTrace> stack = new Stack<RequestTrace>();
            {
                RequestTrace trace = revt.getTrace();
                while ( trace != null )
                {
                    stack.push( trace );
                    trace = trace.getParent();
                }
            }
            if ( stack.size() > 0 )
            {
                int level = 1;
                int indentSpaces = 2;
                for ( RequestTrace trace : stack )
                {
                    logger.info( String.format( "%" + ( level * indentSpaces ) + "s%s", "",
                        String.valueOf( trace.getData() ) ) );
                    level++;
                }
            }
        }
    }

    // ==

    private volatile boolean active;

    protected boolean isActive()
    {
        return active;
    }

    protected void setActive( final boolean active )
    {
        this.active = active;
    }
}
