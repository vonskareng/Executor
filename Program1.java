// Peter Idestam-Almquist, 2020-02-04.
// Rebecka Skareng

// [Do necessary modifications of this file.]

package paradis.assignment3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

// [You are welcome to add some import statements.]

public class Program1 {
	final static int NUM_WEBPAGES = 40;
	private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];
	private static BlockingQueue<WebPage> initiated = new ArrayBlockingQueue<>(NUM_WEBPAGES);
	private static BlockingQueue<WebPage> downloaded = new ArrayBlockingQueue<>(NUM_WEBPAGES);
	private static BlockingQueue<WebPage> analyzed = new ArrayBlockingQueue<>(NUM_WEBPAGES);
	private static BlockingQueue<WebPage> categorized = new ArrayBlockingQueue<>(NUM_WEBPAGES);
	private static ExecutorService executor = ForkJoinPool.commonPool();

	// private static BlockingQueue<WebPage>
	// [You are welcome to add some variables.]

	// [You are welcome to modify this method, but it should NOT be parallelized.]
	private static void initialize() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			webPages[i] = new WebPage(i, "http://www.site.se/page" + i + ".html");
			try {
				initiated.put(webPages[i]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// [Do modify this sequential part of the program.]
	private static void downloadWebPages() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			executor.execute(() -> {
				try {
					WebPage webPage = initiated.take();
					webPage.download();
					downloaded.put(webPage);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});

		}
	}

	// [Do modify this sequential part of the program.]
	private static void analyzeWebPages() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			executor.execute(() -> {
				try {
					WebPage webPage = downloaded.take();
					webPage.analyze();
					analyzed.put(webPage);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
	}

	// [Do modify this sequential part of the program.]
	private static void categorizeWebPages() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			executor.execute(() -> {
				try {
					WebPage webPage = analyzed.take();
					webPage.categorize();
					categorized.put(webPage);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
	}

	// [You are welcome to modify this method, but it should NOT be parallelized.]
	private static void presentResult() {
		for (int i = 0; i < NUM_WEBPAGES; i++) {
			try {
				System.out.println(categorized.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
