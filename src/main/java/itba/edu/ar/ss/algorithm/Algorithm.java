package itba.edu.ar.ss.algorithm;

import itba.edu.ar.ss.model.entity.Entity;
import itba.edu.ar.ss.model.force.Force;

public interface Algorithm<T> {

	public void evolveEntity(Entity<T> entity,Force<T> force);
	
	public void evolveEntity(Entity<T> entity,Force<T> force,double finalTime);
	
	public void subscribe(AlgorithmObserver<T> subscriber);

	public String getName();
	
}
