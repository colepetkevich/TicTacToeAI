package neuralNetwork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tictactoe.DataSetInterpreter;

public class NeuralNetwork
{
	private static final double LEARNING_RATE = .01;
	private static final double BIAS_LEARNING_RATE =.0025;
	
	private NeuronLayer inputNeuronLayer;
	private ArrayList<NeuronLayer> hiddenNeuronLayers;
	private NeuronLayer outputNeuronLayer;
	private ArrayList<Weight> weights;
	
	private int neuronCount;
	private int weightCount;

	//default constructor creates a new NeuralNetwork
	public NeuralNetwork(int numOfInputNeurons, int numOfOutputNeurons)
	{
		//creating neuron arrays
		inputNeuronLayer = new NeuronLayer(numOfInputNeurons);
		outputNeuronLayer = new NeuronLayer(numOfOutputNeurons);
		hiddenNeuronLayers = new ArrayList<NeuronLayer>();
		
		//setting input's next layer to output layer and setting output's previous layer to input layer
		inputNeuronLayer.setPreviousLayer(null);
		inputNeuronLayer.setNextLayer(outputNeuronLayer);
		outputNeuronLayer.setPreviousLayer(inputNeuronLayer);
		outputNeuronLayer.setNextLayer(null);
		
		weights = new ArrayList<Weight>();
	}
	
	//file io constructor get NeuralNetworkData from a binary file
	public NeuralNetwork(String filePath)
	{
		setData(filePath);
	}
	
	//adds a hidden
	public void addHiddenLayer(int numOfHiddenNeurons)
	{
		//only able to add hidden layers if weights are not created
		if (weights.size() == 0)
		{
			if (hiddenNeuronLayers.isEmpty())
			{
				//if this is the first hidden layer, create a new Neuron array
				//and make its Neurons point to input and output layers
				NeuronLayer hiddenLayer = new NeuronLayer(numOfHiddenNeurons, inputNeuronLayer, outputNeuronLayer);
				inputNeuronLayer.setNextLayer(hiddenLayer);
				outputNeuronLayer.setPreviousLayer(hiddenLayer);
				
				//add Neuron array to hiddenLayers and point inputNeurons
				hiddenNeuronLayers.add(hiddenLayer);
				return;
			}
			
			//there are already hidden layers, create a new Neuron array
			//and make its Neruons point to last hidden layer and output layer
			NeuronLayer lastHiddenLayer = hiddenNeuronLayers.get(hiddenNeuronLayers.size() - 1);
			NeuronLayer hiddenLayer = new NeuronLayer(numOfHiddenNeurons, lastHiddenLayer, outputNeuronLayer);
			lastHiddenLayer.setNextLayer(hiddenLayer);
			outputNeuronLayer.setPreviousLayer(hiddenLayer);
					
			//add hidden layer to hiddenLayers
			hiddenNeuronLayers.add(hiddenLayer);
		}
	}
	
	public void createWeights()
	{
		if (weights.size() == 0)
		{
			//creating weights in between inputNeuron and first hidden layer or output layer
			for (int i = 0; i < inputNeuronLayer.size(); i++)
				inputNeuronLayer.get(i).createWeights(weights);
			
			//creating weights in between 2 hidden layers or last hidden layer and ouput layer
			for (int i = 0; i < hiddenNeuronLayers.size(); i++)
			{
				NeuronLayer hiddenLayer = hiddenNeuronLayers.get(i);
				for (int j = 0; j < hiddenLayer.size(); j++)
					hiddenLayer.get(j).createWeights(weights);
			}
			
			//setting the weight in output neurons to the already created weights
			for (int i = 0; i < outputNeuronLayer.size(); i++)
				outputNeuronLayer.get(i).createWeights(weights);
		}
		
		//setting neuron count
		neuronCount = 0;
		neuronCount += inputNeuronLayer.size();
		for (int i = 0; i < hiddenNeuronLayers.size(); i++)
			neuronCount += hiddenNeuronLayers.get(i).size();
		neuronCount += outputNeuronLayer.size();
		
		//setting weight count
		weightCount = weights.size();
	}
	
