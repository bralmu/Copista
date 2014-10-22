package com.madgeargames.copista.notes;

import java.util.Arrays;

public class NoteSet {

	private final Note[] notes;
	private static Note[] notesAll = { new Note(0, "Do", 217, 1, 6),
			new Note(1, "Re", 255, 121, 22), new Note(2, "Mi", 255, 223, 0),
			new Note(3, "Fa", 14, 211, 35), new Note(4, "Sol", 40, 131, 236),
			new Note(5, "La", 21, 75, 121), new Note(6, "Si", 168, 0, 197),
			new Note(7, "do", 255, 0, 0) };

	/**
	 * All notes
	 */
	public NoteSet() {
		notes = notesAll;
	}

	/**
	 * Custom notes
	 * 
	 * @param notesIndex
	 */
	public NoteSet(int[] notesIds) {
		notes = new Note[notesIds.length];
		for (int i = 0; i < notesIds.length; i++) {
			notes[i] = notesAll[notesIds[i]];
		}
		Arrays.sort(notes);
	}

	public Note getNote(int index) {
		return notes[index];
	}

	public Note getNoteById(int noteId) {
		for (Note n : notes) {
			if (n.id == noteId) {
				return n;
			}
		}
		return null;
	}

	public int getNoteIndex(int noteId) {
		for (int i = 0; i < notes.length; i++) {
			if (notes[i].id == noteId) {
				return i;
			}
		}
		return -1;
	}

	public int size() {
		return notes.length;
	}

	public int[] getIds() {
		int[] notesIds = new int[notes.length];
		for (int i = 0; i < notes.length; i++) {
			notesIds[i] = notes[i].id;
		}
		return notesIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(notes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NoteSet other = (NoteSet) obj;
		if (!Arrays.equals(notes, other.notes))
			return false;
		return true;
	}

}
