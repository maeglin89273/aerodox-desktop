/**
 * 
 */
package io.aerodox.desktop.math;

/**
 * @author maeglin89273
 *
 */
public class MathUtility {
	
	public static final double EPSILON = 0.0000001;
	private static final float SLOWDOWN_TIME_CONST = 2.7f; 
	private static final float DECAY_CURVE_COEF = 2.2f;
	private MathUtility(){
	}
	
	public static double[][] getRotationMatrixFromVector(Vector3D rotationVector) {
		
		double[][] rotationMatrix = new double[3][3];
		
        double q0;
        double q1 = rotationVector.getX();
        double q2 = rotationVector.getY();
        double q3 = rotationVector.getZ();

        q0 = 1 - q1*q1 - q2*q2 - q3*q3;
        q0 = (q0 > 0) ? Math.sqrt(q0) : 0;
        
        double sq_q1 = 2 * q1 * q1;
        double sq_q2 = 2 * q2 * q2;
        double sq_q3 = 2 * q3 * q3;
        double q1_q2 = 2 * q1 * q2;
        double q3_q0 = 2 * q3 * q0;
        double q1_q3 = 2 * q1 * q3;
        double q2_q0 = 2 * q2 * q0;
        double q2_q3 = 2 * q2 * q3;
        double q1_q0 = 2 * q1 * q0;

        rotationMatrix[0][0] = 1 - sq_q2 - sq_q3;
        rotationMatrix[0][1] = q1_q2 - q3_q0;
        rotationMatrix[0][2] = q1_q3 + q2_q0;

        rotationMatrix[1][0] = q1_q2 + q3_q0;
        rotationMatrix[1][1] = 1 - sq_q1 - sq_q3;
        rotationMatrix[1][2] = q2_q3 - q1_q0;

        rotationMatrix[2][0] = q1_q3 - q2_q0;
        rotationMatrix[2][1] = q2_q3 + q1_q0;
        rotationMatrix[2][2] = 1 - sq_q1 - sq_q2;
        
        return rotationMatrix;
    }
	
	public static double[][] transposeMatrix(double[][] matrix) {
		double[][] transpose = new double[matrix.length][matrix[0].length];
		
		for (int i = 0; i < transpose.length; i++) {
			for (int j = 0; j < transpose.length; j++) {
				transpose[i][j] = matrix[j][i];
			}
			
		}
		
		return transpose;
	}
	
	public static double[][] multipyMatrices(double[][] mA, double[][] mB) {
		if (mA[0].length != mB.length) {
			return null;
		}
		
		int row = mA.length, col = mB[0].length, m = mB.length;
		double[][] result = new double[row][col];
		for (int aRowI = 0; aRowI < row; aRowI++) {
			for (int bColI = 0; bColI < col; bColI++) {
				for (int i = 0; i < m; i++) {
					result[aRowI][bColI] += mA[aRowI][i] * mB[i][bColI]; 
				}
			}
		}
		
		return result;
	}
	
	public static double slowdownFormula(double timeInSec, double v0) {
		double ratio = timeInSec / SLOWDOWN_TIME_CONST;
		double vt =  Math.signum(v0) * -1 * Math.pow(ratio, DECAY_CURVE_COEF) + v0;
		
		return vt * v0 > 0? vt: 0; 
	}
	
}
