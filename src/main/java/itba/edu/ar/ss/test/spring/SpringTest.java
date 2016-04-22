package itba.edu.ar.ss.test.spring;

import itba.edu.ar.ss.algorithm.Algorithm;
import itba.edu.ar.ss.algorithm.AlgorithmObserver;
import itba.edu.ar.ss.algorithm.impl.BeemanForSpring;
import itba.edu.ar.ss.algorithm.impl.GearPredictorCorrector;
import itba.edu.ar.ss.algorithm.impl.VerletVelocityForSpring;
import itba.edu.ar.ss.data.entity.impl.Spring;
import itba.edu.ar.ss.data.force.Force;
import itba.edu.ar.ss.data.force.impl.SpringForce;
import itba.edu.ar.ss.output.SpringError;
import itba.edu.ar.ss.output.SpringPositionVsTime;

public class SpringTest {

	public static void main(String[] args) {
		double mass = 70;
		double elasticConst = Math.pow(10, 4);
		double absortionConst = 100;
		double position = 1;
		double velocity = (-1)*absortionConst / (mass * 2);
		Spring spring = new Spring(mass, elasticConst, absortionConst, position, velocity);
		
		
		double deltaTime = 0.0001;
		double finalTime = 5;

		String path = System.getProperty("user.dir")+"/";		
		
		//Algorithm algorithm = new VerletVelocityForSpring(deltaTime, spring);
		Algorithm<Double> algorithm = new BeemanForSpring(deltaTime,spring);
		//Algorithm<Double> algorithm = new GearPredictorCorrector(deltaTime,spring);
		Force<Double> force = new SpringForce(spring,deltaTime);
		
		AlgorithmObserver<Double> positionVsTime = new SpringPositionVsTime(path);
		AlgorithmObserver<Double> error = new SpringError(path, algorithm.getName(), spring, deltaTime, finalTime);
		
		algorithm.subscribe(positionVsTime);
		algorithm.subscribe(error);
		algorithm.evolveEntity(spring, force, finalTime);
	}

}
