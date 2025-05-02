package main.java.com.mapanarrativo.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private Image image;
    private boolean fitToPanel;

    public ImagePanel() {
        this.fitToPanel = true;
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setFitToPanel(boolean fitToPanel) {
        this.fitToPanel = fitToPanel;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            if (fitToPanel) {
                // Scale the image to fit the panel
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Center the image at original size
                int x = (getWidth() - image.getWidth(this)) / 2;
                int y = (getHeight() - image.getHeight(this)) / 2;
                if (x < 0) x = 0;
                if (y < 0) y = 0;
                g.drawImage(image, x, y, this);
            }
        }
    }
}