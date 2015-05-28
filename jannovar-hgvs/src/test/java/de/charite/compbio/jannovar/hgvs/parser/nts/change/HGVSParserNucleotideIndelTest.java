package de.charite.compbio.jannovar.hgvs.parser.nts.change;

import org.junit.Assert;
import org.junit.Test;

import de.charite.compbio.jannovar.hgvs.parser.HGVSLexer;
import de.charite.compbio.jannovar.hgvs.parser.HGVSParser;
import de.charite.compbio.jannovar.hgvs.parser.HGVSParserTestBase;
import de.charite.compbio.jannovar.hgvs.parser.HGVSParser.Nt_change_indelContext;

/**
 * Parser for HGVS deletion nucleotide changes.
 *
 * @author Manuel Holtgrewe <manuel.holtgrewe@bihealth.de>
 */
public class HGVSParserNucleotideIndelTest extends HGVSParserTestBase {

	@Test
	public void testWithPositionWithStrings() {
		HGVSParser parser = buildParserForString("123delCinsTCG", HGVSLexer.NUCLEOTIDE_CHANGE, false);
		Nt_change_indelContext nt_change_indel = parser.nt_change_indel();
		Assert.assertEquals("(nt_change_indel (nt_point_location (nt_base_location (nt_number 123))) del (nt_string C) ins (nt_string TCG))",
				nt_change_indel.toStringTree(parser));
	}

	@Test
	public void testWithPositionWithLengths() {
		HGVSParser parser = buildParserForString("123del1ins23", HGVSLexer.NUCLEOTIDE_CHANGE, false);
		Nt_change_indelContext nt_change_indel = parser.nt_change_indel();
		Assert.assertEquals(
				"(nt_change_indel (nt_point_location (nt_base_location (nt_number 123))) del (nt_number 1) ins (nt_number 23))",
				nt_change_indel.toStringTree(parser));
	}

	@Test
	public void testWithPositionWithoutStrings() {
		HGVSParser parser = buildParserForString("123delins", HGVSLexer.NUCLEOTIDE_CHANGE, false);
		Nt_change_indelContext nt_change_indel = parser.nt_change_indel();
		Assert.assertEquals("(nt_change_indel (nt_point_location (nt_base_location (nt_number 123))) del ins)",
				nt_change_indel.toStringTree(parser));
	}

	@Test
	public void testWithRangeWithStrings() {
		HGVSParser parser = buildParserForString("123_124delATinsGGTAT", HGVSLexer.NUCLEOTIDE_CHANGE, false);
		Nt_change_indelContext nt_change_indel = parser.nt_change_indel();
		Assert.assertEquals(
				"(nt_change_indel (nt_range (nt_point_location (nt_base_location (nt_number 123))) _ (nt_point_location (nt_base_location (nt_number 124)))) del (nt_string AT) ins (nt_string GGTAT))",
				nt_change_indel.toStringTree(parser));
	}

	@Test
	public void testWithRangeWithLengths() {
		HGVSParser parser = buildParserForString("123_124del2ins4", HGVSLexer.NUCLEOTIDE_CHANGE, false);
		Nt_change_indelContext nt_change_indel = parser.nt_change_indel();
		Assert.assertEquals(
				"(nt_change_indel (nt_range (nt_point_location (nt_base_location (nt_number 123))) _ (nt_point_location (nt_base_location (nt_number 124)))) del (nt_number 2) ins (nt_number 4))",
				nt_change_indel.toStringTree(parser));
	}

	@Test
	public void testWithRangeWithoutStrings() {
		HGVSParser parser = buildParserForString("123_124delins", HGVSLexer.NUCLEOTIDE_CHANGE, false);
		Nt_change_indelContext nt_change_indel = parser.nt_change_indel();
		Assert.assertEquals(
				"(nt_change_indel (nt_range (nt_point_location (nt_base_location (nt_number 123))) _ (nt_point_location (nt_base_location (nt_number 124)))) del ins)",
				nt_change_indel.toStringTree(parser));
	}

}
