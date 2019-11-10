/* Calculator.java - Demonstrates Graphics
 * Author:     Cole Petkevich
 * Module:     14
 * Project:    Project 1 of Homework 14
 * Description:
 *
 *    Instance variables:
			private String turn = "O";
			private String[][] ticTacToe = new String[3][3];
			
			private boolean gameOver = false;
			
			JLabel textLabel;
			
			JButton button11;
			JButton button12;
			JButton button13;
			JButton button21;
			JButton button22;
			JButton button23;
			JButton button31;
			JButton button32;
			JButton button33;
 *
 *    Static variables:
			private static final String BUTTON11 = "button 11";
			private static final String BUTTON12 = "button 12";
			private static final String BUTTON13 = "button 13";
			private static final String BUTTON21 = "button 21";
			private static final String BUTTON22 = "button 22";
			private static final String BUTTON23 = "button 23";
			private static final String BUTTON31 = "button 31";
			private static final String BUTTON32 = "button 32";
			private static final String BUTTON33 = "button 33";
			
			private static final String X = "X";
			private static final String O = "O";
			private static final String EMPTY = "empty";
 *
 *    Methods:
 *    		public TicTacToe() - sets all the qualities of the JFrame
 *        	public void actionPerformed(ActionEvent actionEvent) - does actions depending on actionEvent's command
 *        	private String nextTurn() - returns the current turn
 *        	private String getWinner() - returns the winner
 *        	private boolean boardFull() - returns true if the board is full
 */

package tictactoe;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import neuralNetwork.NeuralNetwork;

public class TicTacToe extends JFrame implements ActionListener
{
	//static variables
	public static final String DATA_SET_FILE_PATH = "src/TicTacToeGameDataSet.txt";
	
	public static final char X = 'X';
	public static final char O = 'O';
	public static final char TIE = 'T';
	public static final char EMPTY = ' ';
	
	private static final String TITLE = "Tic-Tac-Toe Game";
	private static final String RESET = "Reset";
	
	private static final String BUTTON0 = "button 0";
	private static final String BUTTON1 = "button 1";
	private static final String BUTTON2 = "button 2";
	private static final String BUTTON3 = "button 3";
	private static final String BUTTON4 = "button 4";
	private static final String BUTTON5 = "button 5";
	private static final String BUTTON6 = "button 6";
	private static final String BUTTON7 = "button 7";
	private static final String BUTTON8 = "button 8";
	
	//instance variables
	private char turn;
	private char[] ticTacToe = new char[9];
	private GameDataSetLogger dataLogger;
	private boolean isSinglePlayer;
	
	private boolean gameOver = false;
	
	private JLabel textLabel;
	private JButton resetButton;
	
	private JButton button0;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;
	private JButton button7;
	private JButton button8;
	
	//single player variables
	private static final String NETWORK_FILE_PATH = "src/NeuralNetwork.dat";
	private NeuralNetwork neuralNetwork;
	
	public TicTacToe(boolean isSinglePlayer)
	{
		this.isSinglePlayer = isSinglePlayer;
		neuralNetwork = null;
		if (isSinglePlayer)
		{
			neuralNetwork = new NeuralNetwork(NETWORK_FILE_PATH);
		}
		
		//setting JFrame qualities
		setSize(450, 600);
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		//setting layout
		setLayout(new BorderLayout());
		
		//setting text label and reset button
		JPanel titlePanel = new JPanel();
		add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new BorderLayout());
		titlePanel.add(new JPanel(), BorderLayout.NORTH);
		titlePanel.add(new JPanel(), BorderLayout.SOUTH);
		titlePanel.add(new JPanel(), BorderLayout.WEST);
		titlePanel.add(new JPanel(), BorderLayout.EAST);
		
		textLabel = new JLabel();
		titlePanel.add(textLabel, BorderLayout.CENTER);
		textLabel.setFont(new Font("Arial", Font.PLAIN, 42));
		textLabel.setText(TITLE);
		textLabel.setVerticalAlignment(JLabel.CENTER);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel resetPanel = new JPanel();
		add(resetPanel, BorderLayout.SOUTH);
		resetPanel.setLayout(new BorderLayout());
		resetPanel.add(new JPanel(), BorderLayout.NORTH);
		//resetPanel.add(new JPanel(), BorderLayout.SOUTH);
		//resetPanel.add(new JPanel(), BorderLayout.WEST);
		//resetPanel.add(new JPanel(), BorderLayout.EAST);
		
		resetButton = new JButton();
		resetPanel.add(resetButton, BorderLayout.CENTER);
		resetButton.setFont(new Font("Arial", Font.PLAIN, 32));
		resetButton.setText(RESET);
		
		resetButton.addActionListener(this);
		resetButton.setActionCommand(RESET);
		
		//setting gameboard label
		JLabel gameLabel = new JLabel();
		add(gameLabel, BorderLayout.CENTER);
		gameLabel.setLayout(new GridLayout(3, 3));
		
