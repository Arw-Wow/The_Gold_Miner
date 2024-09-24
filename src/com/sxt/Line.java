package com.sxt;

import java.awt.*;

public class Line
{
    enum LineState
    {
        SWING, FETCH, RETRACT,
    }

    private final Image img_hook;
    private static final double min_length = 80.0;                  // 摆线最小长度
//    private static final double max_length = 500.0;               // 摆线最大长度
    private static final double swing_speed = 0.0015;               // 摆动速度参数
    private static final double fetch_speed = 1.5;                  // 伸长速度参数
    private static double retract_speed = 2.0;                      // 收回速度参数

    private final Position pos1 = new Position(380, 180);       // 固定位置
    private final Position pos2 = new Position();                    // 伸出位置
    private Position pos_hook = new Position();                      // 钩子位置

    private double length = min_length;                             // 长度
    private double angle = 0.1;                                     // 摆动角度
    private int direction = 1;                                      // 摆动方向，1为左，-1为右

    private LineState state = LineState.SWING;
    boolean is_fetching_mineral = false;

    Line()
    {
        this.img_hook = Toolkit.getDefaultToolkit().getImage("img/hook.png");
    }

    void reload()
    {
        retract_speed = 2.0;
        state = LineState.SWING;
        angle = 0.1;
        direction = 1;
        length = min_length;
    }

    LineState getState()
    {
        return this.state;
    }

    void setState(LineState state)
    {
        this.state = state;
    }

    Position getPos2()
    {
        return pos2;
    }

    void getWeightVal(double weight_val)    // 传入重量系数，speed会乘上该系数。
    {
        retract_speed /= weight_val;
    }

    void resetRetractSpeed()
    {
        retract_speed = 2.0;
    }

    void updateSelf()
    {
        switch (this.state)
        {
            case SWING:
                // 调整摆动方向
                if (angle > 0.9)    direction = -1;
                if (angle < 0.1)    direction =  1;
                // 更新摆角
                angle += swing_speed * direction;
                break;

            case FETCH:
                // 伸长摆线
                if (!this.checkIfOutOfWindow())
                {
                    length += fetch_speed;
                }
                else
                {
                    state = LineState.RETRACT;
                }
                break;

            case RETRACT:
                // 收回摆线
                if (length >= min_length)
                {
                    length -= retract_speed;
                }
                else
                {
                    length = min_length;
                    state = LineState.SWING;
                    resetRetractSpeed();
                }
                break;
        }
        pos2.x = pos1.x + (int)(length * Math.cos(angle * Math.PI));
        pos2.y = pos1.y + (int)(length * Math.sin(angle * Math.PI));
        pos_hook.x = pos2.x - img_hook.getWidth(null) / 2;
        pos_hook.y = pos2.y - 5;
    }

    void paintSelf(Graphics g)
    {
        g.setColor(Color.red);
        g.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
        g.drawLine(pos1.x - 1, pos1.y, pos2.x - 1, pos2.y);
        g.drawLine(pos1.x + 1, pos1.y, pos2.x + 1, pos2.y);     // 加两条平行线，实现加粗
        g.drawImage(img_hook, pos_hook.x, pos_hook.y, null);
    }

    boolean checkIfOutOfWindow()
    {
        return (pos2.x + 20 > GameWin.window_width || pos2.x - 20 < 0 || pos2.y + 100 > GameWin.window_height || pos2.y - 20 < 0);
    }
}
