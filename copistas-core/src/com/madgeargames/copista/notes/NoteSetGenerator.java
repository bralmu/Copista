package com.madgeargames.copista.notes;

import java.util.ArrayList;
import java.util.List;

public class NoteSetGenerator {
	/**
	 * Distancia sonora entre notas.
	 */
	private static int[][] distanceMatrix = { { 0, 2, 4, 5, 7, 9, 11, 12 },
			{ 2, 0, 2, 3, 5, 7, 9, 10 }, { 4, 2, 0, 1, 3, 5, 7, 8 },
			{ 5, 3, 1, 0, 2, 4, 6, 7 }, { 7, 5, 3, 2, 0, 2, 4, 5 },
			{ 9, 7, 5, 4, 2, 0, 2, 3 }, { 11, 9, 7, 6, 4, 2, 0, 1 },
			{ 12, 10, 8, 7, 5, 3, 1, 0 } };

	/**
	 * Crea un conjunto de notas aleatorio y del tamaño especificado.
	 * 
	 * @param size
	 *            Tamaño del conjunto.
	 * @return Conjunto de notas.
	 */
	public static NoteSet generateNoteSet(int size) {
		int randomNoteIndex = (int) (Math.random() * 8 - .0001);
		NoteSet ns = new NoteSet(new int[] { randomNoteIndex });
		while (ns.size() < size && ns.size() < 8) {
			ns = increaseNoteSet(ns);
		}
		return ns;
	}

	/**
	 * Amplia en una nota el conjunto de notas. Añade la nota basándose en las
	 * distancias entre notas.
	 * 
	 * @param baseNoteSet
	 *            Conjunto de notas que se toma como referencia.
	 * @return Nuevo conjunto de notas con una nota más que el de referencia.
	 */
	public static NoteSet increaseNoteSet(NoteSet baseNoteSet) {
		int newNoteId;
		// regla para ampliar NoteSet de 1 nota.
		if (baseNoteSet.size() == 1) {
			int noteId = baseNoteSet.getNote(0).id;
			int distanciaA0 = distanceMatrix[noteId][0];
			int distanciaA7 = distanceMatrix[noteId][7];
			if (distanciaA0 > distanciaA7) {
				newNoteId = Math.round((float) noteId / (float) 2);
			} else {
				newNoteId = Math.round(((7f - noteId) / 2f) + noteId);
			}
			return new NoteSet(new int[] { noteId, newNoteId });
		}
		// regla para ampliar NoteSet de 7 notas.
		else if (baseNoteSet.size() == 7) {
			return new NoteSet();
		}
		// regla para ampliar NoteSet de entre 2 y 6 notas.
		else if (baseNoteSet.size() > 1 && baseNoteSet.size() < 7) {
			int[] candidates = new int[8 - baseNoteSet.size()];
			float[] distances = new float[8 - baseNoteSet.size()];
			// fill candidates[]
			int iCandidates = 0;
			int iNotes = 0;
			int iNoteSet = 0;
			while (iNotes < 8) {
				if (iNoteSet < baseNoteSet.size()
						&& iNotes == baseNoteSet.getNote(iNoteSet).id) {
					iNoteSet++;
				} else {
					candidates[iCandidates] = iNotes;
					iCandidates++;
				}
				iNotes++;
			}
			// fill distances[]
			for (int candidatesIndex = 0; candidatesIndex < candidates.length; candidatesIndex++) {
				int cId = candidates[candidatesIndex];
				int limInferior = -1;
				int limSuperior = 8;
				for (int i = 0; i < baseNoteSet.size(); i++) {
					int nsId = baseNoteSet.getNote(i).id;
					if (nsId > limInferior && nsId < cId) {
						limInferior = nsId;
					}
					if (nsId < limSuperior && nsId > cId) {
						limSuperior = nsId;
					}
				}
				if (limSuperior == 8) {
					distances[candidatesIndex] = distanceMatrix[7][cId];
				} else if (limInferior == -1) {
					distances[candidatesIndex] = distanceMatrix[0][cId];
				} else {
					distances[candidatesIndex] = (distanceMatrix[limInferior][cId] + distanceMatrix[limSuperior][cId]) / 2f;
				}
			}
			// find maximum distance
			float maximumDistance = 0;
			for (int i = 0; i < distances.length; i++) {
				if (distances[i] > maximumDistance) {
					maximumDistance = distances[i];
				}
			}
			// find the candidates with maximun distance
			List<Integer> secondRoundCandidates = new ArrayList<Integer>(30);
			for (int i = 0; i < distances.length; i++) {
				if (distances[i] == maximumDistance) {
					secondRoundCandidates.add(candidates[i]);
				}
			}
			// select a random one
			int randomIndex = (int) (Math.random()
					* secondRoundCandidates.size() - 0.0001f % secondRoundCandidates
					.size());
			newNoteId = secondRoundCandidates.get(randomIndex);
			// create new NoteSet
			int[] notesIds = new int[baseNoteSet.size() + 1];
			for (int i = 0; i < baseNoteSet.size(); i++) {
				notesIds[i] = baseNoteSet.getNote(i).id;
			}
			notesIds[baseNoteSet.size()] = newNoteId;
			return new NoteSet(notesIds);
		}
		// regla para ampliar NoteSet de 8 notas.
		else {
			return baseNoteSet;
		}
	}
}
