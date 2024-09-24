package com.sxt;

import java.awt.*;

public class BackGround
{
    private static final Image img_bg = Toolkit.getDefaultToolkit().getImage("img/bg.jpg");
    private static final Image img_bg1 = Toolkit.getDefaultToolkit().getImage("img/bg1.jpg");
    private static final Image img_peo = Toolkit.getDefaultToolkit().getImage("img/peo.png");

    void paintSelf(Graphics g)  // Graphics是画笔类
    {
        g.drawImage(img_bg, 0, 200, null);
        g.drawImage(img_bg1, 0, 0, null);
        g.drawImage(img_peo, 310, 50, null);
    }
}
