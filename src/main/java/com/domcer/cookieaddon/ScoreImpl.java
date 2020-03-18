package com.domcer.cookieaddon;

import me.nikl.gamebox.GameBox;
import me.nikl.gamebox.games.cookieclicker.CCGame;
import me.nikl.gamebox.games.cookieclicker.CCGameManager;
import org.bukkit.entity.Player;
import strafe.games.core.util.CC;
import strafe.games.core.util.board.assemble.AssembleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/11 14:50
 * 4
 */
public class ScoreImpl implements AssembleAdapter {
    private static final SimpleDateFormat dateFormat;

    public String getTitle(Player player) {
        return "&7&l磐&f&l石 &f&k!&r &e街机游戏";
    }

    public List<String> getLines(final Player player) {
        Map<UUID, CCGame> games = ((CCGameManager) GameBox.ins.getPluginManager().getGameManager("cookieclicker")).getGames();
        List<String> lines = new ArrayList<>();
        lines.add(CC.GRAY + ScoreImpl.dateFormat.format(System.currentTimeMillis()));
        lines.add("");
        lines.add("&e欢迎来到&a磐石食堂!");

        lines.add("");
        if (CookieAddon.siting.contains(player.getUniqueId())) {
            lines.add("&c输入 &f/gb &c重新开启游戏");
        }
        else {
            lines.add("&e快右键任意座位.");
            lines.add("&e准备恰饭吧!");
        }
        lines.add("");
        if (games.get(player.getUniqueId()) != null) {
            CCGame ccGame = games.get(player.getUniqueId());
            lines.add("&f饼干总数： &e" + Format.formatNum(String.valueOf((int)ccGame.getTotalCookiesProduced()), false));
            lines.add("&f当前持有饼干： &e" + Format.formatNum(String.valueOf((int)ccGame.getCookies()), false));
            lines.add("");
        }
        lines.add("&7&opanshimc.cn");
        return lines;
    }

    static {
        dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");
    }
}
