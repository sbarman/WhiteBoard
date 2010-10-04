package whiteboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import whiteboard.packet.CursorMovedPacket;
import whiteboard.packet.DrawLinePacket;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {
  private static final long serialVersionUID = 270375503236272626L;

  private BufferedImage image;
  private Graphics2D imageG2d;
  boolean drawingNotErasing = true;
  
  private WhiteBoard whiteboard;
  private WhiteBoardClient client;
  
  private Point lastPoint;
  
  public DrawingPanel(WhiteBoard wb) {
    whiteboard = wb;
    client = wb.getClient();
    
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
  }
  
  private void resetImage() {
    image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
    
    imageG2d = image.createGraphics();
    imageG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    imageG2d.setColor(Color.WHITE);
    imageG2d.fillRect(0, 0, image.getWidth(), image.getHeight());
  }
  
  public void paintComponent(Graphics g) {
    if (image == null) resetImage();
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
  }
  
  public void clearImage() {
    resetImage();
    this.repaint();
  }
  
  public void setIsDrawing() {
    drawingNotErasing = true;
  }
  
  public void setIsErasing() {
    drawingNotErasing = false;
  }
  
  private void drawAtPoint(Point p) {
//    int radius = whiteboard.getRadius();
    
//    if (whiteboard.isDrawing()) {
//      imageG2d.setColor(whiteboard.getDrawingColor());
//    } else if (whiteboard.isErasing()) {
//      imageG2d.setColor(Color.WHITE);
//    }
    
    if (lastPoint == null) {
    	client.sendCommandPacket(new DrawLinePacket(p, p, whiteboard.isErasing()));
      //imageG2d.fillOval(p.x - radius, p.y - radius, radius * 2, radius * 2);
    } else {
    	client.sendCommandPacket(new DrawLinePacket(lastPoint, p, whiteboard.isErasing()));
//      Shape line = new Line2D.Double(lastPoint, p);
//      imageG2d.setStroke(new BasicStroke(radius * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//      imageG2d.draw(line);
    }
    
    lastPoint = p;
    this.repaint();
  }
  
  public void drawPoint(int x, int y, Color c, int radius) {
	  imageG2d.setColor(c);
	  imageG2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);    
  }
  
  @Override public void mouseClicked(MouseEvent e) {}
  @Override public void mouseEntered(MouseEvent e) {}
  @Override public void mouseExited(MouseEvent e) {
    drawAtPoint(e.getPoint());
    lastPoint = null;
  }
  @Override public void mousePressed(MouseEvent e) {
    this.drawAtPoint(e.getPoint());
  }
  @Override public void mouseReleased(MouseEvent e) {
    lastPoint = null;
  }

  @Override public void mouseDragged(MouseEvent e) {
    this.drawAtPoint(e.getPoint());
  }
  
  @Override public void mouseMoved(MouseEvent e) {
	  client.sendCommandPacket(new CursorMovedPacket(e.getX(), e.getY()));
  }
}
