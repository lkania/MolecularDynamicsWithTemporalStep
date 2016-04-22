package itba.edu.ar.ss.data.system;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.input.file.data.Data;

public class AstronomicalObjectData extends Data{

	public AstronomicalObjectData(int particleQuantity, double mass, double radio) {
		super(particleQuantity, mass, radio);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public FloatPoint getVelocity(double positionX, double positionY) {
		// TODO Auto-generated method stub
		return super.getVelocity(positionX, positionY);
	}
	
	
}