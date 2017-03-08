package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class GraphicHanoi {
	FrameComp fc;
	public GraphicHanoi(){
		fc = new FrameComp();
	}
	public void start(){
		fc.setSize(1000,680);
		fc.setResizable(false);
		fc.setVisible(true);
		fc.setTitle("Towers of Hanoi");
		fc.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	@SuppressWarnings("serial")
	public class FrameComp extends JFrame{
		private GraphicComp gc;
		private Container pane;
		private JMenuBar menuBar;
		private JMenu menu;
		private JMenuItem numDiscs_m,solve_m,delay_m;
		private int numDiscs,stepcount,step,delay;
		private String[] steps;
		private Timer t;
		ActionListener menuListen;
		public FrameComp(){
			Tick tock = new Tick();
			delay=500;
			t=new Timer(delay,tock);
			gc=new GraphicComp();
			pane=getContentPane();
			pane.add(gc,BorderLayout.CENTER);
			menuBar=new JMenuBar();
			setJMenuBar(menuBar);
			menu=new JMenu("Options");
			menuBar.add(menu);
			menuListen=new menuDid();
			numDiscs_m=new JMenuItem("Set number of discs");
			numDiscs_m.addActionListener(menuListen);
			solve_m=new JMenuItem("Solve");
			solve_m.addActionListener(menuListen);
			delay_m=new JMenuItem("Set delay");
			delay_m.addActionListener(menuListen);
			menu.add(numDiscs_m);
			menu.add(delay_m);
			menu.addSeparator();
			menu.add(solve_m);
			numDiscs=5;
			steps=new String[(int)Math.pow(2, 5)-1];
			stepcount=0;
			step=0;
		}
		private class Tick implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(step<(int)Math.pow(2, numDiscs)-1){
					animateSolve(step);
					step++;
					gc.show();
					gc.count();
				}else{
					t.stop();
					gc.finished=true;
					gc.show();
				}
			}
		}
		private void showIntError(String additional){
			JOptionPane.showMessageDialog(gc, "Please enter a valid integer value."+additional, "Invalid Entry Error!", JOptionPane.ERROR_MESSAGE);
		}
		public void setDelay(){
			try{
				delay=Integer.parseInt(JOptionPane.showInputDialog(gc, "Set timer dely:", "Discs",JOptionPane.QUESTION_MESSAGE));
			}catch(NumberFormatException e){
				if (e.getCause()==null){
					t.setDelay(500);
				}else{
					showIntError("");
				}
			}
			t.setDelay(delay);
			gc.gimmeDelay(delay);
		}
		public void solve(int x,int start, int aux, int end){
			if (x==1){
				steps[stepcount]=start+","+end;
				stepcount++;
			}else{
				solve(x-1,start,end,aux);
				steps[stepcount]=start+","+end;
				stepcount++;
				solve(x-1,aux,start,end);
			}
		}
		public void animateSolve(int step){
			int oPeg=Integer.parseInt(""+steps[step].charAt(0));
			int dPeg=Integer.parseInt(""+steps[step].charAt(2));
			int disc=checkTopDisc(oPeg);
			gc.d.moveDiscToPeg(disc, dPeg);
		}
		public int checkTopDisc(int peg){
			for (int i=1;i<numDiscs+1;i++){
				if (gc.d.getCurrentPeg(i)==peg){
					return i;
				}
			}
			return -1;
		}
		private void setNumDiscs(){
			int input=5;
			try{
				input=Integer.parseInt(JOptionPane.showInputDialog(gc, "Set number of discs:", "Discs",JOptionPane.QUESTION_MESSAGE));
			}
			catch(NumberFormatException e){
				if (e.getCause()==null){
					numDiscs=5;
					gc.setNumDiscs(5);
				}else
					showIntError("");
			}if (input>21){
				showIntError(" Going over 21 discs is a bad idea. Defaulting to 5.");
				gc.setNumDiscs(5);
				input=5;
			}
			numDiscs=input;
			gc.setNumDiscs(numDiscs);
			steps=new String[(int)Math.pow(2, numDiscs)-1];
			stepcount=0;
			step=0;
			gc.finished=false;
		}
		private class menuDid implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==numDiscs_m){
					setNumDiscs();
				}
				if (e.getSource()==solve_m){
					solve(numDiscs,1,2,3);
					t.start();
				}
				if (e.getSource()==delay_m){
					setDelay();
				}
			}
		}
	}
	private class Discs{
		private int numDiscs,xScale,yScale,hostWidth;
		private int[] x, width,wScale,currentPeg,previousPeg;
		public Discs(int numDiscs){
			hostWidth=1000;
			this.numDiscs=numDiscs;
			xScale=hostWidth/28;
			yScale=515/(numDiscs);
			x=new int[numDiscs+1];
			wScale=new int[numDiscs+1];
			width=new int[numDiscs+1];
			currentPeg=new int[numDiscs+1];
			previousPeg=new int[numDiscs+1];
			for (int x=1;x<numDiscs+1;x++){
				wScale[x]=11*xScale/numDiscs;
				this.x[x]=6*xScale-(xScale/2)-x*wScale[x]/2;
				width[x]=x*wScale[x];
				currentPeg[x]=1;
				previousPeg[x]=1;
			}
		}
		public int getX(int disc){
			return x[disc];
		}
		public int getWidth(int disc){
			return width[disc];
		}
		public int getY(int disc){
			return 125+yScale*(disc-1);
		}
		public int getHeight(){
			return yScale;
		}
		public int getCurrentPeg(int disc){
			return currentPeg[disc];
		}
		public void setHostWidth(int hostWidth){
			this.hostWidth=hostWidth;
			xScale=hostWidth/28;
			for (int x=1;x<numDiscs+1;x++){
				wScale[x]=11*xScale/numDiscs;
				width[x]=x*wScale[x];
			}
		}
		public void moveDiscToPeg(int disc,int peg){
			previousPeg[disc]=currentPeg[disc];
			currentPeg[disc]=peg;
			switch(peg){
			case 1: x[disc]=6*xScale-(xScale/2)-disc*wScale[disc]/2;
				break;
			case 2: x[disc]=14*xScale-(xScale/2)-disc*wScale[disc]/2;
				break;
			case 3: x[disc]=22*xScale-(xScale/2)-disc*wScale[disc]/2;
				break;
			}
		}
		
	}
	@SuppressWarnings("serial")
	private class GraphicComp extends JComponent{
		private int numDiscs,delay;
		public Discs d;
		boolean finished;
		double timecount;
		public GraphicComp(){
			numDiscs=5;
			d=new Discs(5);
			finished=false;
			timecount=0;
			delay=500;
		}
		public void show(){
			repaint();
		}
		public void gimmeDelay(int delay){
			this.delay=delay;
		}
		public void count(){
			timecount+=delay;
		}
		public void setNumDiscs(int numDiscs){
			this.numDiscs=numDiscs;
			d=new Discs(numDiscs);
			timecount=0;
			repaint();
		}
		public void paintComponent (Graphics g){
			super.paintComponent(g);
			Graphics2D gtd = (Graphics2D) g;
			gtd.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			gtd.setBackground(Color.BLACK);
			gtd.clearRect(0, 0, getWidth(), getHeight());
			
			d.setHostWidth(getWidth());
			gtd.setColor(Color.WHITE);
			int xscale=getWidth()/28;
			for (int x=0;x<56;x++){
				if (x%16==10){
					gtd.fillRect(x*xscale/2+xscale/3-1, 100, xscale/2-1, getHeight());
				}
			}
			for (int i=1;i<numDiscs+1;i++){
				gtd.setColor(Color.GRAY);
				gtd.fillRect(d.getX(i), d.getY(i), d.getWidth(i), d.getHeight());
				gtd.setColor(Color.WHITE);
				gtd.drawRect(d.getX(i), d.getY(i), d.getWidth(i)-1, d.getHeight());
			}
			if (finished){
				gtd.drawString("Solved in "+timecount/1000+"seconds.", getWidth()/2, getHeight()/2);
			}
		}
	}
	public static void main(String[] args){
		GraphicHanoi gh = new GraphicHanoi();
		gh.start();
	}
}

