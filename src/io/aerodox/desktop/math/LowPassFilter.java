package io.aerodox.desktop.math;



/**
 * Created by maeglin89273 on 1/29/15.
 */
public class LowPassFilter<T> {

    public static final float ALPHA_DEFAULT = 0.687f;
    
    private float alpha;
    private float oneMinusAlpha;
    
    private int stableCount;
    private int warmUpCount;
    private boolean stable;
    
    private ValueIterator<T> lowPassedItor;
    
    public LowPassFilter(ValueIterator<T> itor) {
        this(itor, ALPHA_DEFAULT);
    }

    public LowPassFilter(ValueIterator<T> itor, float alpha) {
        this.lowPassedItor = itor;
        reset(alpha);
    }

    public T filter(ValueIterator<T> newInput) {

        this.filterImpl(newInput);
        updateStable();
        
        return this.lowPassedItor.getValues();
    }
    
    private void updateStable() {
        if (!this.isStable()) {
        	this.warmUpCount++;
            if (this.warmUpCount > this.stableCount) {
            	this.stable = true;
            }
        }
    }

    private void filterImpl(ValueIterator<T> newInput) {
    	for (this.lowPassedItor.restart(); this.lowPassedItor.hasNext(); this.lowPassedItor.next(), newInput.next()) {
    		this.lowPassedItor.set(alpha * this.lowPassedItor.get() + oneMinusAlpha * newInput.get());
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
    	
    	for (this.lowPassedItor.restart(); this.lowPassedItor.hasNext(); this.lowPassedItor.next()) {
    		this.lowPassedItor.set(0);
    	}
    }
    
    public static abstract class ValueIterator<T> {
    	protected T values;
    	protected ValueIterator(T values) {
    		this.values = values;
    	}
    	public abstract void restart();
    	public abstract boolean hasNext();
    	public abstract void next();
    	public abstract void set(double value);
    	public abstract double get();
    	public T getValues() {
    		 return this.values;
    	 }
    }
}
