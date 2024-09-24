package com.sxt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

// JFrame 创建窗口，监听鼠标键盘事件
public class GameWin extends JFrame
{
    static final int FPS = 144;        // 帧数
    static final int window_width = 768;        // 窗口宽度
    static final int window_height = 1000;        // 窗口高度

    BackGround back_ground = new BackGround();
    Line line = new Line();
    Boom boom = new Boom();
    ArrayList<Mineral> mineral_list = new ArrayList<>();    // 矿物列表

    private int total_score = 0;
    private int demand_score = 30;          // 需求分数（每关上涨）
    private int total_mineral_num = (int)(Math.random() * (16 - 10) + 10);     // 场景中矿物的数量
    private int boom_num = 3;               // 炸弹数量

    private int level = 1;
//    boolean is_current_level_win = false;

    private long game_start_time;       // 游戏开始时间，以秒为单位
    private long game_left_time = 20;   // 剩余时间，以秒为单位
    private long game_total_time = 25;  // 游戏总共的时间，每关增加25秒

    void launch()
    {
        this.setVisible(true);  // 窗口可见
        this.setSize(window_width, window_height); // 窗口大小
        this.setLocationRelativeTo(null);   // 窗口居中
        this.setTitle("黄金矿工");

        setDefaultCloseOperation(EXIT_ON_CLOSE);    // 点击右上的X执行exit退出程序

        this.input();

        // 加载
        this.reload();

        boolean is_running = true;
        long begin_time = System.currentTimeMillis();   // 获取1970.1.1以来的毫秒数（即时间戳）
        game_start_time = begin_time / 1000;
        long end_time;
        while (is_running)
        {
            // update
            this.update();

            // paint
            repaint();

            // FPS
            end_time = System.currentTimeMillis();
            if (end_time - begin_time < 1000 / FPS)
            {
                try { Thread.sleep(1000 / FPS - (end_time - begin_time)); }
                catch (InterruptedException e) { System.out.println("error"); }
            }
            begin_time = end_time;
        }
    }

