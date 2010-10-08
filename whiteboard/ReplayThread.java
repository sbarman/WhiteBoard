package whiteboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Vector;

import javax.swing.JFrame;

import whiteboard.packet.Packet;

public class ReplayThread extends Thread {

	private Dimension drawingPanelSize;
	private WhiteBoardClient client;

	public ReplayThread(Dimension drawingPanelSize, WhiteBoardClient client) {
		this.drawingPanelSize = drawingPanelSize;
		this.client = client;
	}
	
	public void run() {
		JFrame replayFrame = new JFrame();

		DrawingPanel replayDrawingPanel = new DrawingPanel(null);
		replayDrawingPanel.setPreferredSize(drawingPanelSize);
		replayFrame.getContentPane().add(replayDrawingPanel,
				BorderLayout.CENTER);
		replayFrame.pack();
		replayFrame.setResizable(false);
		replayFrame.setVisible(true);
		replayDrawingPanel.clearImage();

		Vector<Packet> packets = client.getPacketHistory();
		if (packets.isEmpty()) {
			return;
		}
		
		PipedOutputStream pipedOutput;
		PipedInputStream pipedInput;
		ObjectOutputStream output;
		ObjectInputStream input;

		try {
			pipedOutput = new PipedOutputStream();
			pipedInput = new PipedInputStream(pipedOutput);
			output = new ObjectOutputStream(pipedOutput);
			input = new ObjectInputStream(pipedInput);
		} catch (IOException e1) {
			return;
		}
		
		WhiteBoardClientListener listener = new WhiteBoardClientListener(
				input, null, replayDrawingPanel, client.getClientId());
		listener.start();
		
		int i = 0;
//		long timeDif = System.currentTimeMillis() - packets.get(0).getTime();
		long startTime = System.currentTimeMillis();
		
		while (i < packets.size()) {
			//long waitTime = (timeDif + packets.get(i).getTime()) - System.currentTimeMillis();
			long waitTime = (packets.get(i).getTime() - packets.get(0).getTime())/2 -
					(System.currentTimeMillis() - startTime);  
			if (waitTime <= 0) {
				try {
					output.writeObject(packets.get(i));
					System.out.println("Packet(" + packets.get(i).getTime() + ": " + i);
					i++;
				} catch (IOException e1) {
					i = packets.size();
				}	
			} else {
				try {
					System.out.println("yeild: " + waitTime);
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
				
				}
			}
		}
		
		listener.stopThread();
		
		try {
			input.close();
			output.close();
			pipedInput.close();
			pipedOutput.close();
		} catch (IOException e) {
		}
	}
}
