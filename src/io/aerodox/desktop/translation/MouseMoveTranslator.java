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
	private static final float MAG1 = 5;
	private static final float MAG2 = 2;
	private static final float THRESHOLD = 0.7f;
	

	@Override
	public Action translate(Arguments args) {
//		Vector3D gyro = args.getAsVector3D("gyro");
//		gyro.negateX();
//		gyro.negateZ();
//		gyro.mutiply(MAG1);
//		
//		acc.negateX();
//		acc.mutiply(MAG1 * ConfigurationService.getInstance().getSensitivity());
//		acc = adjustAcc(acc);
		
		Vector3D rotVec = args.getAsVector3D("rotVec");
		Vector2D pos = projectToScreen(getRotationMatrixFromVector(rotVec));
//		
		return new MouseMoveAction(pos);
//		return new MouseMoveAction(new Vector2D(gyro.getZ(), gyro.getX()));
	}
	
	private Vector2D projectToScreen(float[] rotMat) {
		Vector3D ray = initPointer();
		ray.applyMatrix(rotMat);
		ConfigurationService config = ConfigurationService.getInstance();
		float scale = config.getDistance() / ray.getY();
		ray.mutiply(scale * config.getProjectScale());
		
		return ray.projectToPlane(config.getScreenPlane());
	}

	private static Vector3D initPointer() {
		return new Vector3D(0, 1, 0);
	}
	private Vector3D adjustAcc(Vector3D vec) {
		
		vec.minus(0, 0, 0.5f);
		vec.set(filterValue(vec.getX()), filterValue(vec.getY()), filterValue(vec.getZ()));
//		float aX = Math.abs(vec.getX()), aY = Math.abs(vec.getY()), aZ = Math.abs(vec.getZ());
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
	
	private float filterValue(float v) {
		return Math.abs(v) <= THRESHOLD? 0 : v;
	}
	
	private static float[] getRotationMatrixFromVector(Vector3D rotationVector) {
		
		float[] rotationMatrix = new float[9];
		
        float q0;
        float q1 = rotationVector.getX();
        float q2 = rotationVector.getY();
        float q3 = rotationVector.getZ();

        q0 = 1 - q1*q1 - q2*q2 - q3*q3;
        q0 = (q0 > 0) ? (float)Math.sqrt(q0) : 0;

        float sq_q1 = 2 * q1 * q1;
        float sq_q2 = 2 * q2 * q2;
        float sq_q3 = 2 * q3 * q3;
        float q1_q2 = 2 * q1 * q2;
        float q3_q0 = 2 * q3 * q0;
        float q1_q3 = 2 * q1 * q3;
        float q2_q0 = 2 * q2 * q0;
        float q2_q3 = 2 * q2 * q3;
        float q1_q0 = 2 * q1 * q0;

        rotationMatrix[0] = 1 - sq_q2 - sq_q3;
        rotationMatrix[1] = q1_q2 - q3_q0;
        rotationMatrix[2] = q1_q3 + q2_q0;

        rotationMatrix[3] = q1_q2 + q3_q0;
        rotationMatrix[4] = 1 - sq_q1 - sq_q3;
        rotationMatrix[5] = q2_q3 - q1_q0;

        rotationMatrix[6] = q1_q3 - q2_q0;
        rotationMatrix[7] = q2_q3 + q1_q0;
        rotationMatrix[8] = 1 - sq_q1 - sq_q2;
        
        return rotationMatrix;
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
			performer.mouseMove(acc);
			return null;
		}
		
		
	}

}
