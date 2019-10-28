package neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;

public class NeuralNetworkData implements Serializable
{
	public NeuronLayer inputNeuronLayer;
	public NeuronLayer outputNeuronLayer;
	public ArrayList<NeuronLayer> hiddenNeuronLayers;
	public ArrayList<Weight> weights;
	public int neuronCount;
	public int weightCount;
	
	public NeuralNetworkData(NeuronLayer inputNeuronLayer, NeuronLayer outputNeuronLayer, ArrayList<NeuronLayer> hiddenNeuronLayers, ArrayList<Weight> weights, int neuronCount, int weightCount)
	{
		this.inputNeuronLayer = inputNeuronLayer;
		this.outputNeuronLayer = outputNeuronLayer;
		this.hiddenNeuronLayers = hiddenNeuronLayers;
		this.weights = weights;
		this.neuronCount = neuronCount;
		this.weightCount = weightCount;
	}
}
