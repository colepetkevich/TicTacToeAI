package neuralNetwork;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import tictactoe.DataSetInterpreter;

public class Trainer 
{
	private static final String NETWORK_FILE_PATH = "src/NeuralNetwork.dat";
	private static final String DATA_SET_FILE_PATH = "src/TicTacToeGameDataSet.txt";
	private static final int NUMBER_OF_CHECKS = 2500;
	private static final int CHECKS_PER_SAVE = 2500;
	
	public static void main(String[] args)
	{
		NeuralNetwork neuralNetwork = null;
		
		//if there is already a file that saves the existing neural network use that one
		if (new File(NETWORK_FILE_PATH).exists())
			neuralNetwork = new NeuralNetwork(NETWORK_FILE_PATH);
		//otherwise create a new random one
		else
		{
			neuralNetwork = new NeuralNetwork(9, 9);
			neuralNetwork.addHiddenLayer(12);
			neuralNetwork.addHiddenLayer(15);
			neuralNetwork.addHiddenLayer(12);
			neuralNetwork.createWeights();
		}
				
		DataSetInterpreter interpreter = new DataSetInterpreter();
		interpreter.interpret(DATA_SET_FILE_PATH);
		//interpreter.printIO();
		
		ArrayList<double[]> inputs = interpreter.getInputs();
		ArrayList<double[]> outputs = interpreter.getOutputs();
		
		//printing initial results
		System.out.println("Training Began at " + LocalTime.now());
		System.out.println("Initial Accuracy: " + neuralNetwork.getAccuracy(inputs, outputs));
		System.out.println("Initial Mean Squared Error: " + neuralNetwork.getMeanSquaredError(inputs, outputs));
		System.out.println();
		
		for (int i = 0; i < NUMBER_OF_CHECKS; i++)
		{
			ArrayList<double[]> tempInputs = (ArrayList<double[]>) inputs.clone();
			ArrayList<double[]> tempOutputs = (ArrayList<double[]>) outputs.clone();
			//trains neural network with inputs and outputs in random order
			while (tempInputs.size() > 0)
			{
				int randomIndex = (int) (tempInputs.size() * Math.random());
				neuralNetwork.train(tempInputs.remove(randomIndex), tempOutputs.remove(randomIndex));
			}
			
			//saving neural network and printing temporary results
			if (i != 0 && i % CHECKS_PER_SAVE == 0)
			{
				neuralNetwork.saveData(NETWORK_FILE_PATH);
				System.out.println("Network Updated at " + LocalTime.now());
				System.out.println("Updated Accuracy: " + neuralNetwork.getAccuracy(inputs, outputs));
				System.out.println("Updated Mean Squared Error: " + neuralNetwork.getMeanSquaredError(inputs, outputs));
				System.out.println();
			}
		}
		
		//saving current neuralNetwork to binary file
		neuralNetwork.saveData(NETWORK_FILE_PATH);
		
		//printing results
		System.out.println("Final Accuracy: " + neuralNetwork.getAccuracy(inputs, outputs));
		System.out.println("Final Mean Squared Error: " + neuralNetwork.getMeanSquaredError(inputs, outputs));
		System.out.println("Program Complete at " + LocalTime.now());
	}
}
