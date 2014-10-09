package com.madgeargames.copista.tests;

import com.madgeargames.copista.notes.NoteSet;
import com.madgeargames.copista.notes.NoteSetGenerator;

public class TestNoteSetGenerator {

	public static void main(String[] args) {
		// NoteSets same base
		int randomNoteIndex = (int) (Math.random() * 8 - .0001);
		NoteSet ns = new NoteSet(new int[] { randomNoteIndex });
		printNoteSet(ns);
		while (ns.size() < 8) {
			ns = NoteSetGenerator.increaseNoteSet(ns);
			printNoteSet(ns);
		}
		// NoteSets full random
		for (int i = 1; i < 10; i++) {
			NoteSet noteSet = NoteSetGenerator.generateNoteSet(i);
			printNoteSet(noteSet);
		}
	}

	private static void printNoteSet(NoteSet noteset) {
		for (int i = 0; i < noteset.size(); i++) {
			System.out.print(noteset.getNote(i).id + " ");
		}
		System.out.print("\n");
	}

}