		//adding buttons
		button0 = new JButton();
		gameLabel.add(button0);
		button0.setFont(new Font("Arial", Font.PLAIN, 64));
		button1 = new JButton();
		gameLabel.add(button1);
		button1.setFont(new Font("Arial", Font.PLAIN, 64));
		button2 = new JButton();
		gameLabel.add(button2);
		button2.setFont(new Font("Arial", Font.PLAIN, 64));
		button3 = new JButton();
		gameLabel.add(button3);
		button3.setFont(new Font("Arial", Font.PLAIN, 64));
		button4 = new JButton();
		gameLabel.add(button4);
		button4.setFont(new Font("Arial", Font.PLAIN, 64));
		button5 = new JButton();
		gameLabel.add(button5);
		button5.setFont(new Font("Arial", Font.PLAIN, 64));
		button6 = new JButton();
		gameLabel.add(button6);
		button6.setFont(new Font("Arial", Font.PLAIN, 64));
		button7 = new JButton();
		gameLabel.add(button7);
		button7.setFont(new Font("Arial", Font.PLAIN, 64));
		button8 = new JButton();
		gameLabel.add(button8);
		button8.setFont(new Font("Arial", Font.PLAIN, 64));
		
		//adding actionListeners and adding action commands
		button0.addActionListener(this);
		button0.setActionCommand(BUTTON0);
		button1.addActionListener(this);
		button1.setActionCommand(BUTTON1);
		button2.addActionListener(this);
		button2.setActionCommand(BUTTON2);
		button3.addActionListener(this);
		button3.setActionCommand(BUTTON3);
		button4.addActionListener(this);
		button4.setActionCommand(BUTTON4);
		button5.addActionListener(this);
		button5.setActionCommand(BUTTON5);
		button6.addActionListener(this);
		button6.setActionCommand(BUTTON6);
		button7.addActionListener(this);
		button7.setActionCommand(BUTTON7);
		button8.addActionListener(this);
		button8.setActionCommand(BUTTON8);
		
		dataLogger = new GameDataSetLogger();
		
