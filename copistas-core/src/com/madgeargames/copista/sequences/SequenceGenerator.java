package com.madgeargames.copista.sequences;

import java.util.ArrayList;
import java.util.List;

import com.madgeargames.copista.notes.NoteSet;

public class SequenceGenerator {
	public static List<Sequence> generateSequences(NoteSet noteSet, int sequenceLength,
			int sequenceCount) {
		// Empty list of sequences.
		List<Sequence> sequences = new ArrayList<Sequence>(sequenceCount);
		// Forward loop sequence
		int[] numericSequence = new int[sequenceLength];
		boolean direction = true;
		int insertionsCount = 0;
		int noteIndex = -1;
		while (insertionsCount < sequenceLength) {
			if (direction) {
				noteIndex++;
				numericSequence[insertionsCount] = noteSet.getNote(noteIndex).id;
				insertionsCount++;
				if (noteIndex == noteSet.size() - 1) {
					direction = false;
				}
			} else {
				noteIndex--;
				numericSequence[insertionsCount] = noteSet.getNote(noteIndex).id;
				insertionsCount++;
				if (noteIndex == 0) {
					direction = true;
				}
			}
		}
		sequences.add(new Sequence(numericSequence));
		// Backwards loop sequence
		numericSequence = new int[sequenceLength];
		direction = false;
		insertionsCount = 0;
		noteIndex = noteSet.size();
		while (insertionsCount < sequenceLength) {
			if (direction) {
				noteIndex++;
				numericSequence[insertionsCount] = noteSet.getNote(noteIndex).id;
				insertionsCount++;
				if (noteIndex == noteSet.size() - 1) {
					direction = false;
				}
			} else {
				noteIndex--;
				numericSequence[insertionsCount] = noteSet.getNote(noteIndex).id;
				insertionsCount++;
				if (noteIndex == 0) {
					direction = true;
				}
			}
		}
		sequences.add(new Sequence(numericSequence));
		// random sequences
		while (sequences.size() < sequenceCount) {
			numericSequence = new int[sequenceLength];
			direction = true;
			insertionsCount = 0;
			noteIndex = -1;
			while (insertionsCount < sequenceLength) {
				int randomNoteIndex = (int) (Math.random() * noteSet.size() - .0001);
				numericSequence[insertionsCount] = noteSet.getNote(randomNoteIndex).id;
				insertionsCount++;
			}
			sequences.add(new Sequence(numericSequence));
		}
		return sequences;
	}
}
