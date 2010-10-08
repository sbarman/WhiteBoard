package whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import whiteboard.packet.ChangeBrushColorPacket;
import whiteboard.packet.ChangeBrushSizePacket;
import whiteboard.packet.ClearScreenPacket;
import whiteboard.packet.DrawImagePacket;
import whiteboard.packet.TextMessagePacket;

public class WhiteBoard {
  public static final int DEFAULT_RADIUS = 3;
  public static final Color DEFAULT_COLOR = Color.BLACK;
	
  public static final int MIN_RADIUS = 1;
  public static final int MAX_RADIUS = 50;
  
  private JFrame frame; 
  private JButton colorPickerButton;
  private JSlider brushSizeSlider;
  private JRadioButton drawButton;
  private JRadioButton eraseButton;
  private DrawingPanel drawingPanel;
  
  public DrawingPanel getDrawingPanel() {
    return drawingPanel;
  }

  private JTextField messageField;
  private JTextArea messageLog;
  
  private final Dimension drawingPanelSize = new Dimension(640, 480);
  
  private int radius = DEFAULT_RADIUS;
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

  private Color drawingColor = DEFAULT_COLOR;  
  public Color getDrawingColor() {
    return drawingColor;
  }
  
  private WhiteBoardClient client;
  private JScrollPane messageLogScrollPane;
  public WhiteBoardClient getClient() {
	  return client;
  }
  
  public WhiteBoard() {
	  this.client = new WhiteBoardClient();
  }
 
  public void run() {
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    drawingPanel = new DrawingPanel(this);
    drawingPanel.setPreferredSize(drawingPanelSize);
    frame.getContentPane().add(drawingPanel, BorderLayout.CENTER);
    
    JPanel textPanel = new JPanel();
    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
    frame.getContentPane().add(textPanel, BorderLayout.SOUTH);
    
    // textPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
    
    messageLog = new JTextArea();
    messageLog.setRows(6);
    messageLog.setEditable(false);
    messageLogScrollPane = new JScrollPane(messageLog);
    textPanel.add(messageLogScrollPane);
    
    messageField = new JTextField();
    messageField.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
        String text = messageField.getText();
        client.sendCommandPacket(new TextMessagePacket(text));
        messageField.setText("");
      }
    });
    textPanel.add(messageField);
    
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
    frame.getContentPane().add(controlPanel, BorderLayout.NORTH);
    
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
        drawingColor = JColorChooser.showDialog(frame, "Pick a color", drawingColor);    
        client.sendCommandPacket(new ChangeBrushColorPacket(drawingColor));
      }
    });
    colorPickerButton.putClientProperty("JButton.buttonType", "square");
    colorPickerButton.setSize(new Dimension(24, 12));
    colorPickerButton.setFocusPainted(false);
    controlPanel.add(colorPickerButton);
    
    brushSizeSlider = new JSlider(JSlider.HORIZONTAL, MIN_RADIUS, MAX_RADIUS, DEFAULT_RADIUS);
    brushSizeSlider.setMaximumSize(new Dimension(128, 48));
    brushSizeSlider.addChangeListener(new ChangeListener() {
      @Override public void stateChanged(ChangeEvent e) {
      	radius = brushSizeSlider.getValue();
    	client.sendCommandPacket(new ChangeBrushSizePacket(radius));
      }
    });
    controlPanel.add(brushSizeSlider);
    
    JButton screenshotButton = new JButton("Screenshot");
    screenshotButton.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
        try {
          frame.setVisible(false);
          Image screenshot = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
          double panelRatio = drawingPanel.getWidth() / drawingPanel.getHeight();
          double screenshotRatio = ((double) screenshot.getWidth(null)) / screenshot.getHeight(null);
          if (panelRatio > screenshotRatio) {
            screenshot = screenshot.getScaledInstance((int) (screenshotRatio * drawingPanel.getHeight()), (int) drawingPanel.getHeight(), Image.SCALE_SMOOTH);
            client.sendCommandPacket(new DrawImagePacket(screenshot, new Point((drawingPanel.getWidth() - screenshot.getWidth(null)) / 2, 0)));
          } else {
            screenshot = screenshot.getScaledInstance((int) drawingPanel.getWidth(), (int) (drawingPanel.getWidth() / screenshotRatio), Image.SCALE_SMOOTH);
            client.sendCommandPacket(new DrawImagePacket(screenshot, new Point(0, (drawingPanel.getHeight() - screenshot.getHeight(null)) / 2)));
          }
        } catch (Exception e1) {
          JOptionPane.showMessageDialog(null, "Screenshot failed.");
        } finally {
          frame.setVisible(true);
        }
      }
    });
    controlPanel.add(screenshotButton);
    
    JButton clearButton = new JButton("Clear");
    clearButton.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
    	client.sendCommandPacket(new ClearScreenPacket());
      //  drawingPanel.clearImage();
      }
    });
    controlPanel.add(clearButton);
    
    JButton historyButton = new JButton("Replay");
    historyButton.addActionListener(new ActionListener() {
        @Override public void actionPerformed(ActionEvent e) {
        	JFrame replayFrame = new JFrame();
            
            DrawingPanel replayDrawingPanel = new DrawingPanel(null);
            replayDrawingPanel.setPreferredSize(drawingPanelSize);
            replayFrame.getContentPane().add(replayDrawingPanel, BorderLayout.CENTER);
        }
      });
    controlPanel.add(historyButton);
    
    controlPanel.add(Box.createHorizontalGlue());
    
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
    client.initListener(this);
  }
  
  public void addMessage(String line) {
    if (messageLog.getText().isEmpty()) {
      messageLog.append(line);
    } else {
      messageLog.append("\n" + line);
    }
    // Scroll to bottom
    messageLog.setCaretPosition(messageLog.getText().length());
  }
  
  public static void main(String[] args) {
    WhiteBoard wb = new WhiteBoard();
    wb.run();
  }
}
