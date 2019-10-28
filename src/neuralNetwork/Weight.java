package neuralNetwork;

import java.io.Serializable;

public class Weight implements Serializable
{
	private Double value;
	private Double dErrorOverdWeight;
	
	private Neuron previousNeuron;
	private Neuron nextNeuron;
	
	public Weight(double divisor, Neuron previousNeuron, Neuron nextNeuron)
	{		
		this.previousNeuron = previousNeuron;
		this.nextNeuron = nextNeuron;
		
		//value is set to a random normally distributed value
		value = NeuralNetwork.randomNormallyDistributedValue(divisor);
	}
	
	public boolean equals(Weight other)
	{
		return value == other.value && previousNeuron.equals(other.previousNeuron) && nextNeuron.equals(other.nextNeuron);
	}

	//GETTERS AND SETTERS
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public Double getdErrorOverdWeight() {
		return dErrorOverdWeight;
	}

	public void setdErrorOverdWeight(double dErrorOverdWeight) {
		this.dErrorOverdWeight = dErrorOverdWeight;
	}

	public Neuron getPreviousNeuron() {
		return previousNeuron;
	}

	public Neuron getNextNeuron() {
		return nextNeuron;
	}
}
