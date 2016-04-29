package itba.edu.ar.ss.algorithm;

import java.util.List;

import itba.edu.ar.ss.model.entity.Entity;

public interface AlgorithmObserver<T> {

	public void deltaTimeStepDone(Entity<T> entity,double time);
	
	public void simulationEnded();
	
}
