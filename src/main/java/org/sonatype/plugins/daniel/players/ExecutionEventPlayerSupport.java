package org.sonatype.plugins.daniel.players;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.ExecutionEvent.Type;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import com.google.common.eventbus.Subscribe;

public abstract class ExecutionEventPlayerSupport
{
    @Requirement
    private Logger logger;

    private final Set<Type> observedTypes;

    protected ExecutionEventPlayerSupport( Type... observedTypes )
    {
        final Set<Type> types = new HashSet<Type>();
        for ( Type observedType : observedTypes )
        {
            types.add( observedType );
        }
        this.observedTypes = Collections.unmodifiableSet( types );
    }

    @Subscribe
    public final void onEvent( final ExecutionEvent event )
    {
        if ( observedTypes.contains( event.getType() ) )
        {
            processEvent( event );
        }
    }

    // ==

    protected Logger getLogger()
    {
        return logger;
    }

    protected abstract void processEvent( final ExecutionEvent event );
}
