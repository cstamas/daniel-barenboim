package org.sonatype.plugins.sessiontracker;

import org.apache.maven.execution.MavenSession;

public interface SessionManager
{
    void started( MavenSession session );

    void stopped( MavenSession session );

    String getSessionId();
}
