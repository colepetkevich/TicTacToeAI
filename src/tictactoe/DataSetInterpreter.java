package tictactoe;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DataSetInterpreter 
{
	public static final double INTERPRETED_X = 1.0;
	public static final double INTERPRETED_O = 0.5;
	public static final double INTERPRETED_EMPTY = -1.0;
	
	private String filePath;
	private ArrayList<double[]> inputs;
	private ArrayList<double[]> outputs;
	
	public DataSetInterpreter()
	{
		inputs = new ArrayList<double[]>();
		outputs = new ArrayList<double[]>();
	}
	
	public void interpret(String fileName)
	{
		BufferedReader inputFile = null;
		
		try 
		{
			inputFile = new BufferedReader(new FileReader(fileName));
			
			String line = inputFile.readLine();
			while (line != null)
			{
				double[] input = new double[9];
				double[] output = new double[9];
				
				Scanner scanner = new Scanner(line);
				scanner.useDelimiter("]");
				
				Scanner game = new Scanner(" " + scanner.next().substring(1));
				game.useDelimiter(",");
				Scanner move = new Scanner(" " + scanner.next().substring(1));
				move.useDelimiter(",");
				
				//interpreting and adding inputs
				char[] charInput = new char[9];
				for (int i = 0; i < input.length; i++)
					charInput[i] = game.next().charAt(1);
				input = interpretCharInput(charInput);
				inputs.add(input);
				
				//adding outputs
				for (int i = 0; i < output.length; i++)
					output[i] = Double.parseDouble(move.next().substring(1));
				outputs.add(output);
				
				line = inputFile.readLine();
			}
			
			inputFile.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void printIO()
	{
		for (int i = 0; i < inputs.size(); i++)
		{
			System.out.println(Arrays.toString(inputs.get(i)) + Arrays.toString(outputs.get(i)));
		}
	}
	
	public ArrayList<double[]> getInputs() {
		return inputs;
	}

	public ArrayList<double[]> getOutputs() {
		return outputs;
	}
	
	public static double[] interpretCharInput(char[] charInput)
	{
		double[] input = new double[charInput.length];
		
		for (int i = 0; i < charInput.length; i++)
		{
			switch (charInput[i])
			{
				case TicTacToe.X:
					input[i] = INTERPRETED_X;
					break;
				case TicTacToe.O:
					input[i] = INTERPRETED_O;
					break;
				default:
					input[i] = INTERPRETED_EMPTY;
					break;
			}
		}
		
		return input;
	}
}