    void input()
    {
        addMouseListener(new MouseAdapter()     // 匿名内部类的使用
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                // 1左键，2滚轮，3右键
                if (e.getButton() == 1 && line.getState().equals(Line.LineState.SWING)) // 摆动状态时才能出钩
                {
                    line.setState(Line.LineState.FETCH);
                }
                else if (e.getButton() == 3 && line.is_fetching_mineral)  // 正在抓矿物才能使用炸弹
                {
                    useBoom();
                    line.resetRetractSpeed();
                    line.is_fetching_mineral = false;
                }
            }
        });
    }

    void update()
    {
        // 更新绳索
        line.updateSelf();

        switch (line.getState())
        {
            case SWING:
                Iterator<Mineral> iterator = mineral_list.iterator();
                while (iterator.hasNext())
                {
                    Mineral mineral = iterator.next();
                    if (mineral.checkIfFetched())
                    {
                        total_score += mineral.getScore();
                        iterator.remove();
                        line.is_fetching_mineral = false;
                    }
                }
                break;

            case FETCH:
                // 判断钩索是否碰到矿物
                for (Mineral mineral : mineral_list)
                {
                    if (line.getPos2().checkIfIn(mineral.pos.x, mineral.pos.y, mineral.w, mineral.h))
                    {
                        line.getWeightVal(mineral.getWeightVal());
                        line.setState(Line.LineState.RETRACT);
                        mineral.isFetched();
                        line.is_fetching_mineral = true;
                        break;  // break以防止碰撞到多个矿物全部抓取（虽然不可能，但这样更安全）
                    }
                }
                break;

            case RETRACT:
                // 更新矿物的位置
                for (Mineral mineral : mineral_list)
                {
                    if (mineral.checkIfFetched())
                    {
                        mineral.setPos(line.getPos2().x - mineral.w / 2, line.getPos2().y); // 让钩子在矿物头上的中间
                        break;
                    }
                }
                break;
        }

        if (total_score >= demand_score)
        {
            this.nextLevel();
        }

        game_left_time = game_total_time - (System.currentTimeMillis() / 1000 - game_start_time);
        if (game_left_time <= 0)
        {
            LoseTheGame();
        }
    }

    @Override
    public void paint(Graphics g)   // 尽管main中没有直接调用paint，但是launch中的setVisible会自动调用paint
    {
        Image offScreenImage;       // 作为双缓冲窗口的备用缓冲

        offScreenImage = this.createImage(window_width, window_height);
        Graphics gImage = offScreenImage.getGraphics();     // 创建一个在缓冲窗口的画笔

        // 在缓冲窗口绘制
        back_ground.paintSelf(gImage);
        line.paintSelf(gImage);
        for (Mineral mineral : mineral_list)
        {
            mineral.paintSelf(gImage);
        }

        // 三段式打印字符串
        gImage.setColor(Color.BLACK);
        gImage.setFont(new Font("宋体", Font.BOLD, 30));  // 设置为宋体，加粗，大小30
        gImage.drawString("积分：" + total_score, 30, 175);

        gImage.drawString("level：" + level, 30, 75);
        gImage.drawString("剩余时间：" + game_left_time, 30, 125);

        gImage.drawImage(boom.img_boom, 465, 115, null);
        gImage.drawString(" * " + boom_num, 530, 170);

        // 在主窗口绘制缓冲窗口
        g.drawImage(offScreenImage, 0, 0, null);
    }

    void generateMineral()
    {
        int mineral_num = 0;
        Mineral mineral;
        while (mineral_num < total_mineral_num)
        {
            double random_type = Math.random();
            // 0 ~ 0.3 Gold  0.3 ~ 0.5 GoldMini  0.5 ~ 0.7 GoldPlus  0.7 ~ 1.0 Rock
            if (random_type < 0.3)          mineral = new MineralGold();
            else if (random_type < 0.5)     mineral = new MineralGoldMini();
            else if (random_type < 0.7)     mineral = new MineralGoldPlus();
            else                            mineral = new MineralRock();

            // 判断创建的这个矿物是否和已有矿物重合
            boolean can_generate_mineral = true;
            for (Mineral other : mineral_list)
            {
                if (mineral.checkIfOverlap(other))
                {
                    can_generate_mineral = false;
                    break;
                }
            }
            // 没有重合，就加入列表
            if (can_generate_mineral)
            {
                mineral_list.add(mineral);
                mineral_num++;
            }
        }

    }

    void useBoom()
    {
        if (boom_num <= 0)  return;

        mineral_list.removeIf(Mineral::checkIfFetched); // 应用了方法引用

        boom_num--;
    }

    void nextLevel()
    {
        level++;
        if (level > 15)
        {
            WinTheGame();
        }
        demand_score += 30;
        game_total_time += 25;

        reload();
    }

    void reload()
    {
        mineral_list.clear();
        boom_num = 3;
        total_mineral_num = (int)(Math.random() * (14 - 8) + 8);
        generateMineral();

        line.reload();
    }

    void LoseTheGame()
    {
        this.paint(getGraphics());  // 将结束的状态再绘制一帧
        Graphics g = getGraphics();
        g.setColor(Color.blue);
        g.setFont(new Font("华文琥珀", Font.BOLD, 60));
        g.drawString("You Lose The Game!", getWidth() / 2 - 270, getHeight() / 2);

        try { Thread.sleep(10000); }
        catch (InterruptedException e) { throw new RuntimeException(e); }

        System.exit(0);
    }

    void WinTheGame()
    {
        this.paint(getGraphics());  // 将结束的状态再绘制一帧;
        Graphics g = getGraphics();
        g.setColor(Color.blue);
        g.setFont(new Font("华文琥珀", Font.BOLD, 60));
        g.drawString("You Win The Game!", getWidth() / 2 - 260, getHeight() / 2);

        try { Thread.sleep(10000); }
        catch (InterruptedException e) { throw new RuntimeException(e); }

        System.exit(0);
    }

    public static void main(String[] args)
    {
        GameWin game_win = new GameWin();
        game_win.launch();
    }
}
