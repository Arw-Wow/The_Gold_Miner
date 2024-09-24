package com.sxt;

import java.awt.*;

public class MineralGold extends Mineral
{
    MineralGold()
    {
        this.pos.x = (int)(Math.random() * (GameWin.window_width - 70 - 70) + 70);
        this.pos.y = (int)(Math.random() * (GameWin.window_height - 300 - 200) + 300);

        this.w = 52;
        this.h = 52;

        this.score = 10;

        this.weight_val = 5;

        this.img = Toolkit.getDefaultToolkit().getImage("img/gold1.gif");
    }
}

class MineralGoldMini extends MineralGold
{
    MineralGoldMini()
    {
        this.pos.x = (int)(Math.random() * (GameWin.window_width - 50 - 50) + 50);
        this.pos.y = (int)(Math.random() * (GameWin.window_height - 300 - 150) + 300);

        this.w = 36;
        this.h = 36;

        this.score = 5;

        this.weight_val = 3;

        this.img = Toolkit.getDefaultToolkit().getImage("img/gold0.gif");
    }
}

class MineralGoldPlus extends MineralGold
{
    MineralGoldPlus()
    {
        this.pos.x = (int)(Math.random() * (GameWin.window_width - 120 - 120) + 120);
        this.pos.y = (int)(Math.random() * (GameWin.window_height - 300 - 220) + 300);

        this.w = 105;
        this.h = 105;

        this.score = 20;

        this.weight_val = 8;

        this.img = Toolkit.getDefaultToolkit().getImage("img/gold2.gif");
    }
}