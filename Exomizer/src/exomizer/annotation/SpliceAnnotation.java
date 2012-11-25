package exomizer.annotation;

import exomizer.reference.KnownGene;
import exomizer.reference.Annotation;
import exomizer.reference.Translator;


/**
 * This class is intended to provide a static method to generate annotations for splice
 * mutations. This method is put in its own class only for convenience and to at least
 * have a name that is easy to find.
 * @version 0.01 (November 15, 2012)
 * @author Peter N Robinson
 */

public class SpliceAnnotation {
    /** Number of nucleotides away from exon/intron boundary to be considered as potential splicing mutation. */
    public final static int SPLICING_THRESHOLD=2;

    /**
     * Determine if the variant under consideration  (which the calling
     * code has determined to be on the plus strand) represents a splice variant, defined as 
     * being in the SPLICING_THRESHOLD nucleotides within the exon/intron boundry. If so,
     * return true, otherwise, return false.
     * @param k Exon number in gene represented by kgl
     * @param kgl Gene to be checked for splice mutation for current chromosomal variant.
     */
    public static boolean isSpliceVariantPlusStrand(KnownGene kgl, int start, int end, String ref, String alt, int k) {
	if (kgl.getExonCount() == 1) return false; /* Single-exon genes do not have introns */
	int exonend = kgl.getExonEnd(k);
	int exonstart = kgl.getExonStart(k);
	if (k==0 && start >= exonend-SPLICING_THRESHOLD+1 && start <= exonend+SPLICING_THRESHOLD) {
	    /* variation is located right after (3' to) first exon. For instance, if 
	       SPLICING_THRESHOLD is 2, we get the last two nucleotides of the first (zeroth)
	       exon and the first 2 nucleotides of the following intron*/
	    return true;
	} else if (k == kgl.getExonCount()-1 && start >= exonstart - SPLICING_THRESHOLD 
		   && start <= exonstart + SPLICING_THRESHOLD -1) {
	    /* variation is located right before (5' to) the last exon, +/- SPLICING_THRESHOLD
	       nucleotides of the exon/intron boundary */
	    return true;
	} else if (k>0 && k < kgl.getExonCount()-1) {
	    /* interior exon */
	    if (start >= exonstart -SPLICING_THRESHOLD && start <= exonstart + SPLICING_THRESHOLD - 1)
		/* variation is located at 5' end of exon in splicing region */
		return true;
	    else if (start >= exonend - SPLICING_THRESHOLD + 1 &&  start <= exonend + SPLICING_THRESHOLD)
		/* variation is located at 3' end of exon in splicing region */
		return true;
	}
	/* Now repeat the above calculations for "end", the end position of the variation.
	* TODO: in many cases, start==end, this calculation is then superfluous. Refactor.*/
	if (k==0 && end >= exonend-SPLICING_THRESHOLD+1 && end <= exonend+SPLICING_THRESHOLD) {
	    /* variation is located right after (3' to) first exon. For instance, if 
	       SPLICING_THRESHOLD is 2, we get the last two nucleotides of the first (zeroth)
	       exon and the first 2 nucleotides of the following intron*/
	    return true;
	} else if (k == kgl.getExonCount()-1 && end >= exonstart - SPLICING_THRESHOLD 
		   && end <= exonstart + SPLICING_THRESHOLD -1) {
	    /* variation is located right before (5' to) the last exon, +/- SPLICING_THRESHOLD
	       nucleotides of the exon/intron boundary */
	    return true;
	} else if (k>0 && k < kgl.getExonCount()-1) {
	    /* interior exon */
	    if (end >= exonstart -SPLICING_THRESHOLD && end <= exonstart + SPLICING_THRESHOLD - 1)
		/* variation is located at 5' end of exon in splicing region */
		return true;
	    else if (end >= exonend - SPLICING_THRESHOLD + 1 &&  end <= exonend + SPLICING_THRESHOLD)
		/* variation is located at 3' end of exon in splicing region */
		return true;
	}
	/* Check whether start/end are different and overlap with splice region. */
	if (k==0 && start <= exonend && end >= exonend) {
	    /* first exon, start is 5' to exon/intron boundry and end is 3' to boundary */
	    return true;
	} else if (k == kgl.getExonCount()-1 && start <= exonstart && end >= exonstart) {
	    /* last exon, start is 5' to exon/intron boundry and end is 3' to boundary */
	    return true;
	} else if (k>0 && k < kgl.getExonCount() -1) {
	     /* interior exon */
	    if (start <= exonstart && end >= exonstart) {
		/* variant overlaps 5' exon/intron boundary */
		return true;
	    } else if (start <= exonend && end >= exonend) {
		/* variant overlaps 3' exon/intron boundary */
		return true;
	    }
	}
	return false; /* This variant does not lead to a splicing mutation */
    }



