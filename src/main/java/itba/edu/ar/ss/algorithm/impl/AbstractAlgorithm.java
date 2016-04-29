package itba.edu.ar.ss.algorithm.impl;

import java.util.LinkedList;
import java.util.List;

import itba.edu.ar.ss.algorithm.Algorithm;
import itba.edu.ar.ss.algorithm.AlgorithmObserver;
import itba.edu.ar.ss.model.entity.Entity;
import itba.edu.ar.ss.model.force.Force;

public abstract class AbstractAlgorithm<T> implements Algorithm<T> {

	protected double deltaTime;
	protected List<AlgorithmObserver<T>> subscribers = new LinkedList<AlgorithmObserver<T>>();

	public AbstractAlgorithm(double deltaTime) {
		super();
		this.deltaTime = deltaTime;
	}

	protected void setNewValues(Entity<T> entity, T newPosition, T newVelocity) {
		entity.setPosition(newPosition);
		entity.setVelocity(newVelocity);
	}

	public void subscribe(AlgorithmObserver<T> subscriber) {
		this.subscribers.add(subscriber);
	}

	protected void notifyDeltaTimeStepDone(Entity<T> entity, double time) {
		for (AlgorithmObserver<T> subscriber : subscribers)
			subscriber.deltaTimeStepDone(entity, time);
	}

	protected void notifySimulationEnded() {
		for (AlgorithmObserver<T> subscriber : subscribers)
			subscriber.simulationEnded();
	}
	
	public void evolveEntity(Entity<T> entity, Force<T> force, double finaltime) {
		for(double time=0;time<finaltime;time+=deltaTime)
		{
			evolveEntity(entity, force);
			notifyDeltaTimeStepDone(entity,time);
		}
		notifySimulationEnded();
	}
	
	public void evolveSystem(List<Entity<T>> entities, List<Force<T>> forces, double finaltime){
		if(entities.size()!=forces.size())
			throw new IllegalStateException();
		
		for(int i=0;i<entities.size();i++){
			evolveEntity(entities.get(i), forces.get(i), finaltime);
		}
	}
	
	public String getName() {
		return null;
	}


}
