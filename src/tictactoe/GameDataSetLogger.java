package tictactoe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameDataSetLogger 
{
	private ArrayList<char[]> inputs;
	private ArrayList<double[]> outputs;
	
	public GameDataSetLogger()
	{
		inputs = new ArrayList<char[]>();
		outputs = new ArrayList<double[]>();
	}
	
	public void saveData(String filePath)
	{
		BufferedWriter outputFile = null;
		
		try 
		{
			File file = new File(filePath);
			
			outputFile = new BufferedWriter(new FileWriter(filePath, file.exists()));
			
			for (int i = 0; i < inputs.size(); i++)
				outputFile.write(Arrays.toString(inputs.get(i)) + Arrays.toString(outputs.get(i)) + "\n");
			
			outputFile.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void addData(char[] board, int moveIndex)
	{	
		double[] move = new double[9];
		Arrays.fill(move, 0.0);
		move[moveIndex] = 1.0;
		
		addAllOrientations(Arrays.copyOf(board, board.length), move);
	}
	
	public void clearData()
	{
		inputs.clear();
		outputs.clear();
	}
	
	private void addAllOrientations(char[] originalBoard, double[] originalMove)
	{
		ArrayList<char[]> possibleInputs = new ArrayList<char[]>();
		ArrayList<double[]> possibleOutputs = new ArrayList<double[]>();
		
		//add original board and move to inputs and outputs
		possibleInputs.add(originalBoard);
		possibleOutputs.add(originalMove);
		
		char[] rotationBoard = rotate(originalBoard);
		double[] rotationMove = rotate(originalMove);
		possibleInputs.add(rotationBoard);
		possibleOutputs.add(rotationMove);
		
		//rotate board and move 2 times and add each of 2 unique boards and moves to inputs and outputs
		for (int i = 0; i < 2; i++)
		{
			rotationBoard = rotate(rotationBoard);
			rotationMove = rotate(rotationMove);
			possibleInputs.add(rotationBoard);
			possibleOutputs.add(rotationMove);
		}
		
		//reflect board and move once
		char[] reflectionBoard = reflect(originalBoard);
		double[] reflectionMove = reflect(originalMove);
		possibleInputs.add(reflectionBoard);
		possibleOutputs.add(reflectionMove);
		
		//rotate the reflected board and move 3 times and add each of 3 unique boards and moves to inputs and outputs
		for (int i = 0; i < 3; i++)
		{
			reflectionBoard = rotate(reflectionBoard);
			reflectionMove = rotate(reflectionMove);
			possibleInputs.add(reflectionBoard);
			possibleOutputs.add(reflectionMove);
		}
		
		//removing all duplicates
		for (int i = 0; i < possibleInputs.size() - 1; i++)
		{
			for (int j = i + 1; j < possibleInputs.size(); j++)
			{
				if (Arrays.equals(possibleInputs.get(i), possibleInputs.get(j)))
				{
					possibleInputs.remove(j);
					possibleOutputs.remove(j);
					j--;
				}
			}
		}
		
		//add all original inputs and outputs to inputs and outputs
		for (int i = 0; i < possibleInputs.size(); i++)
		{
			inputs.add(possibleInputs.get(i));
			outputs.add(possibleOutputs.get(i));
		}
	}
	
	private char[] reflect(char[] board)
	{
		char[] reflection = Arrays.copyOf(board, board.length);
		for (int i = 0; i < 3; i++)
		{
			reflection[3*i] = board[3*i+2];
			reflection[3*i+2] = board[3*i];
		}
		return reflection;
	}
	
	private double[] reflect(double[] move)
	{
		double[] reflection = Arrays.copyOf(move, move.length);
		for (int i = 0; i < 3; i++)
		{
			reflection[3*i] = move[3*i+2];
			reflection[3*i+2] = move[3*i];
		}
		return reflection;
	}
	
	private char[] rotate(char[] board)
	{
		char[] rotation = Arrays.copyOf(board, board.length);
		rotation[0] = board[2];
		rotation[1] = board[5];
		rotation[2] = board[8];
		rotation[3] = board[1];
		rotation[5] = board[7];
		rotation[6] = board[0];
		rotation[7] = board[3];
		rotation[8] = board[6];
		return rotation;
	}
	
	private double[] rotate(double[] move)
	{
		double[] rotation = Arrays.copyOf(move, move.length);
		rotation[0] = move[2];
		rotation[1] = move[5];
		rotation[2] = move[8];
		rotation[3] = move[1];
		rotation[5] = move[7];
		rotation[6] = move[0];
		rotation[7] = move[3];
		rotation[8] = move[6];
		return rotation;
	}
}