     /**
     * Determine if the variant under consideration  (which the calling
     * code has determined to be on the plus strand) represents a splice variant, defined as 
     * being in the SPLICING_THRESHOLD nucleotides within the exon/intron boundry. If so,
     * return true, otherwise, return false.
     * @param k Exon number in gene represented by kgl
     * @param kgl Gene to be checked for splice mutation for current chromosomal variant.
     */
    public static boolean isSpliceVariantMinusStrand(KnownGene kgl, int start, int end, String ref, String alt, int k) {
	System.out.println("Warning: Not yet implemented: isSpliceVariantMinusStrand");
	return false;

    }



  
       /**
     * Return an annotation for a splice mutation for a gene on the plus strand.
     * <P> The logic is that if a mutation is at position IVSN-1 or IVSN-2 (where N is the
     * number of the following exon (k+1 in the code below, since k is zero-based), then
     * the mutation can be right in front of the exon or right after the exon
     * <OL>
     * <LI>Acceptor mutation: recognize this because the position of the mutation is within one or two 
     * (SPLICING_THRESHOLD) nucleotides of the start of the exon.  We then have c.XYZ-1G>T, or something
     * similar, where XYZ is the first position of the exon; this is calculated by 
     * {@code cumlenexon -= (exonend - exonstart);} (think about it!). The position of the
     * mutation (-1 or -2) is calculated by {@code exonstart-start}.
     * <LI>Donor mutation. This is something like c.XYZ+2T>C, where XYZ is the last position
     * of the exon, which is simply {@code cumlenexon}. The position of the mutation (+1 or +2)
     * is calculated by start-exonend.
     * </OL>
     * At the moment, no specific annotation is made for other kinds of splice mutation (e.g., a
     * deletion of several base pairs surrounding a splice site. For now, we will just use the gene
     * symbol as the annotation together with the variant type SPLICING. This can be improved in the future.
     * <P>
     *  I am unsure what this comment in annovar is supposed to mean:
     * if name2 is already a splicing variant, but its detailed annotation 
     * (like c150-2A>G) is not available, 
     * and if this splicing leads to amino acid change (rather than UTR change)
     * @param k Exon number in gene represented by kgl
     * @param kgl Gene to be checked for splice mutation for current chromosomal variant.
     * @param start start position of the variant
     * @param  end position of the variant
     * @param ref reference sequence
     * @param alt variant sequence
     * @param k number (zero-based) of the affected exon.
     * @param cumlenexon cumulative length up the end of exon k
     * @return An {@link exomizer.reference.Annotation Annotation} object corresponding to the splice mutation.
     */
    public static Annotation getSpliceAnnotationPlusStrand(KnownGene kgl, int start, int end, String ref, String alt, int k, int cumlenexon) {
	int cdsstart = kgl.getCDSStart();
	if (start == end && start >= cdsstart) { /* single-nucleotide variant */
	    int exonend = kgl.getExonEnd(k);
	    int exonstart = kgl.getExonStart(k);
	    if (start >= exonstart -SPLICING_THRESHOLD  && start < exonstart) {
		/*  #------*-<---->------- mutation located right in front of exon */
		cumlenexon -= (exonend - exonstart);
		/*  Above, we had $lenexon += ($exonend[$k]-$exonstart[$k]+1); take back but for 1.*/
		String anno = String.format("HGVS=%s(%s:exon:%d:c.%d-%d%s>%s)",kgl.getName2(),kgl.getName(),
					    k+1,cumlenexon,exonstart-start,ref,alt);
		Annotation ann = Annotation.createSplicingAnnotation(anno);
		return ann;
		/* Annovar:$splicing_anno{$name2} .= "$name:exon${\($k+1)}:c.$lenexon-" . ($exonstart[$k]-$start) . "$ref>$obs,"; */
	    } else if (start > exonend && start <= exonend + SPLICING_THRESHOLD)  {
		/* #-------<---->-*--------<-->-- mutation right after exon end */
		String anno = String.format("exon%d:c.%d+%d$s>$s",k+1,cumlenexon,start-exonend,ref,alt);
		//$splicing_anno{$name2} .= "$name:exon${\($k+1)}:c.$lenexon+" . ($start-$exonend[$k]) . "$ref>$obs,";
		Annotation ann = Annotation.createSplicingAnnotation(anno);
		return ann;
	    } 
	} 
	/* If we get here, the is a complicated splice mutation not covered by the above cases.*/
	String annot = String.format("%s:%s:exon%d:complicated splice mutation",kgl.getName2(),kgl.getName(),k+1);
	Annotation ann = Annotation.createSplicingAnnotation(annot);
	return ann;
	
    }


    public static Annotation getSpliceAnnotationMinusStrand(KnownGene kgl, int start, int end, String ref, String alt, int k, int cumlenexon) {
	System.out.println("Warning: Not yet implemented: getSpliceAnnotationMinusStrand");
	return null;

    }




}