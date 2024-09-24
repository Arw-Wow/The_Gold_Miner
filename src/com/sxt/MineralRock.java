package com.sxt;

import java.awt.*;

public class MineralRock extends Mineral
{
    MineralRock()
    {
        this.pos.x = (int)(Math.random() * (GameWin.window_width - 80 - 80) + 80);
        this.pos.y = (int)(Math.random() * (GameWin.window_height - 280 - 180) + 280);

        this.w = 71;
        this.h = 71;

        this.score = 2;

        this.weight_val = 10;

        this.img = Toolkit.getDefaultToolkit().getImage("img/rock1.png");
    }

}
