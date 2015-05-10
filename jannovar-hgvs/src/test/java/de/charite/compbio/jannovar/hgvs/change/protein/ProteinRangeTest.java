package de.charite.compbio.jannovar.hgvs.change.protein;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.charite.compbio.jannovar.hgvs.change.AminoAcidCode;

public class ProteinRangeTest {

	private ProteinPointLocation firstLoc;
	private ProteinPointLocation firstLoc2;
	private ProteinPointLocation lastLoc;

	@Before
	public void setUp() {
		firstLoc = new ProteinPointLocation(123, "A");
		firstLoc2 = new ProteinPointLocation(123, "A");
		lastLoc = new ProteinPointLocation(125, "G");
	}

	@Test
	public void testEquals() {
		Assert.assertEquals(firstLoc, firstLoc2);
		Assert.assertNotEquals(lastLoc, firstLoc);
	}

	/**
	 * Test toHGVSString() with a range of length >1.
	 */
	@Test
	public void testToHGVSStringRange() {
		ProteinRange range = new ProteinRange(firstLoc, lastLoc);

		Assert.assertEquals("124A_126G", range.toHGVSString(AminoAcidCode.ONE_LETTER));
		Assert.assertEquals("124Ala_126Gly", range.toHGVSString(AminoAcidCode.THREE_LETTER));
	}

	/**
	 * Test toHGVSString() with a single position.
	 */
	@Test
	public void testToHGVSStringSinglePos() {
		ProteinRange range = new ProteinRange(firstLoc, firstLoc2);

		Assert.assertEquals("124A", range.toHGVSString(AminoAcidCode.ONE_LETTER));
		Assert.assertEquals("124Ala", range.toHGVSString(AminoAcidCode.THREE_LETTER));

	}

}
