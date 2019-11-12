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

	private ArrayList<Neuron[]> neuronLayers;
	
	public NeuralNetwork(int... layerCounts)
	{
        //making sure enough layers are added
        if (layerCounts.length < 2)
            throw new IllegalArgumentException("must be 2 or more layers");
        
        neuronLayers = new ArrayList<Neuron[]>(layerCounts.length);
        
        for (int i = 0; i < layerCounts.length; i++)
        	neuronLayers.add(new Neuron[layerCounts[i]]);
        
        //creating neurons
        for (Neuron[] neuronLayer: neuronLayers)
        	for (int i = 0; i < neuronLayer.length; i++)
        		neuronLayer[i] = new Neuron();
        
        //creating weights
        for (int i = 0; i < neuronLayers.size() - 1; i++)
        {
        	Neuron[] previousLayer = neuronLayers.get(i);
        	Neuron[] nextLayer = neuronLayers.get(i + 1);
        	
        	for (Neuron previousNeuron: previousLayer)
        	{
        		Weight[] weights = new Weight[nextLayer.length];
        		for (int j = 0; j < weights.length; j++)
        		{
        			Neuron nextNeuron = nextLayer[j];
        			Weight weight = new Weight(previousNeuron, nextNeuron);
        			weight.setValue(randomNormallyDistributedValue(previousLayer.length));
        			weights[j] = weight;
        		}
        		previousNeuron.setNextWeights(weights);
        	}
        	
        	int neuronIndex = 0;
        	for (Neuron nextNeuron: nextLayer)
        	{
        		Weight[] weights = new Weight[previousLayer.length];
        		for (int j = 0; j < weights.length; j++)
        		{
        			Neuron previousNeuron = previousLayer[j];
        			weights[j] = previousNeuron.getNextWeights()[neuronIndex];
        		}
        		nextNeuron.setPreviousWeights(weights);
        		
        		neuronIndex++;
        	}
        }
	}

	//file io constructor get NeuralNetworkData from a binary file
	public NeuralNetwork(String filePath)
	{
		setData(filePath);
	}

	public void train(double[] input, double[] targetOutput)
	{
		//forward propagation: calculates neuron output values using the input, weight and bias values
		forwardPropagate(input);

		//updates all the weights and neuron biases based on target output
		backPropagate(targetOutput);
	}
	
	public double[] calculate(double[] input)
	{
		forwardPropagate(input);
		
		Neuron[] outputLayer = neuronLayers.get(neuronLayers.size() - 1);
		double[] output = new double[outputLayer.length];

		for (int i = 0; i < output.length; i ++)
			output[i] = outputLayer[i].getOutput();

		return output;
	}

	private void forwardPropagate(double[] input)
	{
		if (input.length != neuronLayers.get(0).length)
			throw new IllegalArgumentException();
		
		//setting the output of all neurons in input layer
		for (int i = 0; i < neuronLayers.get(0).length; i++)
			neuronLayers.get(0)[i].setOutput(input[i]);
		
		//forward propagating through all other neuron layers
		for (int i = 1; i < neuronLayers.size(); i++)
		{
			Neuron[] neuronLayer = neuronLayers.get(i);
			for (Neuron neuron: neuronLayer)
			{
				//calculating neuron input
				double neuronInput = neuron.getBias();
				for (Weight weight: neuron.getPreviousWeights())
					neuronInput += weight.getValue() * weight.previousNeuron.getOutput();
				
				//setting the neuron's input and output
				neuron.setInput(neuronInput);
				neuron.setOutput(activation(neuronInput));
			}
		}
	}

	private void backPropagate(double[] targetOutput)
	{		
		int outputNeuronLayerIndex = neuronLayers.size() - 1;
		for (int layerIndex = outputNeuronLayerIndex; layerIndex >= 0; layerIndex--)
		{
			Neuron[] neuronLayer = neuronLayers.get(layerIndex);
			for (int i = 0; i < neuronLayer.length; i++)
			{
				Neuron neuron = neuronLayer[i];
				
				//if neuron is in the output layer then use target output to calculate dError / dOutput
				if (layerIndex == outputNeuronLayerIndex)
					neuron.setdEdO(errorDerivative(neuron.getOutput(), targetOutput[i]));
				//otherwise backpropagate normally
				else
				{
					double dEdO = 0.0;
					for (Weight weight: neuron.getNextWeights())
					{
					//	Weight weight = nextWeights[k];
						Neuron nextNeuron = weight.nextNeuron;
						
						double dEdW = neuron.getOutput() * nextNeuron.getdEdI();
						weight.setValue(weight.getValue() - LEARNING_RATE * dEdW);
						
						dEdO += weight.getValue() * weight.nextNeuron.getdEdI();
					}

					neuron.setdEdO(dEdO);
				}
					
				neuron.setdEdI(activationDerivative(neuron.getInput()) * neuron.getdEdO());
				neuron.setBias(neuron.getBias() - BIAS_LEARNING_RATE * neuron.getdEdI());
			}
		}
	}

	public double getAccuracy(ArrayList<double[]> inputs, ArrayList<double[]> outputs)
	{
		int passes = 0;

		for (int i = 0; i < inputs.size(); i++)
		{
			//getting the prediction arrays from inputs array list and storing a copy of them
			double[] prediction = calculate(inputs.get(i));
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
			double[] prediction = calculate(inputs.get(i));
			prediction = Arrays.copyOf(prediction, prediction.length);

			//System.out.println(Arrays.toString(prediction) + "\t" + Arrays.toString(outputs.get(i)));

			for (int j = 0; j < prediction.length; j++)
				meanSquaredError += error(outputs.get(i)[j], prediction[j]);

		}

		return 2 * meanSquaredError / inputs.size();
	}

	//file input and output
	public void setData(String filePath)
	{
		ObjectInputStream binaryInput = null;

		try 
		{
			binaryInput = new ObjectInputStream(new FileInputStream(filePath));
			neuronLayers = (ArrayList<Neuron[]>) binaryInput.readObject();

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
			binaryOutput.writeObject(neuronLayers);

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