/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright 2017-2022 Vincent A. Cicirello, <https://www.cicirello.org/>.
 *
 * This file is part of the rho mu library.
 *
 * The rho mu library is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 *
 * The rho mu library is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the rho mu library.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.cicirello.math.rand;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.cicirello.math.MathFunctions;
import java.util.SplittableRandom;

/**
 * JUnit tests for Binomial random variables.
 */
public class BinomialTests extends SharedTestRandom {
	
	@Test
	public void testNextBinomialSplittable() {
		SplittableRandom r = new SplittableRandom(42);
		final int MAX_N = 10;
		double[] crit = {3.841, 5.991, 7.815, 9.488, 11.07, 12.59, 14.07, 15.51, 16.92, 18.31};
		double[] P = {0.25, 0.5, 0.75};
		for (double p : P) {
			double q = 1-p;
			for (int n = 1; n <= MAX_N; n++) {
				double[] dist = new double[n+1];
				for (int i = 0; i < dist.length; i++) {
					dist[i] = CHOOSE[n][i] * MathFunctions.pow(p, i) * Math.pow(q, n-i);
				}
				final double TRIALS = 300 * (n+1); //1.0 / Math.min(dist[0], dist[n]) * 10 * (n+1);
				int[] buckets = new int[n+1];
				for (int i = 0; i < TRIALS; i++) {
					int b = RandomVariates.nextBinomial(n, p, r);
					buckets[b]++;
				}
				double chi = chiSquare(buckets, dist);
				assertTrue(chi <= crit[n-1], "n="+n+" p="+p+ " chi="+chi);
			}
		}
		for (double p : P) {
			double q = 1-p;
			for (int n = 30; n <= 50; n+=20) {
				int which = n == 30 ? 0 : 1;
				double[] dist = new double[n+1];
				for (int i = 0; i < dist.length; i++) {
					dist[i] = CHOOSE_LARGE[which][i] * MathFunctions.pow(p, i) * Math.pow(q, n-i);
				}
				final double TRIALS = 300 * (n+1); //1.0 / Math.min(dist[0], dist[n]) * 10 * (n+1);
				int[] buckets = new int[n+1];
				for (int i = 0; i < TRIALS; i++) {
					int b = RandomVariates.nextBinomial(n, p, r);
					buckets[b]++;
				}
				double chi = chiSquare(buckets, dist);
				double critV = n == 30 ? 43.77 : 67.50;
				assertTrue(chi <= critV, "n="+n+" p="+p+ " chi="+chi);
			}
		}
	}
	
	@Test
	public void testNextBinomialLargeN_Splittable() {
		SplittableRandom r = new SplittableRandom(42);
		final int N = 1000;
		final int K = 20;
		int t01 = 0;
		int t25 = 0;
		int t50 = 0;
		for (int i = 0; i < K; i++) {
			int b01 = RandomVariates.nextBinomial(N, 0.01, r);
			int b25 = RandomVariates.nextBinomial(N, 0.25, r);
			int b50 = RandomVariates.nextBinomial(N, 0.5, r);
			assertTrue(b01 >= 0 && b01 <= N);
			assertTrue(b25 >= 0 && b25 <= N);
			assertTrue(b50 >= 0 && b50 <= N);
			t01 += b01;
			t25 += b25;
			t50 += b50;
		}
		assertTrue(Math.abs(N*0.5 - 1.0*t50/K) <= N*0.125);
		assertTrue(Math.abs(N*0.25 - 1.0*t25/K) <= N*0.125);
		assertTrue(Math.abs(N*0.01 - 1.0*t01/K) <= N*0.125);
	}
		
	@Test
	public void testNextBinomialThreadLocal() {
		// Since we cannot seed ThreadLocalRandom, this test case is
		// not 100% replicable.  Additionally, we know that this version of
		// the method simply calls the version that takes a Random as a parameter.
		// Since we did test that version for goodness of fit with a binomial,
		// and since replication is not possible without a seed, we simply verify
		// that over a large number of trials that most possible outcomes are produced.
		final int MAX_N = 10;
		double[] P = {0.5, 0.75};
		for (double p : P) {
			for (int n = 1; n <= MAX_N; n++) {
				final double TRIALS = 2000 * (n+1); 
				boolean[] generated = new boolean[n+1];
				int count = 0;
				for (int i = 0; i < TRIALS; i++) {
					int b = RandomVariates.nextBinomial(n, p);
					if (!generated[b]) {
						generated[b] = true;
						count++;
						if (count >= n-1) break;
					} 
				}
				assertTrue( count >= n-1, "Verifying most possible outcomes produced over large number of trials");
			}
		}
	}
	
	private static final double[][] CHOOSE_LARGE = {
		{1f, 30f, 435f, 4060f, 27405f, 142506f, 593775f, 2035800f, 5852925f, 14307150f, 30045015f, 54627300f, 86493225f, 119759850f, 145422675f, 155117520f, 145422675f, 119759850f, 86493225f, 54627300f, 30045015f, 14307150f, 5852925f, 2035800f, 593775f, 142506f, 27405f, 4060f, 435f, 30f, 1},
		{1f, 50f, 1225f, 19600f, 230300f, 2118760f, 15890700f, 99884400f, 536878650f, 2505433700f, 10272278170f, 37353738800f, 1.214E+11f, 3.54861E+11f, 9.37846E+11f, 2.25083E+12f, 4.92369E+12f, 9.84738E+12f, 1.80535E+13f, 3.04059E+13f, 4.71292E+13f, 6.73274E+13f, 8.87498E+13f, 1.08043E+14f, 1.21549E+14f, 1.26411E+14f, 1.21549E+14f, 1.08043E+14f, 8.87498E+13f, 6.73274E+13f, 4.71292E+13f, 3.04059E+13f, 1.80535E+13f, 9.84738E+12f, 4.92369E+12f, 2.25083E+12f, 9.37846E+11f, 3.54861E+11f, 1.214E+11f, 37353738800f, 10272278170f, 2505433700f, 536878650f, 99884400f, 15890700f, 2118760f, 230300f, 19600f, 1225f, 50f, 1}
	};
	
	private static final int[][] CHOOSE = {
		{1},
		{1, 1},
		{1, 2, 1},
		{1, 3, 3, 1},
		{1, 4, 6, 4, 1},
		{1, 5, 10, 10, 5, 1},
		{1, 6, 15, 20, 15, 6, 1},
		{1, 7, 21, 35, 35, 21, 7, 1},
		{1, 8, 28, 56, 70, 56, 28, 8, 1},
		{1, 9, 36, 84, 126, 126, 84, 36, 9, 1},
		{1, 10, 45, 120, 210, 252, 210, 120, 45, 10, 1}
	};
}
