package robmart.mod.mineparties.api.notification;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Robmart.
 * <p>
 * This software is a modification for the game Minecraft, intended to give the game RPG party features.
 * Copyright (C) 2020 Robmart
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Notification {
    protected static final Map<UUID, Notification> notificationList = Maps.newHashMap();

    private final UUID identifier;
    private final PlayerEntity playerReceiver;
    private final MutableText message;
    private boolean hasSentMessage = false;
    private final Method method;
    private final Object instance;
    private final Object[] args;

    public Notification(PlayerEntity player, String message, Method method, Object instance, Object... args) {
        this(player, MutableText.of(new LiteralTextContent(message)), method, instance, args);
    }

    public Notification(PlayerEntity player, MutableText message, Method method, Object instance, Object... args) {
        this.playerReceiver = player;
        this.message = message;
        this.method = method;
        this.instance = instance;
        this.args = args;

        UUID uuid = null;
        while(uuid == null || uuid.toString().equals("") || notificationList.containsKey(uuid)) {
            uuid = UUID.randomUUID();
        }
        this.identifier = uuid;

        notificationList.put(this.identifier, this);
    }

    public static Map<UUID, Notification> getNotificationList() {
        return ImmutableMap.copyOf(notificationList);
    }

    public void sendMessage() {
        System.out.println(getIdentifier().toString());

        message.setStyle(message.getStyle().withBold(true).withClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/notification " + getIdentifier().toString())));

        playerReceiver.sendMessage(message, false);
        this.hasSentMessage = true;
    }

    public void execute() throws InvocationTargetException, IllegalAccessException {
        this.method.invoke(this.instance, this.args);
        notificationList.remove(this.identifier);
    }

    public UUID getIdentifier() {
        return this.identifier;
    }

    public boolean hasSentMessage() {
        return this.hasSentMessage;
    }

    public void setHasSentMessage(boolean hasSentMessage) {
        this.hasSentMessage = hasSentMessage;
    }
}
