package itba.edu.ar.ss.algorithm;

import java.util.Collection;
import java.util.List;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.ss.model.entity.Entity;
import itba.edu.ar.ss.model.entity.impl.AstronomicalObject;
import itba.edu.ar.ss.model.force.Force;

public interface Algorithm<T> {

	public void evolveEntity(Entity<T> entity,Force<T> force);
	
	public void evolveEntity(Entity<T> entity,Force<T> force,double finalTime);
	
	public void evolveSystem(Collection<Entity<T>> entities, List<Force<T>> forces, double finaltime);
	
	public void subscribe(AlgorithmObserver<T> subscriber);

	public String getName();

	
}
