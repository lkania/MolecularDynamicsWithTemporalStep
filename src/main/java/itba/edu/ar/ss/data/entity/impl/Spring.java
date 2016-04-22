package itba.edu.ar.ss.data.entity.impl;

import itba.edu.ar.ss.data.entity.Entity;

public class Spring implements Entity<Double>{

	private double mass;
	private double elasticConst;
	private double absortionConst;
	private double position;
	private double velocity;

	public Spring(double mass, double elasticConst, double absortionConst, double position, double velocity) {
		super();
		this.mass = mass;
		this.elasticConst = elasticConst;
		this.absortionConst = absortionConst;
		this.position = position;
		this.velocity = velocity;

	}

	public double getMass() {
		return mass;
	}

	public double getElasticConst() {
		return elasticConst;
	}

	public double getAbsortionConst() {
		return absortionConst;
	}

	public Double getPosition() {
		return position;
	}

	public void setPosition(Double position) {
		this.position = position;
	}

	public Double getVelocity() {
		return velocity;
	}

	public void setVelocity(Double velocity) {
		this.velocity = velocity;
	}


	public Double getPreviousPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
