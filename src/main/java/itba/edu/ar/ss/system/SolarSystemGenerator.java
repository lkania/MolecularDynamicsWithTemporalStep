package itba.edu.ar.ss.system;

import java.util.LinkedList;
import java.util.List;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.input.file.CellIndexMethodFileGenerator;
import itba.edu.ar.input.file.data.Data;
import itba.edu.ar.ss.system.data.SolarSystemData;

public class SolarSystemGenerator {

	public static void generate(SolarSystemData data, String path, List<String> staticPaths,
			List<String> dynamicPaths) {

		double length = data.getSpaceLength();

		FloatPoint sunPosition = data.getSun().getPosition();

		double astronomicalObjectMass = data.getSunMass() / data.getAstronomicalObjectsQuantity();
		
		AstronomicalObjectData astronomicalObjects = new AstronomicalObjectData(data.getAstronomicalObjectsQuantity(),
				astronomicalObjectMass, sunPosition, data.getAngularMoment());

		List<Data> datas = new LinkedList<Data>();
		datas.add(astronomicalObjects);

		CellIndexMethodFileGenerator.generate(staticPaths, dynamicPaths, length, datas, path);

	}

}
