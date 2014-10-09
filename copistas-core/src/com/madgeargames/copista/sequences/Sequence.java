package com.madgeargames.copista.sequences;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
	public final List<Integer> sequence;

	public Sequence(int[] numericSequence) {
		this.sequence = new ArrayList<Integer>();
		for (int n : numericSequence) {
			sequence.add(n);
		}
	}

	public int[] toArray() {
		int[] ret = new int[sequence.size()];
		for (int i = 0; i < sequence.size(); i++) {
			ret[i] = sequence.get(i);
		}
		return ret;
	}

	@Override
	public String toString() {
		String ret = "";
		for (Integer i : sequence) {
			ret = ret + i + " ";
		}
		return ret;
	}
}
