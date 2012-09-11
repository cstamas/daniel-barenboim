package org.sonatype.plugins.daniel;

/**
 * Just a marker "role" to mark player's role. Player don't need to implement this interface at all, this is merely for
 * Plexus "role" and to be able to "look up" them. All players has to be registered as Plexus components having this
 * "role" and a distinct "hint".
 * 
 * @author cstamas
 */
public interface Player
{
}
