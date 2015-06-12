package de.charite.compbio.jannovar.hgvs.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.charite.compbio.jannovar.hgvs.HGVSVariant;
import de.charite.compbio.jannovar.hgvs.nts.variant.SingleAlleleNucleotideVariant;

/**
 * Tests for the HGVSParserDriver for parsing nucleotide dellications.
 * 
 * @author Manuel Holtgrewe <manuel.holtgrewe@bihealth.de>
 */
public class HGVSParserDriverNucleotideDeletionTest {

	HGVSParserDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = new HGVSParserDriver(false);
	}

	@Test
	public void testWithoutSequence() {
		String hgvsStrings[] = new String[] { "NM_000138.4:c.247_248delins", "NM_000138.4:c.247+1_247+3delins",
				"NM_000138.4:c.247-3_247-1delins", "NM_000138.4:c.*247_*247+3delins", "NM_000138.4:c.-247_-247-3delins" };

		for (String hgvsString : hgvsStrings) {
			HGVSVariant variant = driver.parseHGVSString(hgvsString);

			Assert.assertTrue(variant instanceof SingleAlleleNucleotideVariant);
			Assert.assertEquals(hgvsString, variant.toHGVSString());
		}
	}

	@Test
	public void testWithSequence() {
		String hgvsStrings[] = new String[] { "NM_000138.4:c.247_248delATinsCAT",
				"NM_000138.4:c.247+1_247+3delATAinsCAT", "NM_000138.4:c.247-3_247-1delATAinsCAT",
				"NM_000138.4:c.*247_*247+3delATATinsCAT", "NM_000138.4:c.-247_-247-3delATATinsCAT" };

		for (String hgvsString : hgvsStrings) {
			HGVSVariant variant = driver.parseHGVSString(hgvsString);

			Assert.assertTrue(variant instanceof SingleAlleleNucleotideVariant);
			Assert.assertEquals(hgvsString, variant.toHGVSString());
		}
	}

	@Test
	public void testWithSequenceLength() {
		String hgvsStrings[] = new String[] { "NM_000138.4:c.247_248del2ins3", "NM_000138.4:c.247+1_247+3del3ins3",
				"NM_000138.4:c.247-3_247-1del3ins3", "NM_000138.4:c.*247_*247+3del4ins3",
				"NM_000138.4:c.-247_-247-3del4ins3" };

		for (String hgvsString : hgvsStrings) {
			HGVSVariant variant = driver.parseHGVSString(hgvsString);

			Assert.assertTrue(variant instanceof SingleAlleleNucleotideVariant);
			Assert.assertEquals(hgvsString, variant.toHGVSString());
		}
	}

}
