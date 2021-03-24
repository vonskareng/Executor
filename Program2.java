// Peter Idestam-Almquist, 2020-02-04.
// Rebecka Skareng

// [Do necessary modifications of this file.]

package paradis.assignment3;

import java.util.Arrays;
import java.util.stream.Stream;

// [You are welcome to add some import statements.]

public class Program2 {
	final static int NUM_WEBPAGES = 40;
	private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];
	private static WebPage[] categorized;
	private static Stream<WebPage> init;
	private static Stream<WebPage> downloaded;
	private static Stream<WebPage> analyzed;
	// [You are welcome to add some variables.]

	// [You are welcome to modify this method, but it should NOT be parallelized.]
	private static void initialize() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			webPages[i] = new WebPage(i, "http://www.site.se/page" + i + ".html");
		}
		init = Arrays.stream(webPages).parallel();
	}

	// [Do modify this sequential part of the program.]
	private static void downloadWebPages() {
		downloaded = init.map(webPage -> {
			webPage.download();
			return webPage;
		});
	}

	// [Do modify this sequential part of the program.]
	private static void analyzeWebPages() {
		analyzed = downloaded.map(webPage -> {
			webPage.analyze();
			return webPage;
		});
	}

	// [Do modify this sequential part of the program.]
	private static void categorizeWebPages() {
		categorized = analyzed.map(webPage -> {
			webPage.categorize();
			return webPage;
		}).toArray(WebPage[]::new);
	}

	// [You are welcome to modify this method, but it should NOT be parallelized.]
	private static void presentResult() {
		
		for (int i = 0; i < NUM_WEBPAGES; i++) {

			System.out.println(categorized[i]);

		}
	}

	public static void main(String[] args) {
		// Initialize the list of webpages.
		initialize();

		// Start timing.
		long start = System.nanoTime();

		// Do the work.
		downloadWebPages();
		analyzeWebPages();
		categorizeWebPages();

		// Stop timing.
		long stop = System.nanoTime();

		// Present the result.

		presentResult();

		// Present the execution time.
		System.out.println("Execution time (seconds): " + (stop - start) / 1.0E9);
	}
}