	public void train(double[] input, double[] targetOutput)
	{
		//forward propagation: calculates neuron output values using the input, weight and bias values
		forwardPropagate(input);
		
		//updates all the weights and neuron biases based on target output
		backPropagate(targetOutput);
	}
	
	public void forwardPropagate(double[] input)
	{
		if (input.length != inputNeuronLayer.size() || weights.size() == 0)
			throw new IllegalArgumentException();
				
		//setting the input and output values for the input neurons
		for (int i = 0; i < inputNeuronLayer.size(); i++)
			inputNeuronLayer.get(i).setOutput(input[i]);
		
		//iterating through all the hidden layers
		for (NeuronLayer hiddenNeuronLayer: hiddenNeuronLayers)
		{
			//iterating through all the neurons in each hidden layer
			for (int j = 0; j < hiddenNeuronLayer.size(); j++)
			{
				//finding current neuron
				Neuron neuron = hiddenNeuronLayer.get(j);
				double neuronInput = neuron.getBias();
				//calculating current neuron input and output based off the weights and inputs feeding into it
				for (Weight weight: neuron.getPreviousWeights())
					neuronInput += weight.getValue() * weight.getPreviousNeuron().getOutput();
				
				//setting the neuron input
				neuron.setInput(neuronInput);
				//setting the neuron output using the input through the activation function
				neuron.setOutput(activation(neuronInput));
			}
		}
		
		for (int i = 0; i < outputNeuronLayer.size(); i ++)
		{
			//finding current neuron
			Neuron neuron = outputNeuronLayer.get(i);
			double neuronInput = neuron.getBias();
			//calculating current neuron input and output based off the weights and inputs feeding into it
			for (Weight weight: neuron.getPreviousWeights())
				neuronInput += weight.getValue() * weight.getPreviousNeuron().getOutput();
			
			//setting the neuron input
			neuron.setInput(neuronInput);
			//setting the neuron output using the input through the activation function
			neuron.setOutput(activation(neuronInput));
		}
	}
	
	private void backPropagate(double[] targetOutput)
	{		
		for (int i = 0; i < outputNeuronLayer.size(); i++)
			backPropagateOutputNeuron(targetOutput, outputNeuronLayer.get(i));
		
		
		//back propagating through all hidden layers and back to the targetNeuron
		for (int i = hiddenNeuronLayers.size() - 1; i >= 0; i--)
		{
			NeuronLayer neuronLayer = hiddenNeuronLayers.get(i);

			for (int j = 0; j < neuronLayer.size(); j++)
				backPropagateNeuron(neuronLayer.get(j));
		}
		
		//System.out.println(Arrays.toString(getOutput()) + ", " + Arrays.toString(targetOutput));
	}
	
	private void backPropagateOutputNeuron(double[] targetOutput, Neuron neuron)
	{
		neuron.setdErrorOverdOutput(errorDerivative(neuron.getOutput(), targetOutput[neuron.getIndex()]));
		neuron.setdErrorOverdInput(activationDerivative(neuron.getInput()) * neuron.getdErrorOverdOutput());
		neuron.setBias(neuron.getBias() - BIAS_LEARNING_RATE * neuron.getdErrorOverdInput());
	}
	
	private void backPropagateNeuron(Neuron neuron)
	{
		//setting dError/dOutput to the summation of next weights times the dError/dInput of the weights next neuron
		double neurondErrorOverdOuput = 0.0;
		for (Weight weight: neuron.getNextWeights())
		{
			Neuron nextNeuron = weight.getNextNeuron();
			
			weight.setdErrorOverdWeight(neuron.getOutput() * nextNeuron.getdErrorOverdInput());
			weight.setValue(weight.getValue() - LEARNING_RATE * weight.getdErrorOverdWeight());
			
			neurondErrorOverdOuput += weight.getValue() * nextNeuron.getdErrorOverdInput();
		}
		
		neuron.setdErrorOverdOutput(neurondErrorOverdOuput);
		neuron.setdErrorOverdInput(activationDerivative(neuron.getInput()) * neuron.getdErrorOverdOutput());
		neuron.setBias(neuron.getBias() - BIAS_LEARNING_RATE * neuron.getdErrorOverdInput());
	}
	
