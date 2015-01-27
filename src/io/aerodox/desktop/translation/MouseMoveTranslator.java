/**
 * 
 */
package io.aerodox.desktop.translation;

import java.awt.MouseInfo;
import java.awt.Point;

import io.aerodox.desktop.imitation.Environment;
import io.aerodox.desktop.imitation.Performer;
import io.aerodox.desktop.math.MathUtility;
import io.aerodox.desktop.math.Vector2D;
import io.aerodox.desktop.math.Vector3D;
import io.aerodox.desktop.service.ConfigurationService;


/**
 * @author maeglin89273
 *
 */
public class MouseMoveTranslator implements ActionTranslator {
	
	@Override
	public Action translate(Arguments args) {

		Vector3D rotVec = args.getAsVector3D("gyro");
		double threshold = ConfigurationService.getInstance().getRotationThreshold();
		if (rotVec.getSquare() <= threshold * threshold) {
			return null;
		}
				
		return new MouseMoveAction(MathUtility.getRotationMatrixFromVector(rotVec));
	}
	
	
	
	private static class MouseMoveAction implements Action {
		
		private double[] rotMat;
		private MouseMoveAction(double[] rotMat) {
			this.rotMat = rotMat;
		}
		
		@Override
		public Object perform(Performer performer, Environment env) {
			ConfigurationService config = ConfigurationService.getInstance();
			Vector3D pointer = env.getPointerReference();
			Vector3D ray = toRay(pointer.clone().applyMatrix(this.rotMat), config);
			this.restrictPointer(pointer, ray, config);
			performer.mouseMove(ray.projectToPlane(config.getScreenPlane()));
			return null;
		}
		
		private void restrictPointer(Vector3D pointer, Vector3D ray, ConfigurationService config) {
			Vector3D origin = config.getScreenPlane().getOrigin();
			double leftX = origin.getX();
			double rightX = leftX + ConfigurationService.getScreenWidth();
			double topZ = origin.getZ();
			double bottomZ = topZ - ConfigurationService.getScreenHeight();
			
			if (ray.getX() < leftX) {
				ray.setX(leftX);
			} else if (ray.getX() > rightX) {
				ray.setX(rightX);
			}
			
			if (ray.getZ() > topZ) {
				ray.setZ(topZ);
			} else if (ray.getZ() < bottomZ) {
				ray.setZ(bottomZ);
			}
			
			pointer.set(ray).normalized();
		}

		private Vector3D toRay(Vector3D pointer, ConfigurationService config) {
			
			double scale = config.getDistance() / pointer.getY();
			pointer.mutiply(scale);
			
			return pointer;
		}
		
	}
	
}
