package org.sonatype.plugins.sessiontracker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.aether.ConfigurationProperties;
import org.sonatype.aether.util.ConfigUtils;
import org.sonatype.aether.util.DefaultRepositorySystemSession;

/**
 * Default and dirty implementation of {@link SessionManager}.
 * 
 * @author cstamas
 */
@Component( role = SessionManager.class )
public class DefaultSessionManager
    implements SessionManager
{
    private static final String HEADER_SESSION_ID = "X-Maven-Session";

    @Requirement
    private RepositorySystem repositorySystem;

    private UUID sessionId;

    public synchronized void started( final MavenSession session )
    {
        if ( sessionId != null )
        {
            throw new IllegalStateException( String.format( "Session %s already exists!", sessionId.toString() ) );
        }
        sessionId = UUID.randomUUID();

        // install extra header
        processHeader( session, true );
    }

    public synchronized void stopped( final MavenSession session )
    {
        if ( sessionId == null )
        {
            throw new IllegalStateException( "No session exists to stop it!" );
        }
        // uninstall extra header
        processHeader( session, false );

        sessionId = null;
    }

    public String getSessionId()
    {
        return sessionId.toString();
    }

    // ==

    @SuppressWarnings( "unchecked" )
    protected void processHeader( final MavenSession session, final boolean install )
    {
        // install or uninstall extra header
        // Note: this below is kinda-illegal: Aether session is meant to be unmodified after created
        // but still, it works. A refactoring would be needed to move this work below out of this
        // component, best would be to make Maven expose some API to do this cleanly.
        if ( session.getRepositorySession() instanceof DefaultRepositorySystemSession )
        {
            boolean workDone = false;
            final DefaultRepositorySystemSession aetherSession =
                (DefaultRepositorySystemSession) session.getRepositorySession();
            for ( String sessionPropertyKey : aetherSession.getConfigProperties().keySet() )
            {
                if ( sessionPropertyKey.startsWith( ConfigurationProperties.HTTP_HEADERS ) )
                {
                    final Map<String, String> httpHeaders =
                        (Map<String, String>) ConfigUtils.getObject( aetherSession, new HashMap<String, String>(),
                            sessionPropertyKey );
                    if ( install )
                    {
                        httpHeaders.put( HEADER_SESSION_ID, getSessionId() );
                    }
                    else
                    {
                        httpHeaders.remove( HEADER_SESSION_ID );
                    }
                    aetherSession.setConfigProperty( sessionPropertyKey, httpHeaders );
                    workDone = true;
                }
            }

            if ( install && !workDone )
            {
                final Map<String, String> httpHeaders = new HashMap<String, String>();
                httpHeaders.put( HEADER_SESSION_ID, getSessionId() );
                aetherSession.setConfigProperty( ConfigurationProperties.HTTP_HEADERS, httpHeaders );
            }
        }
    }
}
