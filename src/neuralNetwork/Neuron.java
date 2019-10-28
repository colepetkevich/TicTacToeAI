package neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable
{
	private Double bias;
	private Double input;
	private Double output;
	private Double dErrorOverdInput;
	private Double dErrorOverdOutput;

	private Integer index;
	private NeuronLayer parentLayer;
	
	private Weight[] previousWeights;
	private Weight[] nextWeights;
	
	private Boolean weightsCreated;
	
	public Neuron(int index, NeuronLayer parentLayer)
	{
		bias = 0.0;
		
		this.index = index;
		this.parentLayer = parentLayer;
		
		weightsCreated = false;
	}

	public void createWeights(ArrayList<Weight> weights)
	{
		if (!weightsCreated)
		{
			weightsCreated = true;
			
			//make sure this layer is not the input layer
			NeuronLayer previousLayer = parentLayer.getPreviousLayer();
			if (previousLayer != null)
			{
				//sets previous weights back to the already created weights from neurons of previous layer
				previousWeights = new Weight[previousLayer.size()];
				for (int i = 0; i < previousWeights.length; i++)
				{
					previousWeights[i] = previousLayer.get(i).getNextWeights()[index];
				}
			}
			
			//make sure this layer is not the output layer
			NeuronLayer nextLayer = parentLayer.getNextLayer();
			if (nextLayer != null)
			{
				//creates weights and sets its previous neuron to this neuron and its next neuron to the 
				nextWeights = new Weight[nextLayer.size()];
				for (int i = 0; i < nextWeights.length; i++)
				{ 
					Neuron nextNeuron = nextLayer.get(i);
					nextWeights[i] = new Weight(parentLayer.size(), this, nextNeuron);
					weights.add(nextWeights[i]);
				}
			}
		}
	}
	
	public boolean equals(Neuron other)
	{
		return input == other.input && output == other.output && index == other.index;
	}

	//GETTERS AND SETTERS
	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}
	
	public int getIndex() {
		return index;
	}

	public Weight[] getPreviousWeights() {
		return previousWeights;
	}

	public Weight[] getNextWeights() {
		return nextWeights;
	}
	
	public double getInput() {
		return input;
	}

	public void setInput(double input) {
		this.input = input;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public double getdErrorOverdInput() {
		return dErrorOverdInput;
	}

	public void setdErrorOverdInput(double dErrorOverdInput) {
		this.dErrorOverdInput = dErrorOverdInput;
	}

	public double getdErrorOverdOutput() {
		return dErrorOverdOutput;
	}

	public void setdErrorOverdOutput(double dErrorOverdOutput) {
		this.dErrorOverdOutput = dErrorOverdOutput;
	}
	
	public NeuronLayer getParentLayer()
	{
		return parentLayer;
	}
}
