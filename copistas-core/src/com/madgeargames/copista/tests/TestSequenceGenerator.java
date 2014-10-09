package com.madgeargames.copista.tests;

import java.util.List;

import com.madgeargames.copista.notes.NoteSet;
import com.madgeargames.copista.notes.NoteSetGenerator;
import com.madgeargames.copista.sequences.Sequence;
import com.madgeargames.copista.sequences.SequenceGenerator;

public class TestSequenceGenerator {

	public static void main(String[] args) {
		NoteSet noteSet = NoteSetGenerator.generateNoteSet(3);
		List<Sequence> sequences = SequenceGenerator.generateSequences(noteSet, 10, 5);
		printSequences(sequences);
	}

	private static void printSequences(List<Sequence> sequences) {
		for (Sequence s : sequences) {
			System.out.println(s);
		}
	}

}
