package com.sxt;

import java.awt.*;

public class Mineral
{
    // 左上坐标
    protected Position pos = new Position();

    // 大小
    protected int w;
    protected int h;

    // 图片
    protected Image img;

    // 重量系数
    protected double weight_val = 1.0;

    // 积分
    protected int score = 0;

    // 是否被抓取
    protected boolean is_fetched = false;

    boolean checkIfFetched()
    {
        return is_fetched;
    }

    void isFetched()
    {
        is_fetched = true;
    }

    int getScore()
    {
        return score;
    }

    double getWeightVal()
    {
        return weight_val;
    }

    void setPos(Position pos)
    {
        this.pos = pos;
    }

    void setPos(int x, int y)
    {
        this.pos.x = x;
        this.pos.y = y;
    }

    boolean checkIfOverlap(Mineral other)    // 判断是否重叠
    {
        return (this.pos.x + this.w > other.pos.x && this.pos.x < other.pos.x + other.w &&
                this.pos.y + this.h > other.pos.y && this.pos.y < other.pos.y + other.h);
    }

    void paintSelf(Graphics g)
    {
        g.drawImage(img, pos.x, pos.y, null);
    }
}
