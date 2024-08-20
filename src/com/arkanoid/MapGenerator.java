package com.arkanoid;

import java.awt.*;

public class MapGenerator {
    public int map[][];
    public int blockHeight;
    public int blockWidth;

    // Constructor
    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for(int i=0;i< map.length;i++) {
            for(int j=0;j<map[0].length;j++) {
                map[i][j] = 1;
            }
        }
        blockWidth = 480/col;
        blockHeight = 100/row;
    }
    public void draw(Graphics2D g2d) {
        for(int i=0;i< map.length;i++) {
            for(int j=0;j<map[0].length;j++) {
                if(map[i][j] == 1) {
                    g2d.setColor(Color.white);
                    g2d.fillRect(j*blockWidth+80, i*blockHeight+50, blockWidth, blockHeight);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.setColor(new Color(64, 92,92));
                    g2d.drawRect(j*blockWidth+80, i*blockHeight+50, blockWidth, blockHeight);
                }
            }
        }
    }

    public void setBlockValue(int value, int row, int column) {
        map[row][column] = value;
    }
}
