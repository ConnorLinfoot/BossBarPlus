package com.connorlinfoot.bossbarplus.BossBar;

import com.connorlinfoot.bossbarplus.Util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BossBar1_9 extends BossBar {
	private String text = "";
	private BarColor color = BarColor.PINK;
	private BarStyle style = BarStyle.SOLID;
	private float progress = 1;
	private Object bossBattleServer;
	private Object[] colorEnums;
	private Object[] styleEnums;
	private Object[] actionEnums;
	private Constructor packetConstructor;

	public BossBar1_9(String text) {
		if (text != null)
			this.text = text;
		setupBar();
	}

	public BossBar1_9(String text, BarColor color, BarStyle style) {
		if (text != null)
			this.text = text;
		if (color != null)
			this.color = color;
		if (style != null)
			this.style = style;
		setupBar();
	}

	public BossBar1_9(String text, BarColor color, BarStyle style, float progress) {
		if (text != null)
			this.text = text;
		if (color != null)
			this.color = color;
		if (style != null)
			this.style = style;
		if (progress > 1 || progress < 0)
			this.progress = 1;
		setupBar();
	}

	protected void setupBar() {
		try {
			this.colorEnums = ReflectionUtil.getNMSClass("BossBattle$BarColor").getEnumConstants();
			this.styleEnums = ReflectionUtil.getNMSClass("BossBattle$BarStyle").getEnumConstants();
			this.actionEnums = ReflectionUtil.getNMSClass("PacketPlayOutBoss$Action").getEnumConstants();
			Object colorEnum = this.colorEnums[this.color.getId()];
			Object styleEnum = this.styleEnums[this.style.getId()];
			Constructor bossBattleServerConstructor = ReflectionUtil.getNMSClass("BossBattleServer").getConstructor(ReflectionUtil.getNMSClass("IChatBaseComponent"), ReflectionUtil.getNMSClass("BossBattle$BarColor"), ReflectionUtil.getNMSClass("BossBattle$BarStyle"));
			Object IChatBaseComponent = ReflectionUtil.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{\"text\":\"" + text + "\"}"});
			this.bossBattleServer = bossBattleServerConstructor.newInstance(IChatBaseComponent, colorEnum, styleEnum);
			this.packetConstructor = ReflectionUtil.getNMSClass("PacketPlayOutBoss").getConstructor(ReflectionUtil.getNMSClass("PacketPlayOutBoss$Action"), ReflectionUtil.getNMSClass("BossBattle"));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
	}

	public void addPlayer(Player player) {
		super.addPlayer(player);
		try {
			sendPacket(player, packetConstructor.newInstance(actionEnums[BarAction.ADD.getId()], bossBattleServer));
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void removePlayer(Player player) {
		super.removePlayer(player);
		try {
			sendPacket(player, packetConstructor.newInstance(actionEnums[BarAction.REMOVE.getId()], bossBattleServer));
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void sendPacketToList(Object packet) {
		for (String name : players) {
			Player player = Bukkit.getPlayerExact(name);
			if (player == null)
				continue;
			sendPacket(player, packet);
		}
	}

	private void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", ReflectionUtil.getNMSClass("Packet")).invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTitle(String text) {
		this.text = text;
		try {
			Field field = this.bossBattleServer.getClass().getSuperclass().getField("title");
			Object IChatBaseComponent = ReflectionUtil.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{\"text\":\"" + text + "\"}"});
			field.set(this.bossBattleServer, IChatBaseComponent);
			sendPacketToList(this.packetConstructor.newInstance(this.actionEnums[BarAction.UPDATE_TITLE.getId()], this.bossBattleServer));
		} catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchFieldException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public void setProgress(float progress) {
		this.progress = progress;
		try {
			Method method = this.bossBattleServer.getClass().getMethod("setProgress", float.class);
			method.invoke(this.bossBattleServer, this.progress);
			sendPacketToList(packetConstructor.newInstance(this.actionEnums[BarAction.UPDATE_HEALTH.getId()], this.bossBattleServer));
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
	}

	public void setColor(BarColor color) {
		this.color = color;
		try {
			Field field = this.bossBattleServer.getClass().getSuperclass().getField("color");
			field.set(this.bossBattleServer, this.colorEnums[this.color.getId()]);
			sendPacketToList(this.packetConstructor.newInstance(this.actionEnums[BarAction.UPDATE_STYLE.getId()], this.bossBattleServer));
		} catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public void setStyle(BarStyle style) {
		this.style = style;
		try {
			Field field = this.bossBattleServer.getClass().getSuperclass().getField("style");
			field.set(this.bossBattleServer, this.styleEnums[this.style.getId()]);
			sendPacketToList(this.packetConstructor.newInstance(this.actionEnums[BarAction.UPDATE_STYLE.getId()], this.bossBattleServer));
		} catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public void clearBar() {
		for (String string : players) {
			Player player = Bukkit.getPlayerExact(string);
			if (player == null)
				continue;
			try {
				sendPacket(player, packetConstructor.newInstance(actionEnums[BarAction.REMOVE.getId()], bossBattleServer));
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
