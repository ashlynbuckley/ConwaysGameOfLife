
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferStrategy;

enum inProgress{
	IN_PROGRESS,
	NOT_IN_PROGRESS
}
public class GameOfLife extends JFrame implements Runnable, MouseListener, MouseMotionListener {
	private static final Dimension WindowSize = new Dimension(800,800);
	private BufferStrategy strategy;
	protected inProgress gameState;
	private boolean doNotRandomise; //i dont want it to keep adding random white squares every time "random" is clicked
    public String fileName = "C:\\Users\\ashly\\cellState.txt";
   
	//dimensions of the array
	//the [2] is for the front and back buffer
	private boolean[][][] cells = new boolean[40][40][2];
	
	//constructor
	public GameOfLife() {
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.setTitle("Game of Life");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Display the window, centred on the screen
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int xDim = screensize.width/2 - WindowSize.width/2;
		int yDim = screensize.height/2 - WindowSize.height/2;
		setBounds(xDim, yDim, WindowSize.width, WindowSize.height);
		setVisible(true);
		
		//buffer
		createBufferStrategy(2);
		strategy = getBufferStrategy();	
		//setting game states and flags
		gameState = inProgress.NOT_IN_PROGRESS;
		doNotRandomise = false; //flag for randomising
		
		//start a thread
		Thread t = new Thread(this);
		t.start();
		
 	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//get the x and y coords of mouse
			int x = e.getX() /20;
			int y = e.getY() /20;
			//ensure x and y are within bounds
			
			//so if the game isn't in progress you can switch states of cells and press buttons
			if (gameState == inProgress.NOT_IN_PROGRESS) {
			    //the numbers are coming from the fact I divide the coords by 20, so x = 30 but actually it's 30/2 = 1.5
			    //start button
			    if (x >= 1.5 && x <= 6.5 && y >= 2.5 && y <= 4.5) {
			    	//start buttons starts the evolution
			        gameState = inProgress.IN_PROGRESS;
			    }
			    
			    //random button
			    if (x >= 7.5 && x <= 12.5 && y >= 2.5 && y <= 4.5) {
			    	if (!doNotRandomise) {
			    		randomiseCells(); //call randomise method, sets amount of cells true
			    	}
			    }
			    
			    //save button
			    if (x >= 13.5 && x <= 18.5 && y >= 2.5 && y <=4.5) {
			    	save();
			    }
			    //load button
			    if (x >= 19.5 && x <= 24.5 && y >= 2.5 && y <=4.5) {
			    	load();
			    }
			    
			    //alive/dead
			    if (x >= 0 && x < cells.length && y >= 0 && y < cells[0].length) {
			    	//invert boolean
			    	//doing cells[x][y] throws off the placement of white squares
			        cells[y][x][0] = !cells[y][x][0];
			    }
			    this.repaint();
			}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		//same logic as mouseClicked
	    //get the x and y coords of mouse
	    int x = e.getX() / 20;
	    int y = e.getY() / 20;

	    //ensure x and y are within bounds
	    if (gameState == inProgress.NOT_IN_PROGRESS && x >= 0 && x < cells.length && y >= 0 && y < cells[0].length) {
	        //invert boolean
	        cells[y][x][0] = !cells[y][x][0];
	        this.repaint();
	    }
	}
	
