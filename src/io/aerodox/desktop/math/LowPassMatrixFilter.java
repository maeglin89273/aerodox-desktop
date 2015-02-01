package io.aerodox.desktop.math;



/**
 * Created by maeglin89273 on 1/29/15.
 */
public class LowPassMatrixFilter {

    public static final float ALPHA_DEFAULT = 0.693f;
    
    private double[][] lowPassedVals;
  
    private float alpha;
    private float oneMinusAlpha;
    
    private int stableCount;
    private int warmUpCount;
    private boolean stable;
    
    public LowPassMatrixFilter() {
        this(3, 3, ALPHA_DEFAULT);
    }

    public LowPassMatrixFilter(int rowCount, int colCount, float alpha) {
        this.lowPassedVals = new double[rowCount][colCount];
        reset(alpha);
    }

    public double[][] filter(long timestamp, double[][] newInput) {

        this.filterImpl(timestamp, newInput);
        updateStable(timestamp);
        
        return lowPassedVals;
    }
    
    private void updateStable(long timestamp) {
        if (!this.isStable()) {
        	this.warmUpCount++;
            if (this.warmUpCount > this.stableCount) {
            	this.stable = true;
            }
        }
    }

    private void filterImpl(long timestamp, double[][] newInput) {
        for (int i = 0; i < lowPassedVals.length; i++) {
        	for (int j = 0; j < lowPassedVals[i].length; j++) {
        		lowPassedVals[i][j] = alpha * this.lowPassedVals[i][j] + oneMinusAlpha * newInput[i][j];
        	}
        }
    }

    public boolean isStable() {
        return this.stable;
    }
    
    public void reset(float alpha) {
    	this.alpha = alpha;
    	this.oneMinusAlpha = 1 - alpha;
    	this.stableCount = (int) Math.ceil(Math.log(0.001) / Math.log(alpha));
    	this.reset();
    }
    
    public void reset() {
        this.resetVals();
        this.warmUpCount = 0;
        this.stable = false;
    }

    private void resetVals() {
    	for (int i = 0; i < lowPassedVals.length; i++) {
        	for (int j = 0; j < lowPassedVals[i].length; j++) {
        		this.lowPassedVals[i][j] = 0;
        	}
    	}
    }
}
