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

public class WhiteBoard {
  private JFrame frame;
  private Color drawingColor = Color.BLACK; 
  private JButton colorPickerButton;
  
  public void run() {
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(640, 640);
    frame.setResizable(false);
    
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
    frame.getContentPane().add(controlPanel, BorderLayout.NORTH);
    controlPanel.add(Box.createHorizontalGlue());
    
    JRadioButton drawButton = new JRadioButton("Draw");
    drawButton.setSelected(true);
    controlPanel.add(drawButton);
    
    JRadioButton eraseButton = new JRadioButton("Erase");
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
    
    JSlider brushSizeSlider = new JSlider(JSlider.HORIZONTAL, 3, 30, 3);
    brushSizeSlider.setMaximumSize(new Dimension(128, 48));
    controlPanel.add(brushSizeSlider);
    
    JButton clearButton = new JButton("Clear");
    controlPanel.add(clearButton);
    
    JButton historyButton = new JButton("Replay");
    controlPanel.add(historyButton);
    
    controlPanel.add(Box.createHorizontalGlue());
    
    DrawingPanel drawingPanel = new DrawingPanel();
    frame.getContentPane().add(drawingPanel, BorderLayout.CENTER);
    
    frame.setVisible(true);
  }
  
  public static void main(String[] args) {
    WhiteBoard wb = new WhiteBoard();
    wb.run();
  }
}
