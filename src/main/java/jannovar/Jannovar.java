package jannovar;

/** Command line functions from apache */
import jannovar.cmd.CommandLineParsingException;
import jannovar.cmd.HelpRequestedException;
import jannovar.cmd.JannovarCommand;
import jannovar.cmd.annotate_pos.AnnotatePositionCommand;
import jannovar.cmd.annotate_vcf.AnnotateVCFCommand;
import jannovar.cmd.download.DownloadCommand;
import jannovar.reference.TranscriptInfo;

/**
 * This is the driver class for a program called Jannovar. It has two purposes
 * <OL>
 * <LI>Take the UCSC files knownGene.txt, kgXref.txt, knownGeneMrna.txt, and knownToLocusLink.txt, and to create
 * corresponding {@link TranscriptInfo} objects and to serialize them. The resulting serialized file can be used both by
 * this program itself (see next item) or by the main Exomizer program to annotated VCF file.
 * <LI>Using the serialized file of {@link TranscriptInfo} objects (see above item) annotate a VCF file using
 * annovar-type program logic. Note that this functionality is also used by the main Exomizer program and thus this
 * program can be used as a stand-alone annotator ("Jannovar") or as a way of testing the code for the Exomizer.
 * </OL>
 * <P>
 * To run the "Jannovar" executable:
 * <P>
 * {@code java -Xms1G -Xmx1G -jar Jannovar.jar -V xyz.vcf -D $SERIAL}
 * <P>
 * This will annotate a VCF file. The results of jannovar annotation are shown in the form
 *
 * <PRE>
 * Annotation {original VCF line}
 * </PRE>
 * <P>
 * Just a reminder, to set up annovar to do this, use the following commands.
 *
 * <PRE>
 *   perl annotate_variation.pl --downdb knownGene --buildver hg19 humandb/
 * </PRE>
 *
 * then, to annotate a VCF file called BLA.vcf, we first need to convert it to Annovar input format and run the main
 * annovar program as follows.
 *
 * <PRE>
 * $ perl convert2annovar.pl BLA.vcf -format vcf4 > BLA.av
 * $ perl annotate_variation.pl -buildver hg19 --geneanno BLA.av --dbtype knowngene humandb/
 * </PRE>
 *
 * This will create two files with all variants and a special file with exonic variants.
 * <p>
 * There are three ways of using this program.
 * <ol>
 * <li>To create a serialized version of the UCSC gene definition data. In this case, the command-line flag <b>- S</b>
 * is provide as is the path to the four UCSC files. Then, {@code anno.serialize()} is true and a file <b>ucsc.ser</b>
 * is created.
 * <li>To deserialize the serialized data (<b>ucsc.ser</b>). In this case, the flag <b>- D</b> must be used.
 * <li>To simply read in the UCSC data without creating a serialized file.
 * </ol>
 * Whichever of the three versions is chosen, the user may additionally pass the path to a VCF file using the <b>-v</b>
 * flag. If so, then this file will be annotated using the UCSC data, and a new version of the file will be written to a
 * file called test.vcf.jannovar (assuming the original file was named test.vcf). The
 *
 * @author Peter N Robinson
 * @author mjaeger
 * @version 0.33 (29 December, 2013)
 */
public final class Jannovar {
	/** Configuration for the Jannovar program. */
	JannovarOptions options = null;

	public static void main(String argv[]) {
		if (argv.length == 0) {
			// No arguments, print top level help and exit.
			printTopLevelHelp();
			System.exit(1);
		}

		// Create the corresponding command.
		JannovarCommand cmd = null;
		try {
			if (argv[0].equals("download"))
				cmd = new DownloadCommand(argv);
			else if (argv[0].equals("annotate"))
				cmd = new AnnotateVCFCommand(argv);
			else if (argv[0].equals("annotate-pos"))
				cmd = new AnnotatePositionCommand(argv);
			else
				System.err.println("unrecognized command " + argv[0]);
		} catch (CommandLineParsingException e) {
			System.err.println("problem with parsing command line options: " + e.getMessage());
		} catch (HelpRequestedException e) {
			return; // no error, user wanted help
		}

		// Stop if no command could be created.
		if (cmd == null)
			System.exit(1);

		// Execute the command.
		try {
			cmd.run();
		} catch (JannovarException e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Print top level help (without any command).
	 */
	private static void printTopLevelHelp() {
		System.err.println("Program: jannovar (functional annotation of VCF files)");
		System.err.println("Version: 0.10");
		System.err.println("Contact: Peter N Robinson <peter.robinson@charite.de>");
		System.err.println("");
		System.err.println("Usage: java -jar jannovar.jar <command> [options]");
		System.err.println("");
		System.err.println("Command: download      download transcript database");
		System.err.println("         annotate      functional annotation of VCF files");
		System.err.println("         annotate-pos  functional annotation of genomic change");
		System.err.println("");
		System.err.println("Example: java -jar jannovar.jar download ucsc");
		System.err.println("         java -jar jannovar.jar annotate -D ucsc_hg19.ser variants.vcf");
		System.err.println("         java -jar jannovar.jar annotate-pos -D ucsc_hg19.ser 'chr1:12345C>A'");
		System.err.println("");
	}

}
