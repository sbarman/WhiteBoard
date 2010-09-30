package whiteboard;

import javax.swing.JFrame;

public class WhiteBoard {
  private JFrame frame;
  
  public void run() {
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(640, 640);
    frame.setVisible(true);
  }
  public static void main(String[] args) {
    WhiteBoard wb = new WhiteBoard();
    wb.run();
  }
}