	public double[] getOutput()
	{
		double[] output = new double[outputNeuronLayer.size()];

		for (int i = 0; i < outputNeuronLayer.size(); i ++)
			output[i] = outputNeuronLayer.get(i).getOutput();
		
		return output;
	}
	
	public double getAccuracy(ArrayList<double[]> inputs, ArrayList<double[]> outputs)
	{
		int passes = 0;
		
		for (int i = 0; i < inputs.size(); i++)
		{
			//getting the prediction arrays from inputs array list and storing a copy of them
			forwardPropagate(inputs.get(i));
			double[] prediction = getOutput();
			prediction = Arrays.copyOf(prediction, prediction.length);

			//getting the output array
			//no copy needed as it will not be modified
			int outputIndex = maxIndex(outputs.get(i));
			
			//make sure the prediction is a valid move
			int predictionIndex = maxIndex(prediction);
			while (inputs.get(i)[predictionIndex] != DataSetInterpreter.INTERPRETED_EMPTY)
			{
				prediction[predictionIndex] = -Double.MAX_VALUE;
				predictionIndex = maxIndex(prediction);
			}

			//if prediction is valid count it as a pass
			if (predictionIndex == outputIndex)
				passes++;	
		}
		
		return (double) passes / inputs.size();
	}
	
	public double getMeanSquaredError(ArrayList<double[]> inputs, ArrayList<double[]> outputs)
	{
		double meanSquaredError = 0;
		
		for (int i = 0; i < inputs.size(); i++)
		{
			//getting the prediction arrays from inputs array list and storing a copy of them
			forwardPropagate(inputs.get(i));
			double[] prediction = getOutput();
			prediction = Arrays.copyOf(prediction, prediction.length);

			//System.out.println(Arrays.toString(prediction) + "\t" + Arrays.toString(outputs.get(i)));
			
			for (int j = 0; j < prediction.length; j++)
				meanSquaredError += error(outputs.get(i)[j], prediction[j]);
				
		}
		
		return 2 * meanSquaredError / inputs.size();
	}
	
	//GETTERS AND SETTERS
	public NeuronLayer getInputNeuronLayer() {
		return inputNeuronLayer;
	}

	public NeuronLayer getOutputNeuronLayer() {
		return outputNeuronLayer;
	}

	public ArrayList<NeuronLayer> getHiddenNeuronLayers() {
		return hiddenNeuronLayers;
	}
	
	//file input and output
	public void setData(String filePath)
	{
		ObjectInputStream binaryInput = null;
		
		try 
		{
			binaryInput = new ObjectInputStream(new FileInputStream(filePath));
			NeuralNetworkData data = (NeuralNetworkData) binaryInput.readObject();
			inputNeuronLayer = data.inputNeuronLayer;
			outputNeuronLayer = data.outputNeuronLayer;
			hiddenNeuronLayers = data.hiddenNeuronLayers;
			weights = data.weights;
			neuronCount = data.neuronCount;
			weightCount = data.weightCount;
			
			binaryInput.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void saveData(String filePath)
	{
		ObjectOutputStream binaryOutput = null;
		try 
		{
			binaryOutput = new ObjectOutputStream(new FileOutputStream(filePath));
			binaryOutput.writeObject(new NeuralNetworkData(inputNeuronLayer, outputNeuronLayer, hiddenNeuronLayers, weights, neuronCount, weightCount));
			
			binaryOutput.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//activation function
	public static double activation(double x)
	{
		return 1 / (1 + Math.exp(-x));
	}
	
	//derivative of activation function
	public static double activationDerivative(double x)
	{
		return activation(x) * (1 - activation(x));
	}
	
	//error function
	public static double error(double output, double targetOutput)
	{
		return 0.5 * Math.pow(output - targetOutput, 2.0);
	}
	
	//derivative of error function
	public static double errorDerivative(double output, double targetOutput)
	{
		return output - targetOutput;
	}
	
	//random normally distributed number
	public static double randomNormallyDistributedValue(double divisior)
	{
		Random random = new Random();
		return random.nextGaussian() / Math.sqrt(divisior);
	}
	
	//returns max index of array
	public static int maxIndex(double[] array)
	{
		int index = 0;
		double max = Double.MIN_VALUE;
		
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] > max)
			{
				max = array[i];
				index = i;
			}
		}
		return index;
	}
}
