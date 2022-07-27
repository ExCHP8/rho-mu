/*
 * Example programs for rho-mu library.
 * Copyright (C) 2017-2022 Vincent A. Cicirello
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
 
package org.cicirello.examples.rho_mu;

import org.cicirello.math.rand.EnhancedRandomGenerator;
import java.util.random.RandomGenerator;
import java.util.SplittableRandom;
import java.util.Arrays;

/**
 * Simple program demonstrating some of the core randomization functionality of the library. 
 * The source code examples contained within, along with the comments, should be of greater
 * interest to you than any output generated by this program.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class BasicUsageExamples {
	
	/**
	 * Runs the example program.
	 *
	 * @param args No command line arguments required.
	 */
	public static void main(String[] args) {
		ExamplesShared.printCopyrightAndLicense();
		
		System.out.println();
		System.out.println("The output of this example program isn't likely useful outside");
		System.out.println("of the context of the comments within the source code.");
		System.out.println();
		
		// I. WAYS OF CREATING INSTANCES:
		
		// The org.cicirello.math.rand package includes a hierarchy of 6 wrapper
		// classes, each of which supports wrapping an instance of one of Java 17's
		// RandomGenerator interfaces. Each of these wrapper classes also implements
		// the interface that it wraps. Thus, you can use it as a drop-in replacement
		// wherever you are currently using the corresponding Java 17 interface, such as:
		
		RandomGenerator r1 = new EnhancedRandomGenerator();
		
		// Each of these wrapper classes provide multiple ways of instantiating, depending
		// upon your use-case. Above we see the default constructor, which wraps an instance
		// acquired by the Java 17 RandomGenerator.getDefault() method.
		
		// If you want to specify a seed to easily enable reproducing identical behavior across runs,
		// some of the wrapper classes provide a constructor to accomplish this, such as this example:
		
		RandomGenerator r2 = new EnhancedRandomGenerator(42);
		
		// If you want to wrap an instance of a specific RandomGenerator implementation, there are
		// a few ways of accomplishing this, including:
		
		// a) Passing an instance to the constructor:
		
		RandomGenerator r3 = new EnhancedRandomGenerator(new SplittableRandom());
		
		// b) Passing a String with the name of any of Java's provided algorithms for random
		//    number generation from this list: 
		//    https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/random/package-summary.html#algorithms
		
		RandomGenerator r4 = new EnhancedRandomGenerator("Xoshiro256PlusPlus");
		
		// c) For consistency with the Java 17 interfaces, rho-mu also provides a static of method that will
		//    do the same as above:
		
		RandomGenerator r5 = EnhancedRandomGenerator.of("Xoshiro256PlusPlus");
		
		// Also for consistency with the Java 17 interface, rho-mu provides a static getDefault() method to
		// get an instance that wraps an instance of the default RandomGenerator.
		
		RandomGenerator r6 = EnhancedRandomGenerator.getDefault();
		
		
		// II. USING THE ENHANCED FUNCTIONALITY
		
		// See the documentation of EnhancedRandomGenerator 
		// (https://rho-mu.cicirello.org/api/org.cicirello.rho_mu/org/cicirello/math/rand/EnhancedRandomGenerator.html)
		// for details of all of the enhanced functionality. The examples below are just examples of a selection
		// of that enhanced functionality, and is not all exhaustive.
		
		// Since EnhancedRandomGenerator implements RandomGenerator, all methods of that interface are
		// supported. Some of these simply delegate to the wrapped instance. For example, rho-mu doesn't
		// enhance the unbounded RandomGenerator.nextInt() method, so a call to that method is simply delegated
		// such as in this example:
		
		int delegatedRandomNumber = r1.nextInt();
		System.out.println("Unbounded random ints are simply delegated to wrapped instance: " + delegatedRandomNumber);
		System.out.println();
		
		// However, the bounded RandomGenerator.nextInt(bound) and RandomGenerator.nextInt(origin, bound) are overridden
		// by EnhancedRandomGenerator with a faster algorithm, utilizing the wrapped instance as the source of randomness.
		
		int overriddenRandomNumber1 = r1.nextInt(10);
		System.out.println("The nextInt(bound) method is overridden with a faster algorithm: " + overriddenRandomNumber1);
		System.out.println();
		int overriddenRandomNumber2 = r1.nextInt(5, 10);
		System.out.println("... As is the nextInt(origin, bound) method: " + overriddenRandomNumber2);
		System.out.println();
		
		// Likewise, the methods of RandomGenerator that return streams of bounded ints are overridden to use rho-mu's
		// faster algorithm. Here is an example that simulates rolling a die 100 times, outputting the average of the rolls:
		
		double expectedDieRoll = r1.ints(100, 1, 7).average().getAsDouble();
		System.out.println("Using a stream to simulate computing average die roll: " + expectedDieRoll);
		System.out.println();
		
		// rho-mu's EnhancedRandomGenerator also overrides the RandomGenerator.nextGaussian methods:
		
		double gaussian1 = r1.nextGaussian();
		System.out.println("nextGaussian() is also overridden with different algorithm: " + gaussian1);
		System.out.println();
		double gaussian2 = r1.nextGaussian(10, 5);
		System.out.println("nextGaussian(mean, stdev) is also overridden with different algorithm: " + gaussian2);
		System.out.println();
		
		
		// III. USING THE ADDED FUNCTIONALITY
		
		// See the documentation of EnhancedRandomGenerator 
		// (https://rho-mu.cicirello.org/api/org.cicirello.rho_mu/org/cicirello/math/rand/EnhancedRandomGenerator.html)
		// for details of all of the additional functionality. The examples below are just examples of a selection
		// of that additional functionality, and is not all exhaustive.
		
		// All of the examples above of overridden behavior used EnhancedRandomGenerator instances referenced by
		// variables of type RandomGenerator. To access the additional functionality, you'll need a variable of
		// type EnhancedRandomGenerator.
		
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		
		// There may be times where strict uniformity of your random numbers is not necessary, but where speed
		// is important. rho-mu provides nextBiasedInt methods that tradeoff uniformity for speed by eliminating
		// the rejection sampling required for uniformity. For small bounds, the impact on uniformity is less than
		// it is for larger bounds. There are two such methods, one with a bound, and the other with an origin and bound.
		// Here are a few examples:
		
		int x1 = erg.nextBiasedInt(10);
		System.out.println("Ultrafast due to no rejection sampling but biased as a result: " + x1);
		System.out.println();
		int x2 = erg.nextBiasedInt(5, 10);
		System.out.println("nextBiasedInt also has a form with origin and bound: " + x2);
		System.out.println();
		
		// EnhancedRandomGenerator also supports streams of such biased ints. Here is an example that simulates 
		// rolling a die 100 times, but trading offer uniformity for speed, outputting the average of the rolls:
		
		double expectedBiasedDieRoll = erg.biasedInts(100, 1, 7).average().getAsDouble();
		System.out.println("Using a stream to simulate computing average die roll (but with biased ints): " + expectedBiasedDieRoll);
		System.out.println();
		
		// Java 17's RandomGenerator has methods for uniform random ints, longs, doubles, as well as Gaussian distributed random
		// numbers and also exponentials, but no other distributions. EnhancedRandomGenerator adds to this both Binomial and Cauchy.
		
		int randomBinomial = erg.nextBinomial(100, 0.2);
		System.out.println("Example of nextBinomial(100, 0.2): " + randomBinomial);
		System.out.println();
		double randomCauchy = erg.nextCauchy(1.0);
		System.out.println("Example of nextCauchy with a scale of 1.0: " + randomCauchy);
		System.out.println();
		
		// Java 17's RandomGenerator supports streams of uniform ints, longs, and doubles, but doesn't include streams
		// for other distributions.
		// The EnhancedRandomGenerator class includes methods for streams of Gaussians, Exponentials, Cauchys, Binomials.
		// Here is an example with a stream of Gaussians, which finds the max of a stream of 100 numbers from a Gaussian with
		// mean 0 and standard deviation 1:
		
		double maxGaussian = erg.gaussians(100).max().getAsDouble();
		System.out.println("Max of a stream of 100 random values from a standard normal: " + maxGaussian);
		System.out.println();
		
		// Here's another example, same as above but this time with Cauchys, and also demonstrating in combination with
		// of method.
		
		double maxCauchy = EnhancedRandomGenerator.of("Xoshiro256PlusPlus").cauchys(100, 0.0, 1.0).max().getAsDouble();
		System.out.println("Max of a stream of 100 random values from a Cauchy distribution with median 0 and scale 1: " + maxCauchy);
		System.out.println();
		
		// There are methods for getting random arrays of booleans, such as a random array of n booleans (true and false equally likely):
		
		boolean[] array = erg.arrayMask(8);
		System.out.println("Random boolean array (equally likely true/false): " + Arrays.toString(array));
		System.out.println();
		
		// ... boolean arrays with specified number of trues.
		array = erg.arrayMask(8, 3);
		System.out.println("Random boolean array (specific number of trues, e.g., 3): " + Arrays.toString(array));
		System.out.println();
		
		// ... boolean arrays with specified probability of trues.
		array = erg.arrayMask(8, 0.25);
		System.out.println("Random boolean array (probability of true, e.g., 0.25): " + Arrays.toString(array));
		System.out.println();
		
		// There are methods for generating random pairs of bounded integers (without replacement) as well as
		// random triples of bounded integers (without replacement). These methods are such that each possible pair (likewise
		// each possible triple) is approximately equally likely (you may optionally pass an array to store the result,
		// or null to have one constructed for you):
		
		int[] pair = erg.nextIntPair(10, null);
		System.out.println("Random pair of integers from [0, 10) without replacement: " + Arrays.toString(pair));
		System.out.println();
		
		int[] triple = erg.nextIntTriple(10, null);
		System.out.println("Random triple of integers from [0, 10) without replacement: " + Arrays.toString(triple));
		System.out.println();
		
		// There are variations of the above, but where a window can be specified constraining the elements of the pair,
		// or likewise the elements of a triple, to be within some max distance of each other.
		
		int[] windowedPair = erg.nextWindowedIntPair(20, 5, null);
		System.out.println("Random pair of integers from [0, 20), but with a difference of at most 5, without replacement: " + Arrays.toString(windowedPair));
		System.out.println();
		
		int[] windowedTriple = erg.nextWindowedIntTriple(20, 5, null);
		System.out.println("Random triple of integers from [0, 20), but with a difference of at most 5, without replacement: " + Arrays.toString(windowedTriple));
		System.out.println();
		
		// There are methods for more generally sampling k integers without replacement from the first n integers.

		int[] sample = erg.sample(100, 5, null);
		System.out.println("Random sample of 5 integers from [0, 100), without replacement: " + Arrays.toString(sample));
		System.out.println();
		
		// The EnhancedRandomGenerator actually includes implementations of 3 sampling algorithms, where the above example,
		// the sample method, chooses the fastest for your combination of n and k. It also includes, however, individual
		// methods for each of these three algorithms.
		
		sample = erg.sampleReservoir(100, 5, null);
		System.out.println("Random sample of 5 integers from [0, 100), without replacement (Vitter's reservoir algorithm): " + Arrays.toString(sample));
		System.out.println();
		
		sample = erg.samplePool(100, 5, null);
		System.out.println("Random sample of 5 integers from [0, 100), without replacement (Goodman and Hedetniemi's pool algorithm): " + Arrays.toString(sample));
		System.out.println();
		
		sample = erg.sampleInsertion(100, 5, null);
		System.out.println("Random sample of 5 integers from [0, 100), without replacement (Cicirello's insertion sampling algorithm): " + Arrays.toString(sample));
		System.out.println();
	}
	
}