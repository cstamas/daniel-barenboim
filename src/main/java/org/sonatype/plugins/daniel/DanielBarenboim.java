package org.sonatype.plugins.daniel;

import java.util.Map;

import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.eventspy.EventSpy;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.sonatype.plugins.daniel.events.PickUpInstrumentEvent;
import org.sonatype.plugins.daniel.events.PutDownInstrumentEvent;

import com.google.common.eventbus.EventBus;

/**
 * A Maven EventSpy implementation that serves as a "conductor", employing Guava's EventBus to make EventSpy processing
 * type-safe and easier.
 * 
 * @author cstamas
 */
@Component( role = EventSpy.class, hint = "daniel-barenboim" )
public class DanielBarenboim
    extends AbstractEventSpy
{
    @Requirement
    private Logger logger;

    @Requirement( role = Player.class )
    private Map<String, Object> players;

    private final EventBus eventBus;

    private Context context;

    private boolean playing;

    public DanielBarenboim()
    {
        this.eventBus = new EventBus();
    }

    public void init( final Context context )
        throws Exception
    {
        this.context = context;
        this.playing = true;

        // register players
        for ( Object player : players.values() )
        {
            eventBus.register( player );
        }

        // make them pick up the instruments
        eventBus.post( new PickUpInstrumentEvent( this ) );
    }

    public void onEvent( final Object event )
        throws Exception
    {
        // conduct
        eventBus.post( event );
    }

    public synchronized void close()
        throws Exception
    {
        if ( playing )
        {
            // make them put down the instruments, the show is over
            eventBus.post( new PutDownInstrumentEvent( this ) );
            playing = false;
        }
    }

    // ==

    public synchronized Context getContext()
    {
        if ( !isPlaying() )
        {
            throw new IllegalStateException( "Orchestra is not playing anymore!" );
        }
        return context;
    }

    public boolean isPlaying()
    {
        return playing;
    }
}
