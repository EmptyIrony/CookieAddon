package com.domcer.cookieaddon;

import me.nikl.gamebox.GameBox;
import me.nikl.gamebox.games.cookieclicker.CCGame;
import me.nikl.gamebox.games.cookieclicker.CCGameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import strafe.games.core.util.CC;
import strafe.games.core.util.board.assemble.AssembleAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/11 14:50
 * 4
 */
public class ScoreImpl implements AssembleAdapter {
    private static final SimpleDateFormat dateFormat;

    public String getTitle(final Player player) {
        return "&7磐&8石 &f&k!&r &e街机游戏";
    }

    public List<String> getLines(final Player player) {
        final Map<UUID, CCGame> games = ((CCGameManager) GameBox.ins.getPluginManager().getGameManager("cookieclicker")).getGames();
        final List<String> lines = new ArrayList<>();
        lines.add(CC.GRAY + ScoreImpl.dateFormat.format(System.currentTimeMillis()));
        lines.add("");
        if (CookieAddon.siting.contains(player.getUniqueId())) {
            lines.add("&f输入/gb重新开启游戏");
        }
        else {
            lines.add("&f右键一个台阶坐下吧");
        }
        lines.add("");
        lines.add("&f在线： &a" + Bukkit.getOnlinePlayers().size());
        lines.add("");
        if (games.get(player.getUniqueId()) != null) {
            final CCGame ccGame = games.get(player.getUniqueId());
            lines.add("&f饼干总数： &a" + Format.formatNum(String.valueOf((int)ccGame.getTotalCookiesProduced()), false));
            lines.add("&f当前持有饼干： &a" + Format.formatNum(String.valueOf((int)ccGame.getCookies()), false));
            lines.add("");
        }
        lines.add("&f服务器： &a#街机游戏");
        lines.add("");
        lines.add("&7&opanshimc.cn");
        return lines;
    }

    static {
        dateFormat = new SimpleDateFormat("#yyyy/MM/dd");
    }
}
