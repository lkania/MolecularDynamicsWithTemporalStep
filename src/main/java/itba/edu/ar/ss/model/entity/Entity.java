package itba.edu.ar.ss.model.entity;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;

public interface Entity<T> {

	public T getPosition();
	
	public double getMass();
	
	public T getVelocity();

	public void setPosition(T position);
	
	public void setVelocity(T velocity);

	public T getPreviousPosition();

	
}
