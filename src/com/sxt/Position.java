package com.sxt;

public class Position
{
    public int x;
    public int y;

    Position()
    {
        this.x = 0;
        this.y = 0;
    }

    Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    boolean checkIfIn(int x, int y, int w, int h)
    {
        return (this.x > x && this.x < x + w && this.y > y && this.y < y + h);
    }
}
