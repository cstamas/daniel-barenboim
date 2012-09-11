package org.sonatype.plugins.sessiontracker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.repository.RepositorySystem;
import org.apache.maven.settings.Server;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.aether.ConfigurationProperties;
import org.sonatype.aether.util.ConfigUtils;
import org.sonatype.aether.util.DefaultRepositorySystemSession;

@Component( role = SessionManager.class )
public class DefaultSessionManager
    implements SessionManager
{
    private static final String HEADER_SESSION_ID = "X-Maven-Session";

    @Requirement
    private RepositorySystem repositorySystem;

    private UUID sessionId;

    public void started( final MavenSession session )
    {
        if ( sessionId != null )
        {
            throw new IllegalStateException( String.format( "Session %s already exists!", sessionId.toString() ) );
        }
        sessionId = UUID.randomUUID();

        // install extra header
        processHeader( session, true );
    }

    public void stopped( final MavenSession session )
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
    protected void processHeader( final MavenSession session, boolean install )
    {
        // put the stuff into Servers?
        for ( Server serverEntry : session.getSettings().getServers() )
        {
            Xpp3Dom configEntry = (Xpp3Dom) serverEntry.getConfiguration();
            if ( configEntry == null )
            {
                configEntry = new Xpp3Dom( "configuration" );
                serverEntry.setConfiguration( configEntry );
            }
            Xpp3Dom httpHeadersEntry = configEntry.getChild( "httpHeaders" );
            if ( httpHeadersEntry == null )
            {
                httpHeadersEntry = new Xpp3Dom( "httpHeaders" );
                configEntry.addChild( httpHeadersEntry );
            }
            Xpp3Dom sessionHeaderEntry = new Xpp3Dom( "httpHeader" );
            Xpp3Dom sessionHeaderNameEntry = new Xpp3Dom( "name" );
            sessionHeaderNameEntry.setValue( HEADER_SESSION_ID );
            Xpp3Dom sessionHeaderValueEntry = new Xpp3Dom( "value" );
            sessionHeaderValueEntry.setValue( sessionId.toString() );
            sessionHeaderEntry.addChild( sessionHeaderNameEntry );
            sessionHeaderEntry.addChild( sessionHeaderValueEntry );
            httpHeadersEntry.addChild( sessionHeaderEntry );
        }

        // install or uninstall extra header
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
                        httpHeaders.put( HEADER_SESSION_ID, sessionId.toString() );
                    }
                    else
                    {
                        httpHeaders.remove( HEADER_SESSION_ID );
                    }
                    aetherSession.setConfigProperty( sessionPropertyKey, httpHeaders );
                    workDone = true;
                }
            }

            if ( !workDone )
            {
                final Map<String, String> httpHeaders = new HashMap<String, String>();
                if ( install )
                {
                    httpHeaders.put( HEADER_SESSION_ID, sessionId.toString() );
                }
                else
                {
                    httpHeaders.remove( HEADER_SESSION_ID );
                }
                aetherSession.setConfigProperty( ConfigurationProperties.HTTP_HEADERS, httpHeaders );
            }
        }
    }
}
