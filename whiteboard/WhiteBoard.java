package whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class WhiteBoard {
  private JFrame frame; 
  private JButton colorPickerButton;
  private JSlider brushSizeSlider;
  private JRadioButton drawButton;
  private JRadioButton eraseButton;
  private DrawingPanel drawingPanel;
  
  private final Dimension drawingPanelSize = new Dimension(640, 640);
  
  private int radius = 3;
  public int getRadius() {
    return radius;
  }
  
  private boolean isDrawing = true;
  public boolean isDrawing() { 
    return isDrawing;
  }
  
  private boolean isErasing = false;
  public boolean isErasing() {
    return isErasing;
  }

  private Color drawingColor = Color.BLACK;
  public Color getDrawingColor() {
    return drawingColor;
  }
 
  public void run() {
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
    frame.getContentPane().add(controlPanel, BorderLayout.NORTH);
    
    drawingPanel = new DrawingPanel(this);
    drawingPanel.setPreferredSize(drawingPanelSize);
    frame.getContentPane().add(drawingPanel, BorderLayout.CENTER);
    
    controlPanel.add(Box.createHorizontalGlue());
    
    drawButton = new JRadioButton("Draw");
    drawButton.setSelected(true);
    drawButton.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
        isDrawing = true;
        isErasing = false;
      }
    });
    controlPanel.add(drawButton);
    
    eraseButton = new JRadioButton("Erase");
    eraseButton.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
        isDrawing = false;
        isErasing = true;
      }
    });
    controlPanel.add(eraseButton);
    
    ButtonGroup drawingRadioGroup = new ButtonGroup();
    drawingRadioGroup.add(drawButton);
    drawingRadioGroup.add(eraseButton);
    
    colorPickerButton = new JButton(new Icon() {
      @Override public int getIconHeight() { return (int) (colorPickerButton.getHeight() * 0.8); }
      @Override public int getIconWidth() { return (int) (colorPickerButton.getWidth() * 0.8); }
      @Override public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(drawingColor);
        g.fillRect(x, y, this.getIconWidth(), this.getIconHeight());
      }
    });
    colorPickerButton.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
        drawingColor = JColorChooser.showDialog(frame,
                                                "Pick a color",
                                                drawingColor);
      }
    });
    colorPickerButton.putClientProperty("JButton.buttonType", "square");
    colorPickerButton.setSize(new Dimension(24, 12));
    colorPickerButton.setFocusPainted(false);
    controlPanel.add(colorPickerButton);
    
    brushSizeSlider = new JSlider(JSlider.HORIZONTAL, 3, 30, 3);
    brushSizeSlider.setMaximumSize(new Dimension(128, 48));
    brushSizeSlider.addChangeListener(new ChangeListener() {
      @Override public void stateChanged(ChangeEvent e) {
        radius = brushSizeSlider.getValue();
      }
    });
    controlPanel.add(brushSizeSlider);
    
    JButton clearButton = new JButton("Clear");
    clearButton.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
        drawingPanel.clearImage();
      }
    });
    controlPanel.add(clearButton);
    
    JButton historyButton = new JButton("Replay");
    controlPanel.add(historyButton);
    
    controlPanel.add(Box.createHorizontalGlue());
    
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
  }
  
  public static void main(String[] args) {
    WhiteBoard wb = new WhiteBoard();
    wb.run();
  }
}
