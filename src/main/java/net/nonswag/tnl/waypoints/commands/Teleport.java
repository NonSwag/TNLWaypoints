package net.nonswag.tnl.waypoints.commands;

import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.brigadier.PlayerSubCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.player.manager.WorldManager;
import net.nonswag.tnl.waypoints.api.Waypoint;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

class Teleport extends PlayerSubCommand {

    Teleport() {
        super("teleport", "tnl.waypoint.teleport", "tp", "goto");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source().player();
        String[] args = invocation.arguments();
        if (args.length < 2) throw new InvalidUseException(this);
        Waypoint waypoint = Waypoint.getWaypoint(player.getUniqueId(), args[1]);
        if (waypoint == null) throw new InvalidUseException(this);
        WorldManager manager = player.worldManager();
        Location location = waypoint.getLocation().clone().add(0.5, 0, 0.5);
        location.setYaw(manager.getLocation().getYaw());
        location.setPitch(manager.getLocation().getPitch());
        manager.teleport(location);
        player.messenger().sendMessage("%prefix% §aTeleported to waypoint §6" + waypoint);
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source();
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (args.length == 2) {
            for (Waypoint waypoint : Waypoint.getWaypoints(player.getUniqueId())) suggestions.add(waypoint.getName());
        }
        return suggestions;
    }

    @Override
    public void usage(@Nonnull Invocation invocation) {
        invocation.source().sendMessage("%prefix% §c/waypoint teleport §8[§6Waypoint§8]");
    }
}
