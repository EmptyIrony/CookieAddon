package com.emptyirony.cookieaddon;

import org.bukkit.block.BlockFace;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/18 16:06
 * 4
 */
public class Util {
    public static int faceToYaw(BlockFace face){
        switch (face){
            case SOUTH:
                return 0;
            case EAST:
                return 90;
            case NORTH:
                return 180;
            case WEST:
                return 270;
        }
        return 1000;
    }
}