	//unused
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {	
	}
	@Override
	public void mouseMoved(MouseEvent e) {	
	}
	
		
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);//wait to update screen again
				
				//switch
				switch (gameState) {
				//in this state you can randomise or turn on squares yourself
				case NOT_IN_PROGRESS:
					break;
					
				//triggered when START is pressed, now ruleset is applied, no more adding squares
				case IN_PROGRESS:
					//do the ruleset
					//there is no need for an if the game is in progress bc it will be if it gets to this point in the code
					//and there is no shifting back to not_in_progress
					for (int i = 0; i < cells.length; i++) {
						 for (int j = 0; j < cells[i].length; j++) {
							//get state of neighbours
							int aliveNeighbours = getAliveNeighbours(j, i);
							//checks for ruleset
							if (cells[i][j][0]) {
								//alive cell with less than 2 alive neighbours dies
								if (cells[i][j][0] && aliveNeighbours < 2) {
									cells[i][j][1] = false;
								}
								//alive with 2 or 3 alive neighbour, alive/born
								else if (aliveNeighbours<4) {
									cells[i][j][1] = true;
								}
								//else you have more than 3 alvie neighbours, die 
								else {
									cells[i][j][1] = false;
								}
							} else {
								//you have exactly 3, you are born
								if (aliveNeighbours == 3) {
									cells[i][j][1] = true;
								} else {
									cells[i][j][1] = false;
								}
							}
						 }
					}
					//update buffer
					//fill the front buffer with the new updates from [1]
					for (int i = 0; i < cells.length; i++) {
					    for (int j = 0; j < cells[i].length; j++) {
					        cells[i][j][0] = cells[i][j][1];
					    }
					}

					break;
				}
			}
		
		//exception handling for threads interrupting each other
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		//update the images
		this.repaint();
		}
	}
	
	public void randomiseCells() {
		for (int x=0;x<40;x++) {
			 for (int y=0;y<40;y++) {
				 cells[x][y][0]=(Math.random()<0.25);
			 }
		 }
		doNotRandomise = true; //dont do it again if "random" is pressed again
	}
	
	private int getAliveNeighbours(int x, int y) {
		int liveNeighbours = 0;
		//iterate through all cells
	    for (int xx = -1; xx <= 1; xx++) {
	        for (int yy = -1; yy <= 1; yy++) {
	            if (xx != 0 || yy != 0) { //dont count cell itself
	            	//wrap arounds
	                int xxx = (x + xx + cells.length) % cells.length;
	                int yyy = (y + yy + cells[0].length) % cells[0].length;
	                //if this neighbour cell is alive increment
	                if (cells[xxx][yyy][0]) {
	                    liveNeighbours++;
	                }
	            }
	        }
	    }
		return liveNeighbours;
	}
	
	public void save() {
		//writing to this file
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			//save the game state as a string with the method and then write that string to the file
			String cellStates = encodeGameStateAsString();
			writer.write(cellStates);
			writer.close();
			System.out.println("Game saved"); //debug
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
	    String line = null;
	    try {
	    	//read in the file
	        BufferedReader reader = new BufferedReader(new FileReader(fileName));

	        do {
	            try {
	                line = reader.readLine();
	                //check for line != null so the last iteration is dealt with
	                if (line != null) {
	                	int counter = 0; //used for iterating through the line
	                	//go through each cell, respect to its corresponding digit in the file
	                    for (int x = 0; x < cells.length; x++) {
	                    	for (int y = 0; y < cells[0].length; y++) {
	                    		//get the current digit
	                    		char currChar = line.charAt(counter);
	                    		//if the currChar is 1, then make the cell's state true
	                    		cells[x][y][0] = (currChar == '1');
	                            counter++; //on to the next char in the line
	                    	}
	                    }
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	        } while (line != null);

	        reader.close();
	        System.out.println("Game loaded"); //debug
	        this.repaint();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	private String encodeGameStateAsString() {
		StringBuilder stringBuilder = new StringBuilder();
		
		//transform into string
		//get each cell with nested loop and append it to the string, 1 for true, 0 for false, as the states in cell[][] are booleans
		//now we have a string that mirrors the 2d array but instead of booleans, they're 1s and 0s, mapping to what we see on the screen
		for (int i = 0; i < cells.length; i++) {
	        for (int j = 0; j < cells[i].length; j++) {
	            stringBuilder.append(cells[i][j][0] ? '1' : '0');
	        }
	    }
		
		return stringBuilder.toString();
	}
	
	public void drawButtons(Graphics g) {
		g.setFont(new Font("Arial", Font.BOLD, 20)); //set font
		
		//start button
		//background
		Color c = Color.blue;
		g.setColor(c);
		g.fillRect(30, 50, 100, 40);
		//text
		g.setColor(Color.white);
		g.drawString("START", 45, 75);
		
		//random button
		//background
		g.setColor(c);
		g.fillRect(150, 50, 100, 40);
		//text
		g.setColor(Color.white); 
		g.drawString("RANDOM", 155, 75);
		
		//save button
		c = new Color(0, 128, 0);
		g.setColor(c);
		g.fillRect(270, 50, 100, 40);
		//text
		g.setColor(Color.white); 
		g.drawString("SAVE", 292, 75);
		
		//load button
		g.setColor(c);
		g.fillRect(390, 50, 100, 40);
		//text
		g.setColor(Color.white); 
		g.drawString("LOAD", 410, 75);
		
	}
	public void paint(Graphics g) {
		//paint perioidcally
		g = strategy.getDrawGraphics();
		int y = 0;
		for (int i = 0; i < cells.length; i++) {
			int x = 0;
			
			for (int j = 0; j <cells[i].length; j++) {
				//if this boolean is false
				if (!cells[i][j][0]) {
					Color c = Color.black; //background, needs to be repainted every time to avoid ghosting
					g.setColor(c);
		            g.fillRect(x, y, 20, 20); //fillRect() filling g square with chosen colour
		            x += 20;
				}
				//if this boolean is true
				else if (cells[i][j][0]) {
				Color c = Color.white; //if youre white youre alive
				g.setColor(c);
	            g.fillRect(x, y, 20, 20); //fillRect() filling g square with chosen colour
	            x += 20; 
				}
			}
			y += 20;
		}//end of for
		drawButtons(g); //adds buttons to top corner
    
		strategy.show();
	}
	
	public static void main(String[] args) {
		GameOfLife test = new GameOfLife();
	}

		
	
}
