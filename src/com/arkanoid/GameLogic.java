package com.arkanoid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameLogic extends JPanel implements KeyListener, ActionListener {

    // Initialize the game
    private boolean isPlaying = false;
    private boolean isOver = false;
    private int score = 0;
    private int totalBricks = 20;

    private Timer timer;
    private final int delay = 1;

    private int platformX = 285;
    private final int platformY = 425;
    private boolean isRight;
    private boolean isLeft;

    private int marbleX = 315;
    private int marbleY = 415;
    private int marbleVX = -1;
    private int marbleVY = -2;

    private MapGenerator mapGen;

    // Initialize the game
    public void initialize() {
        isPlaying = false;
        isOver = false;
        score = 0;
        totalBricks = 20;

        platformX = 285;

        marbleX = 315;
        marbleY = 415;
        marbleVX = -1;
        marbleVY = -2;

        mapGen = new MapGenerator(4, 5);
    }

    // Constructor
    public GameLogic() {
        mapGen = new MapGenerator(4,5);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);       // Swing Timer instead of Util Timer
        timer.start();
    }

    public void paint(Graphics graphix) {
        // Background
        graphix.setColor(new Color(64, 92,92));
        graphix.fillRect(1, 1, 635, 475);

        // Score
        graphix.setColor(Color.WHITE);
        graphix.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        graphix.drawString("SCORE: "+score, 500, 25);

        // Debug flags
        graphix.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 13));
        graphix.drawString("isRight: "+isRight, 10, 15);
        graphix.drawString("isLeft: "+isLeft, 10, 25);
        graphix.drawString("blocks: "+totalBricks, 10, 35);

        // Draw map
        mapGen.draw((Graphics2D) graphix);

        // Border
        graphix.setColor(Color.YELLOW);
        graphix.fillRect(0, 0, 2, 480);
        graphix.fillRect(0, 0, 640, 2);
        graphix.fillRect(622, 0, 2, 480);

        // Paddle
        graphix.setColor(Color.GREEN);
        graphix.fillRect(platformX, platformY, 70, 3);

        // Marble
        graphix.setColor(Color.YELLOW);
        graphix.fillOval(marbleX, marbleY, 10, 10);

        // Game Over sequence
        if(marbleY > 440) {
            isPlaying = false;
            isOver = true;
            marbleVY = 0;
            marbleVX = 0;

            graphix.setColor(Color.RED);
            graphix.setFont(new Font(Font.SERIF, Font.BOLD, 30));
            graphix.drawString("YOU DIED!", 230, 200);
            graphix.drawString("Score: "+score, 255, 230);
            graphix.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 20));
            graphix.drawString("Press [ENTER] to Retry", 180, 270);
            graphix.drawString("Press [ESC] to Exit", 200, 290);
        }

        // Win Sequence (basically the same as Game Over)
        if(totalBricks == 0) {
            isPlaying = false;
            isOver = true;
            marbleVY = 0;
            marbleVX = 0;

            graphix.setColor(Color.GREEN);
            graphix.setFont(new Font(Font.SERIF, Font.BOLD, 30));
            graphix.drawString("YOU WIN!", 230, 200);
            graphix.drawString("Score: "+score, 255, 230);
            graphix.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 20));
            graphix.drawString("Press [ENTER] to Play Again", 175, 270);
            graphix.drawString("Press [ESC] to Exit", 200, 290);
        }

        graphix.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(isPlaying) {

            // Paddle hitbox logic
            if(new Rectangle(marbleX, marbleY, 10, 10).intersects(new Rectangle(platformX, platformY, 70, 1))) {
                marbleVY *= -1;
                if(marbleVX < 0 && isRight) {
                    marbleVX *=-1;
                }
                if(marbleVX > 0 && isLeft) {
                    marbleVX *=-1;
                }
            }

            // Block hitbox logic
            A: for(int i=0;i<mapGen.map.length;i++) {
                for(int j=0;j<mapGen.map[0].length;j++) {
                    if(mapGen.map[i][j]>0) {
                        int blockX = j*mapGen.blockWidth+80;
                        int blockY = i*mapGen.blockHeight+50;
                        int blockWidth = mapGen.blockWidth;
                        int blockHeight = mapGen.blockHeight;

                        Rectangle rect = new Rectangle(blockX, blockY, blockWidth, blockHeight);
                        Rectangle marbleHitbox = new Rectangle(marbleX, marbleY, 10, 10);
                        Rectangle blockHitbox = rect;

                        if(marbleHitbox.intersects(blockHitbox)) {
                            mapGen.setBlockValue(0, i, j);
                            totalBricks--;
                            score+=5;

                            if(marbleX+9 <= blockHitbox.x || marbleX+1 >= blockHitbox.x + blockHitbox.width) {
                                marbleVX *= -1;
                            } else {
                                marbleVY *= -1;
                            }

                            break A;
                        }
                    }
                }
            }

            marbleX += marbleVX;
            marbleY += marbleVY;
            if(marbleX < 0) {
                marbleVX *= -1;
            }
            if(marbleX > 613) {
                marbleVX *= -1;
            }
            if(marbleY < 0) {
                marbleVY *= -1;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(!isOver) {
                moveRight();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(!isOver) {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if(isOver) {
                System.out.println("[ESC] pressed!");
                System.exit(0);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(isOver) {
                System.out.println("[ENTER] pressed!");
                initialize();
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isRight = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            isLeft = false;
        }
    }

    private void moveRight() {
        isPlaying = true;
        if(platformX < 550) {
            platformX += 15;
            isRight = true;
            isLeft = false;
        } else {
            platformX = 550;
            isRight = false;
        }
    }

    private void moveLeft() {
        isPlaying = true;
        if(platformX > 0) {
            platformX -= 15;
            isLeft = true;
            isRight = false;
        } else {
            platformX = 0;
            isLeft = false;
        }
    }
}
