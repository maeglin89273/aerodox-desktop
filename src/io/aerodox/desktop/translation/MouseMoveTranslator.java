/**
 * 
 */
package io.aerodox.desktop.translation;

import java.awt.MouseInfo;
import java.awt.Point;

import io.aerodox.desktop.imitation.Environment;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.ConfigurationService;


/**
 * @author maeglin89273
 *
 */
public class MouseMoveTranslator implements SubTranslator {
	private static final double MAG1 = 5;
	private static final double MAG2 = 2;
	private static final double THRESHOLD = 0.7;
//	private static final Vector3D ORIGIN = new Vector3D();
//	private static final Vector3D E1;
//	private static final Vector3D E2;
	
	@Override
	public Action translate(Arguments args) {
		Vector3D gyro = args.getAsVector3D("gyro");
//		double[] rotMat = args.getDoubleArray("rotMat");
		
		gyro.negateX();
		gyro.negateZ();
		gyro.mutiply(MAG1);
//		acc.negateX();
//		acc.mutiply(MAG1 * ConfigurationService.getInstance().getSensitivity());
//		acc = adjustAcc(acc);
		
//		Vector2D pos = projectToScreen(rotMat);
		
//		return new MouseMoveAction(pos);
		return new MouseMoveAction(new Vector2D(gyro.getZ(), gyro.getX()));
	}
	
	private Vector2D projectToScreen(double[] rotMat) {
		Vector3D ray = initPointer();
		ray.applyMatrix(rotMat);
		ConfigurationService config = ConfigurationService.getInstance();
		double scale = config.getDistance() / ray.getY();
		ray.mutiply(scale);
		
		return ray.projectToPlane(config.getScreenPlane());
	}

	private static Vector3D initPointer() {
		return new Vector3D(0, 1, 0);
	}
	private Vector3D adjustAcc(Vector3D vec) {
		
		vec.minus(0, 0, 0.5);
		vec.set(filterValue(vec.getX()), filterValue(vec.getY()), filterValue(vec.getZ()));
//		double aX = Math.abs(vec.getX()), aY = Math.abs(vec.getY()), aZ = Math.abs(vec.getZ());
//		if (aX > aY && aX > aZ) {
//			return new Vector3D(vec.getX(), 0, 0);
//		}
//		
//		if (aY > aX && aY > aZ) {
//			return new Vector3D(0, vec.getY(), 0);
//		}
//		
//		if (aZ > aX && aZ > aY) {
//			return new Vector3D(0, 0, vec.getZ());
//		}
		return vec;
//		return new Vector3D();
		
//		
//		vec.mutiply(MAG1);
//		vec.round();
//		vec.mutiply(MAG2);
	}
	
	private double filterValue(double v) {
		return Math.abs(v) <= THRESHOLD? 0 : v;
	}
	
	private class MouseMoveAction implements Action {
		
		Vector2D acc;
		
		private MouseMoveAction(Vector2D acc) {
			this.acc = acc;
		}
		
		@Override
		public Object perform(Performer performer, Environment env) {
//			System.out.println("performing move x:" + this.pos.getX() + " y:" + this.pos.getY());
//			Vector3D vel = env.getVelocityReference().add(this.acc);
//			System.out.println(vel);
//			performer.mouseMove(acc.add(env.getMousePosition()));
			performer.mouseMove(acc.add(env.getMousePosition()));
			return null;
		}
		
		
	}

}
