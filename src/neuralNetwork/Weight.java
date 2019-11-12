package neuralNetwork;

import java.io.Serializable;

public class Weight implements Serializable
{
	private double value;
	
	public final Neuron previousNeuron;
	public final Neuron nextNeuron;
	
	public Weight(Neuron previousNeuron, Neuron nextNeuron)
	{		
		this.previousNeuron = previousNeuron;
		this.nextNeuron = nextNeuron;
	}

	//GETTERS AND SETTERS
	public double getValue() { return value; }
	public void setValue(double value) { this.value = value; }
}
