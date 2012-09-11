package org.sonatype.plugins.sessiontracker;

import org.apache.maven.execution.MavenSession;

/**
 * Session manager that starts ("start marking Maven outbound requests" better) and stops
 * ("stops marking Maven outbound requests" better), and also exposes currently used sessionID as String.
 * 
 * @author cstamas
 */
public interface SessionManager
{
    void started( MavenSession session );

    void stopped( MavenSession session );

    String getSessionId();
}
