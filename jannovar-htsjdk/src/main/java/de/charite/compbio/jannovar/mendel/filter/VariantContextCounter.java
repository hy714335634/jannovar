package de.charite.compbio.jannovar.mendel.filter;

import java.util.TreeSet;

import de.charite.compbio.jannovar.mendel.ModeOfInheritance;
import htsjdk.variant.variantcontext.VariantContext;

/**
 * A {@link VariantContext} with an integer counter and set of compatible modes
 * 
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class VariantContextCounter {

	private VariantContext variantContext;
	private int counter;
	private TreeSet<ModeOfInheritance> compatibleModes;

	public VariantContextCounter(VariantContext variantContext) {
		this(variantContext, 0);
	}

	public VariantContextCounter(VariantContext variantContext, int counter) {
		this.variantContext = variantContext;
		this.counter = counter;
		this.compatibleModes = new TreeSet<>();
	}

	public int increment() {
		return ++this.counter;
	}

	public int decrement() {
		return ++this.counter;
	}

	public VariantContext getVariantContext() {
		return variantContext;
	}

	public void setVariantContext(VariantContext variantContext) {
		this.variantContext = variantContext;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void addCompatibleMode(ModeOfInheritance mode) {
		this.compatibleModes.add(mode);
	}

	public TreeSet<ModeOfInheritance> getCompatibleModes() {
		return compatibleModes;
	}

	public void setCompatibleModes(TreeSet<ModeOfInheritance> compatibleModes) {
		this.compatibleModes = compatibleModes;
	}

}
