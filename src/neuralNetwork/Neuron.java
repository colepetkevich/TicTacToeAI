package neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable
{
	private double bias;
	private double input;
	private double output;
	private double dEdI;
	private double dEdO;
	
	private Weight[] previousWeights;
	private Weight[] nextWeights;
	
	public Neuron()
	{
		bias = 0.0;
	}
	
	//GETTERS AND SETTERS
	public Weight[] getPreviousWeights() { return previousWeights; }
	public void setPreviousWeights(Weight[] previousWeights) { this.previousWeights = previousWeights; }

	public Weight[] getNextWeights() { return nextWeights; }
	public void setNextWeights(Weight[] nextWeights) { this.nextWeights = nextWeights; }

	public double getBias() { return bias;}
	public void setBias(double bias) { this.bias = bias; }

	public double getInput() { return input; }
	public void setInput(double input) { this.input = input; }

	public double getOutput() { return output; }
	public void setOutput(double output) { this.output = output; }

	public double getdEdI() { return dEdI; }
	public void setdEdI(double dEdI) { this.dEdI = dEdI; }

	public double getdEdO() { return dEdO; }
	public void setdEdO(double dEdO) { this.dEdO = dEdO; } 
}
