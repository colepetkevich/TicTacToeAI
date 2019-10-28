package neuralNetwork;

import java.io.Serializable;

public class NeuronLayer implements Serializable
{
	private Neuron[] neuronLayer;
	
	private NeuronLayer previousLayer;
	private NeuronLayer nextLayer;
	
	public NeuronLayer(int size)
	{
		neuronLayer = new Neuron[size];
		for (int i = 0; i < size(); i++)
			neuronLayer[i] = new Neuron(i, this);
	}
	
	public NeuronLayer(int size, NeuronLayer previousLayer, NeuronLayer nextLayer)
	{
		neuronLayer = new Neuron[size];
		for (int i = 0; i < size(); i++)
			neuronLayer[i] = new Neuron(i, this);
		
		this.previousLayer = previousLayer;
		this.nextLayer = nextLayer;
	}
	
	public Neuron get(int index)
	{
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		
		return neuronLayer[index];
	}
	
	public int size()
	{
		return neuronLayer.length;
	}

	//GETTERS AND SETTERS
	public NeuronLayer getPreviousLayer() {
		return previousLayer;
	}

	public void setPreviousLayer(NeuronLayer previousLayer) {
		this.previousLayer = previousLayer;
	}

	public NeuronLayer getNextLayer() {
		return nextLayer;
	}

	public void setNextLayer(NeuronLayer nextLayer) {
		this.nextLayer = nextLayer;
	}
}
