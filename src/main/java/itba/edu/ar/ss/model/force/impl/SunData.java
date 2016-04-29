package itba.edu.ar.ss.model.force.impl;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.input.file.data.Data;

public class SunData extends Data{

	private static FloatPoint velocity = new FloatPoint(0, 0);
	
	public SunData(int particleQuantity, double mass, double radio,FloatPoint position) {
		super(particleQuantity, mass, radio,velocity,position);
	}
	
	
}
