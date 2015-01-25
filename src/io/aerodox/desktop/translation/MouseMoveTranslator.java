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
	private static final double THRESHOLD = 0.7f;
	

	@Override
	public Action translate(Arguments args) {
//		Vector3D gyro = args.getAsVector3D("acc");
//		acc.negateX();
//		acc.mutiply(MAG1 * ConfigurationService.getInstance().getSensitivity());
//		acc = adjustAcc(acc);
		
		Vector3D rotVec = args.getAsVector3D("rot");
		Vector2D pos = projectToScreen(getRotationMatrixFromVector(rotVec));
		
		return new MouseMoveAction(pos);
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
		
		vec.minus(0, 0, 0.5f);
		vec.set(filterValue(vec.getX()), filterValue(vec.getY()), filterValue(vec.getZ()));
//		
		return vec;

	}
	
	private double filterValue(double v) {
		return Math.abs(v) <= THRESHOLD? 0 : v;
	}
	
	private static double[] getRotationMatrixFromVector(Vector3D rotationVector) {
		
		double[] rotationMatrix = new double[9];
		
        double q0;
        double q1 = rotationVector.getX();
        double q2 = rotationVector.getY();
        double q3 = rotationVector.getZ();

        q0 = 1 - q1*q1 - q2*q2 - q3*q3;
        q0 = (q0 > 0) ? (double)Math.sqrt(q0) : 0;

        double sq_q1 = 2 * q1 * q1;
        double sq_q2 = 2 * q2 * q2;
        double sq_q3 = 2 * q3 * q3;
        double q1_q2 = 2 * q1 * q2;
        double q3_q0 = 2 * q3 * q0;
        double q1_q3 = 2 * q1 * q3;
        double q2_q0 = 2 * q2 * q0;
        double q2_q3 = 2 * q2 * q3;
        double q1_q0 = 2 * q1 * q0;

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
		
		Vector2D pos;
		private static final double SLOWER_FACTOR = 0.1;
		private MouseMoveAction(Vector2D pos) {
			this.pos = pos;
		}
		
		@Override
		public Object perform(Performer performer, Environment env) {
//			Vector2D curPos = env.getMousePosition();
//			Vector2D delta = slowDown(pos, curPos);
//			if (!isVecTooSmall(delta)) {
//				performer.mouseMove(curPos.add(delta));
//			}
			performer.mouseMove(pos);
			return null;
		}
		
		private Vector2D slowDown(Vector2D pos, Vector2D curPos) { 
			Vector2D delta = Vector2D.minus(pos, curPos);
			return delta.mutiply(SLOWER_FACTOR);
			
		}
		
		private boolean isVecTooSmall(Vector2D vec) {
			return (int)vec.getX() == 0 && (int)vec.getY() == 0;
				
		}
	}

}
