package itba.edu.ar.ss.algorithm.impl;

import itba.edu.ar.ss.model.entity.Entity;
import itba.edu.ar.ss.model.entity.impl.Spring;
import itba.edu.ar.ss.model.force.Force;

public class GearPredictorCorrector extends AbstractAlgorithm<Double> {

	private Spring spring;

	private double a;
	private double b;
	private double c;
	private double d;
	private double e;
	private double f;

	double[] correctedR = new double[5];

	private static double[] alpha = new double[6];
	private static double[] factorial = new double[6];
	private double[] powDeltaTime = new double[6];

	static {
		alpha[0] = 3 / 20;
		alpha[1] = 251 / 360;
		alpha[2] = 1;
		alpha[3] = 11 / 18;
		alpha[4] = 1 / 6;
		alpha[5] = 1 / 60;

		factorial[0] = 1;
		factorial[1] = 1;
		factorial[2] = 2;
		factorial[3] = 6;
		factorial[4] = 24;
		factorial[5] = 120;

	}

	public GearPredictorCorrector(double deltaTime, Spring spring) {
		super(deltaTime);

		this.spring = spring;

		double powElascticConst2 = Math.pow(spring.getElasticConst(), 2);

		double powAbsortionConst2 = Math.pow(spring.getAbsortionConst(), 2);
		double powAbsortionConst3 = powAbsortionConst2 * spring.getAbsortionConst();

		double powMass2 = Math.pow(spring.getMass(), 2);
		double powMass3 = powMass2 * spring.getMass();

		a = powElascticConst2 / powMass2
				- powAbsortionConst2 * spring.getElasticConst() / Math.pow(spring.getMass(), 3);
		b = (2 * spring.getAbsortionConst() * spring.getElasticConst() / powMass2 - powAbsortionConst3 / powMass3);
		c = -spring.getElasticConst() / spring.getMass() + powAbsortionConst2 / powMass2;
		d = spring.getAbsortionConst() * spring.getElasticConst() / powMass2;
		e = -spring.getElasticConst() / spring.getMass();
		f = -spring.getAbsortionConst() / spring.getMass();

		powDeltaTime[0] = 1;
		for (int i = 1; i <= powDeltaTime.length - 1; i++) {
			powDeltaTime[i] = powDeltaTime[i - 1] * deltaTime;
		}

	}

	public void evolveEntity(Entity<Double> entity, Force<Double> force) {


		double[] r = getInitialR(entity, force);
		double[] predictedR = new double[6];

		predictedR[5] = r[5];
		predictedR[4] = r[4] + r[5] * deltaTime;
		predictedR[3] = r[3] + r[4] * deltaTime + r[5] * powDeltaTime[2] / 2;

		predictedR[2] = r[2] + r[3] * deltaTime + r[4] * powDeltaTime[2] / 2 + r[5] * powDeltaTime[3] / 6;
		predictedR[1] = r[1] + r[2] * deltaTime + r[3] * powDeltaTime[2] / 2 + r[4] * powDeltaTime[3] / 6
				+ r[5] * powDeltaTime[4] / 24;
		predictedR[0] = r[0] + r[1] * deltaTime + r[2] * powDeltaTime[2] / 2 + r[3] * powDeltaTime[3] / 6
				+ r[4] * powDeltaTime[4] / 24 + r[5] * powDeltaTime[5] / 120;

		setNewValues(entity, predictedR[0], predictedR[1]);

		double currentAceleration = force.getCurrentForce() / entity.getMass();

		double deltaA = currentAceleration - predictedR[2];
		double deltaR2 = deltaA * powDeltaTime[2] / 2;

		for (int i = 0; i < correctedR.length; i++) {
			correctedR[i] = predictedR[i] + alpha[i] * deltaR2 * factorial[i] / powDeltaTime[i];
		}

		setNewValues(entity, correctedR[0], correctedR[1]);

	}

	private boolean firstTime = true;

	private double[] getInitialR(Entity<Double> entity, Force<Double> force) {

		if (firstTime) {
			double[] r = new double[6];
			double currentAceleration = force.getCurrentForce() / entity.getMass();

			r[5] = a * spring.getVelocity() + b * force.getCurrentForce() / spring.getMass();
			r[4] = c * currentAceleration + d * entity.getVelocity();
			r[3] = e * spring.getVelocity() + f * currentAceleration;
			r[2] = currentAceleration;
			r[1] = spring.getVelocity();
			r[0] = spring.getPosition();
			
			return r;
		}

		return correctedR;

	}

	@Override
	public String getName() {
		return "GearPredictorCorrector";
	}
	
}
