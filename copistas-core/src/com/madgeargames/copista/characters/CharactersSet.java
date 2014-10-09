package com.madgeargames.copista.characters;

import com.madgeargames.copista.sequences.Sequence;

public class CharactersSet {

	public Character[] characters;

	public CharactersSet() {
		characters = new Character[5];
		characters[0] = new Character(
				"Johannes Sebastian Bach",
				new Sequence(new int[] { 1, 2, 3, 4, 5, 6, 7, 0, 4 }),
				"bach",
				"Nació en 1685. Su reputación como organista y clavecinista era legendaria en toda Europa por su gran técnica y capacidad de improvisar música al teclado. Además del órgano y del clavecín, tocaba el violín y la viola de gamba. Su obra es considerada como la cumbre de la música barroca.");
		characters[3] = new Character("StudentR", new Sequence(new int[] { 1, 2, 3, 4, 5, 6, 7, 0,
				4 }), "studentR", "Student right character");
		characters[4] = new Character("StudentL", new Sequence(new int[] { 1, 2, 3, 4, 5, 6, 7, 0,
				4 }), "studentL", "Student left character");
		characters[2] = new Character(
				"Wolfgang Amadeus Mozart",
				new Sequence(new int[] { 1, 2, 3, 4, 5, 6, 7, 0, 4 }),
				"mozart",
				"Nació en 1756. Compositor e intérprete con gran dominio del teclado y el violín, maestro del clasicismo, creó más de 600 obras y abarcó todos los géneros musicales de su época. A la edad de 7 años ya daba conciertos.");
		characters[1] = new Character(
				"Ludwig Van Beethoven",
				new Sequence(new int[] { 1, 2, 3, 4, 5, 6, 7, 0, 4 }),
				"beethoven",
				"Nació en 1770. Compositor, maestro de orquesta y pianista. Sus obras para piano y música de cámara tuvieron un gran impacto y gran influencia. Su padre alcohólico quería que su hijo fuese un genio como Mozart y lo obligó desde muy pequeña edad a aprender música.");
	}

}
