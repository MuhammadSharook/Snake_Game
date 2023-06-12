import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener
{
    int Board_Height = 500, Board_Width = 500;
    int MAX_DOTS = 2500;
    int DOT_SIZE = 10;
    int DOTS;
    int []x = new int[MAX_DOTS];
    int []y = new int[MAX_DOTS];

    int apple_x,apple_y;

    //Images
    Image body,head,apple;

    Timer timer;
    int DELAY = 150;

    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;

    boolean inGame = true;
    Board()
    {
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(Board_Width,Board_Height));
        setBackground(Color.WHITE);
        initializeGame();
        loadImages();
    }

    //Initializing game
    public void initializeGame()
    {
        DOTS = 3;
        x[0] = 250;
        y[0] = 250;
        //initializing snake position
        for(int i = 1; i < DOTS; i++)
        {
            x[i] = x[0] + DOT_SIZE * i;
            y[i] = y[0];
        }

        //initializing apple's position
//        apple_x = 150;
//        apple_y = 150;

        //locate apple's random position by calling fun
        locateApple();

        //initialize timer
        timer = new Timer(DELAY,this);
        timer.start();
    }

    //Load images from resources folder to Image Object
    public void loadImages()
    {
        ImageIcon bodyIcon = new ImageIcon("src/resources/body.png");
        body = bodyIcon.getImage();

        ImageIcon headIcon = new ImageIcon("src/resources/black head.png");
        head = headIcon.getImage();

        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }

    //add images at snakes's and apple's position
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }

    //draw Image
    public void doDrawing(Graphics g)
    {
        if(inGame)
        {
            g.drawImage(apple,apple_x,apple_y,this);
            for(int i = 0; i < DOTS; i++)
            {
                if(i == 0)
                {
                    g.drawImage(head,x[0],y[0],this);
                }
                else
                {
                    g.drawImage(body,x[i],y[i],this);
                }
            }
        }
        else
        {
            gameOver(g);
            timer.stop();
        }

    }

    //Randomize apple's position
    public void locateApple()
    {
        apple_x = ((int)(Math.random()*39)) * DOT_SIZE;
        apple_y = ((int)(Math.random()*39)) * DOT_SIZE;
    }

    //check collision with boarder and body
    public void checkCollision()
    {
        //collision with body
        for(int i = 1; i < DOTS; i++)
        {
            if((i > 4) && (x[0] == x[i]) && (y[0] == y[i]))
            {
                inGame = false;
            }
        }
        //collision with boarder
        if(x[0] < 0)
        {
            inGame = false;
        }
        if(x[0] >= Board_Width)
        {
            inGame = false;
        }
        if(y[0] < 0)
        {
            inGame = false;
        }
        if(y[0] >= Board_Height)
        {
            inGame = false;
        }
    }
    //display GameOver Msg
    public void gameOver(Graphics g)
    {
        String msg = "Game Over";
        int score = (DOTS - 3);
        String scoremsg = "Your Score : " + Integer.toString(score);
        Font format = new Font("Lucida Bright",Font.BOLD,15);
        FontMetrics fontMetrics = getFontMetrics(format);

        g.setColor(Color.RED);
        g.setFont(format);
        g.drawString(msg,((Board_Width - fontMetrics.stringWidth(msg))/2),(Board_Height)/4);
        g.drawString(scoremsg,((Board_Width - fontMetrics.stringWidth(scoremsg))/2),3*(Board_Height/4));
    }
    //override Action event
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(inGame)
        {
            checkApple();
            move();
            checkCollision();
        }
        repaint();
    }
    //Snake move
    public void move()
    {
        for(int z = DOTS-1; z > 0; z--)
        {
            x[z] = x[z-1];
            y[z] = y[z-1];
        }
        if(leftDirection)
        {
            x[0] -= DOT_SIZE;
        }
        if(rightDirection)
        {
            x[0] += DOT_SIZE;
        }
        if(upDirection)
        {
            y[0] -= DOT_SIZE;
        }
        if(downDirection)
        {
            y[0] += DOT_SIZE;
        }
    }

    //make snake eat food
    public void checkApple()
    {
        if(apple_x == x[0] && apple_y == y[0])
        {
            DOTS++;
            locateApple();
        }
    }
    //Implements control
    private class TAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent keyEvent)
        {
            int key = keyEvent.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !rightDirection)
            {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if(key == KeyEvent.VK_RIGHT&& !leftDirection)
            {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if(key == KeyEvent.VK_UP && !downDirection)
            {
                leftDirection = false;
                upDirection = true;
                rightDirection = false;
            }

            if(key == KeyEvent.VK_DOWN && !upDirection)
            {
                leftDirection = false;
                rightDirection = false;
                downDirection = true;
            }
        }
    }
}