		//setting game!
		resetGame();
	}

	//does an action depending on the action command
	public void actionPerformed(ActionEvent action) 
	{
		String actionCommand = action.getActionCommand();
		
		if (actionCommand.equals(RESET))
			resetGame();
		
		if (!gameOver)
		{
			switch (actionCommand)
			{
				case BUTTON0:
					if (ticTacToe[0] == EMPTY)
					{
						if (turn == O && !isSinglePlayer)
						{
							dataLogger.addData(ticTacToe, 0);
						}
							
						
						ticTacToe[0] = turn;
						button0.setText(turn + "");
						nextTurn();
					}
					break;
				case BUTTON1:
					if (ticTacToe[1] == EMPTY)
					{
						if (turn == O && !isSinglePlayer)
							dataLogger.addData(ticTacToe, 1);
						
						ticTacToe[1] = turn;
						button1.setText(turn + "");
						nextTurn();
					}
					break;
				case BUTTON2:
					if (ticTacToe[2] == EMPTY)
					{
						if (turn == O && !isSinglePlayer)
							dataLogger.addData(ticTacToe, 2);
						
						ticTacToe[2] = turn;
						button2.setText(turn + "");
						nextTurn();
					}
					break;
				case BUTTON3:
					if (ticTacToe[3] == EMPTY)
					{
						if (turn == O && !isSinglePlayer)
							dataLogger.addData(ticTacToe, 3);
						
						ticTacToe[3] = turn;
						button3.setText(turn + "");
						nextTurn();
					}
					break;
				case BUTTON4:
					if (ticTacToe[4] == EMPTY)
					{
						if (turn == O && !isSinglePlayer)
							dataLogger.addData(ticTacToe, 4);
						
						ticTacToe[4] = turn;
						button4.setText(turn + "");
						nextTurn();
					}
					break;
				case BUTTON5:
					if (ticTacToe[5] == EMPTY)
					{
						if (turn == O && !isSinglePlayer)
							dataLogger.addData(ticTacToe, 5);
						
						ticTacToe[5] = turn;
						button5.setText(turn + "");
						nextTurn();
					}
					break;
				case BUTTON6:
					if (ticTacToe[6] == EMPTY)
					{
						if (turn == O && !isSinglePlayer)
							dataLogger.addData(ticTacToe, 6);
						
						ticTacToe[6] = turn;
						button6.setText(turn + "");
						nextTurn();
					}
					break;
				case BUTTON7:
					if (ticTacToe[7] == EMPTY)
					{
						if (turn == O && !isSinglePlayer)
							dataLogger.addData(ticTacToe, 7);
						
						ticTacToe[7] = turn;
						button7.setText(turn + "");
						nextTurn();
					}
					break;
				case BUTTON8:
					if (ticTacToe[8] == EMPTY)
					{
						if (turn == O && !isSinglePlayer)
							dataLogger.addData(ticTacToe, 8);
						
						ticTacToe[8] = turn;
						button8.setText(turn + "");
						nextTurn();
					}
					break;
			}
			
			if (isSinglePlayer && turn == O && getWinner() != X && !boardFull())
			{
				//predicting move and making sure it is valid
				neuralNetwork.forwardPropagate(DataSetInterpreter.interpretCharInput(ticTacToe));
				double[] prediction = neuralNetwork.getOutput();
				//System.out.println(Arrays.toString(prediction));
				int predictionIndex = NeuralNetwork.maxIndex(prediction);
				while (ticTacToe[predictionIndex] != EMPTY)
				{	
					prediction[predictionIndex] = -Double.MAX_VALUE;
					predictionIndex = NeuralNetwork.maxIndex(prediction);
					
					if (boardFull())
						break;
				}
				
				//making move
				ticTacToe[predictionIndex] = turn;
				if (predictionIndex == 0)
					button0.setText(turn + "");
				else if (predictionIndex == 1)
					button1.setText(turn + "");
				else if (predictionIndex == 2)
					button2.setText(turn + "");
				else if (predictionIndex == 3)
					button3.setText(turn + "");
				else if (predictionIndex == 4)
					button4.setText(turn + "");
				else if (predictionIndex == 5)
					button5.setText(turn + "");
				else if (predictionIndex == 6)
					button6.setText(turn + "");
				else if (predictionIndex == 7)
					button7.setText(turn + "");
				else if (predictionIndex == 8)
					button8.setText(turn + "");
				nextTurn();
			}
			
			//if the board is full or there is a winner end the game
			if (boardFull() || getWinner() != EMPTY)
			{
				char winner = getWinner();
				
				//determine the games outcome
				if (winner == EMPTY)
				{
					textLabel.setText("Cat\'s Game!");
					if (!isSinglePlayer)
						dataLogger.saveData(DATA_SET_FILE_PATH);
				}
				else if (winner == O)
				{
					textLabel.setText("O Wins!");
					if (!isSinglePlayer)
						dataLogger.saveData(DATA_SET_FILE_PATH);
				}
				else 
					textLabel.setText("X Wins!");
				
				//end game when there is a winner or there is a tile
				gameOver = true;
				dataLogger.clearData();
			}

		}
	}
	
	private void resetGame()
	{
		turn = X;
		gameOver = false;
		Arrays.fill(ticTacToe, EMPTY);
		
		textLabel.setText(TITLE);
		button0.setText(EMPTY + "");
		button1.setText(EMPTY + "");
		button2.setText(EMPTY + "");
		button3.setText(EMPTY + "");
		button4.setText(EMPTY + "");
		button5.setText(EMPTY + "");
		button6.setText(EMPTY + "");
		button7.setText(EMPTY + "");
		button8.setText(EMPTY + "");
		
		dataLogger.clearData();
	}
	
	//returns the next turn
	private char nextTurn()
	{
		char currentTurn = turn;
		
		if (turn == X)
			turn = O;
		else
			turn = X;
		
		return currentTurn;
	}
	
	//returns the winner of the game
	private char getWinner()
	{
		//check if the is a winner by having 3 in a row with either X's or O's
		if (ticTacToe[0] == ticTacToe[1] && ticTacToe[0] == ticTacToe[2] && ticTacToe[0] != EMPTY)
			return ticTacToe[0];
		else if (ticTacToe[3] == ticTacToe[4] && ticTacToe[3] == ticTacToe[5] && ticTacToe[3] != EMPTY)
			return ticTacToe[3];
		else if (ticTacToe[6] == ticTacToe[7] && ticTacToe[6] == ticTacToe[8] && ticTacToe[6] != EMPTY)
			return ticTacToe[6];
		else if (ticTacToe[0] == ticTacToe[3] && ticTacToe[0] == ticTacToe[6] && ticTacToe[0] != EMPTY)
			return ticTacToe[0];
		else if (ticTacToe[1] == ticTacToe[4] && ticTacToe[1] == ticTacToe[7] && ticTacToe[1] != EMPTY)
			return ticTacToe[1];
		else if (ticTacToe[2] == ticTacToe[5] && ticTacToe[2] == ticTacToe[8] && ticTacToe[2] != EMPTY)
			return ticTacToe[2];
		else if (ticTacToe[0] == ticTacToe[4] && ticTacToe[0] == ticTacToe[8] && ticTacToe[0] != EMPTY)
			return ticTacToe[0];
		else if (ticTacToe[2] == ticTacToe[4] && ticTacToe[2] == ticTacToe[6] && ticTacToe[2] != EMPTY)
			return ticTacToe[2];
		
		return EMPTY;
	}
	
	//checks if the board is full
	private boolean boardFull()
	{
		for (int i = 0; i < ticTacToe.length; i++)
			if (ticTacToe[i] == EMPTY)
				return false;
		
		return true;
	}
}
