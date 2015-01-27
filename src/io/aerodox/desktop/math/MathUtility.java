/**
 * 
 */
package io.aerodox.desktop.math;

/**
 * @author maeglin89273
 *
 */
public class MathUtility {

	private MathUtility(){
	}
	
	public static double[] getRotationMatrixFromVector(Vector3D rotationVector) {
		
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
	
	public static double[] transpose3x3Matrix(double[] matrix) {
		double[] transpose = new double[matrix.length];
		transpose[0] = matrix[0];
		transpose[1] = matrix[3];
		transpose[2] = matrix[6];
		transpose[3] = matrix[1];
		transpose[4] = matrix[4];
		transpose[5] = matrix[7];
		transpose[6] = matrix[2];
		transpose[7] = matrix[5];
		transpose[8] = matrix[8];
		
		return transpose;
	}
}
