package com.madgeargames.copista.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.madgeargames.copista.Copista;
import com.madgeargames.copista.characters.Character;
import com.madgeargames.copista.notes.NoteSet;
import com.madgeargames.copista.notes.NoteSetGenerator;
import com.madgeargames.copista.sequences.Sequence;
import com.madgeargames.copista.sequences.SequenceGenerator;
import com.madgeargames.copista.widgets.EspectroPlayer;
import com.madgeargames.copista.widgets.MarcadorPuntuacion;

public class BattleScreen extends BaseScreen {

	EspectroPlayer espectro;
	MarcadorPuntuacion[] marcadoresPuntuacion = new MarcadorPuntuacion[2];
	int[] lastSequenceMatched = new int[] { -1, -1 };
	Character maestro;
	Actor flowController = new Actor();
	Texts texts = new Texts();
	LifeUpdater lifeUpdater = new LifeUpdater();
	float[] lifesLeft = new float[] { 100, 100 };
	boolean[] inputEnabled = new boolean[] { false, false };
	int[] playerCombo = new int[] { 0, 0 };
	List<List<Integer>> lastTypedSequences = new ArrayList<List<Integer>>();
	boolean[] lastTypedSequenceComplete = new boolean[] { false, false };
	List<Sequence> sequences;
	public static NoteSet currentNoteSet = NoteSetGenerator.generateNoteSet(2);
	static int currentSequenceLenght = 2;
	static float speed = 2;
	int seqIndex = 0;
	static float soundDuration = .8f / speed;
	static float silenceDuration = .4f / speed;
	boolean comesFromLevelUpScreen = false;
	static public boolean resetGame = false;
	static public boolean twoPlayers = true;

	public BattleScreen() {
		lastTypedSequences.add(new ArrayList<Integer>());
		lastTypedSequences.add(new ArrayList<Integer>());
		addElementsToStage();
	}

	private void addElementsToStage() {
		stage.addActor(flowController);
		maestro = Copista.charactersSet.characters[Copista.maestroIndex];
		maestro.big();
		maestro.setCenterPosition(640 / 2, 360 - 75);
		stage.addActor(maestro);
		espectro = new EspectroPlayer();
		stage.addActor(espectro);
		stage.addActor(texts);
		stage.addActor(lifeUpdater);
		marcadoresPuntuacion[0] = new MarcadorPuntuacion();
		marcadoresPuntuacion[0].setX(640 - marcadoresPuntuacion[0].getWidth());
		stage.addActor(marcadoresPuntuacion[0]);
		marcadoresPuntuacion[1] = new MarcadorPuntuacion();
		if (twoPlayers) {
			stage.addActor(marcadoresPuntuacion[1]);
		}
	}

	private void startBattle() {
		sequences = SequenceGenerator
				.generateSequences(currentNoteSet, currentSequenceLenght, 1000);
		playNextSequence();
	}

	private void playNextSequence() {
		// modificaciones visuales en maestro y personajes.
		maestro.highlight();
		texts.showPlayer(1, false);
		texts.showPlayer(2, false);
		int lastSequenceMatchedCombination;
		if (twoPlayers) {
			lastSequenceMatchedCombination = 0;
			if (lastSequenceMatched[0] == 1 && lastSequenceMatched[1] == 1) {
				lastSequenceMatchedCombination = 1;
			} else if (lastSequenceMatched[0] == 0 || lastSequenceMatched[1] == 0) {
				lastSequenceMatchedCombination = 0;
			}
		} else {
			lastSequenceMatchedCombination = lastSequenceMatched[0];
		}
		switch (lastSequenceMatchedCombination) {
		case -1:
			maestro.moodBase();
			break;
		case 0:
			maestro.moodSad();
			break;
		case 1:
			maestro.moodHappy();
		}
		// reproducir secuencia, habilitar entrada y modificaciones visuales
		espectro.showNotes(sequences.get(seqIndex), soundDuration, silenceDuration, currentNoteSet);
		System.out.println("Reproduciendo secuencia y esperando "
				+ sequences.get(seqIndex).sequence.size() * (soundDuration + silenceDuration)
				+ " segundos.");
		flowController.addAction(Actions.sequence(
				Actions.delay(sequences.get(seqIndex).sequence.size()
						* (soundDuration + silenceDuration)), Actions.run(new Runnable() {

					@Override
					public void run() {
						System.out.println("Habilitando teclado al jugador 0");
						inputEnabled[0] = true;
						if (twoPlayers) {
							inputEnabled[1] = true;
						}
						texts.showPlayer(1, true);
						if (twoPlayers) {
							texts.showPlayer(2, true);
						}
					}
				})));
		// detener barras de vida
		lifeUpdater.enable(false, 0);
		lifeUpdater.enable(false, 1);
	}

	@Override
	protected void onPressKey(int playerId, int keyIndex) {
		System.out.println("Tecla pulsada");
		if (inputEnabled[playerId] && keyIndex < currentNoteSet.size()) {
			System.out.println("Es el jugador " + playerId + " y tiene el teclado habilitado");
			lastTypedSequences.get(playerId).add(keyIndex);
			if (lastTypedSequences.get(playerId).size() == sequences.get(seqIndex).sequence.size()) {
				System.out.println("Secuencia completa escrita, deshabilitando teclado");
				inputEnabled[playerId] = false;
				texts.showPlayer(playerId + 1, false);
				lastTypedSequenceComplete[playerId] = true;
				boolean match = checkKeySequenceMatch(lastTypedSequences.get(playerId),
						sequences.get(seqIndex).sequence);
				// comprobar si coinciden la secuencia escrita con la
				// reproducida
				if (match) {
					System.out.println("Las secuencias coinciden.");
					lastSequenceMatched[playerId] = 1;
					playerCombo[playerId]++;
					lifeUpdater.enable(true, (playerId + 1) % 2);
					if (playerCombo[playerId] == 4) {
						// siguiente nivel
						System.out.println("Siguiente nivel");
						comesFromLevelUpScreen = true;
						if (twoPlayers) {
							LevelUpScreen.setPlayerId((playerId + 1) % 2);
						} else {
							LevelUpScreen.setPlayerId(0);
						}
						Copista.getInstance().setScreen(Copista.getInstance().levelUpScreen);
					} else {
						// continuar
						System.out.println("Continuamos una ronda más en el mismo nivel");
						lastTypedSequences.get(playerId).clear();
						if (twoPlayers && lastTypedSequenceComplete[(playerId + 1) % 2] == true
								|| !twoPlayers) {
							seqIndex++;
							lastTypedSequenceComplete = new boolean[] { false, false };
							playNextSequence();
						}
					}
				} else {
					System.out.println("Las secuencias no coinciden");
					lastSequenceMatched[playerId] = 0;
					lifesLeft[playerId] -= 25;
					marcadoresPuntuacion[playerId].setValue(lifesLeft[playerId]);
					playerCombo[playerId] = 0;
					if (lifesLeft[playerId] == 0) {
						// game over
						System.out.println("Game Over");
						Copista.getInstance().setScreen(Copista.getInstance().gameOverScreen);
					} else {
						// continuar
						System.out.println("Continuamos una ronda más en el mismo nivel");
						lastTypedSequences.get(playerId).clear();
						if (twoPlayers && lastTypedSequenceComplete[(playerId + 1) % 2] == true
								|| !twoPlayers) {
							seqIndex++;
							lastTypedSequenceComplete = new boolean[] { false, false };
							playNextSequence();
						}
					}
				}
			}
		}
	}

	private boolean checkKeySequenceMatch(List<Integer> keySequence, List<Integer> sequence) {
		boolean match = true;
		int[] keyNotesSequence = new int[keySequence.size()];
		for (int i = 0; i < keyNotesSequence.length; i++) {
			keyNotesSequence[i] = currentNoteSet.getNote(keySequence.get(i)).id;
		}
		for (int i = 0; i < keyNotesSequence.length; i++) {
			if (keyNotesSequence[i] != sequence.get(i)) {
				match = false;
				break;
			}
		}
		return match;
	}

	public static void increaseSequenceLenght() {
		System.out.println("increaseSequenceLenght()");
		updateGameOverScreenScores();
		currentSequenceLenght++;
	}

	public static void increaseNoteSet() {
		System.out.println("increaseNoteSet()");
		updateGameOverScreenScores();
		currentNoteSet = NoteSetGenerator.increaseNoteSet(currentNoteSet);
	}

	public static void increaseSpeed() {
		System.out.println("increaseSpeed()");
		updateGameOverScreenScores();
		speed += .2f;
		soundDuration = .8f / speed;
		silenceDuration = .4f / speed;
	}

	private static void updateGameOverScreenScores() {
		GameOverScreen.setPitchIdentificationPoints(currentNoteSet.size());
		GameOverScreen.setSequenceMemoryPoints(currentSequenceLenght);
		GameOverScreen.setSpeedPoints((int) ((speed - 2) * 10 / 2));
	}

	@Override
	public void show() {
		if (comesFromLevelUpScreen) {
			comesFromLevelUpScreen = false;
			lastSequenceMatched = new int[] { -1, -1 };
			lastTypedSequences.get(0).clear();
			lastTypedSequences.get(1).clear();
			lastTypedSequenceComplete = new boolean[] { false, false };
			System.out.println("Generando secuencias con noteset de tamaño "
					+ currentNoteSet.size() + " y longitud " + currentSequenceLenght);
			sequences = SequenceGenerator.generateSequences(currentNoteSet, currentSequenceLenght,
					1000);
			playerCombo[0] = 0;
			playerCombo[1] = 0;
			seqIndex = 0;
			playNextSequence();
		}
		if (resetGame) {
			resetGame = false;
			lifesLeft = new float[] { 100, 100 };
			inputEnabled = new boolean[] { false, false };
			playerCombo = new int[] { 0, 0 };
			lastTypedSequences = new ArrayList<List<Integer>>();
			lastTypedSequences.add(new ArrayList<Integer>());
			lastTypedSequences.add(new ArrayList<Integer>());
			lastTypedSequenceComplete = new boolean[] { false, false };
			currentNoteSet = NoteSetGenerator.generateNoteSet(2);
			currentSequenceLenght = 2;
			speed = 2;
			seqIndex = 0;
			soundDuration = .8f / speed;
			silenceDuration = .4f / speed;
			comesFromLevelUpScreen = false;
			lastSequenceMatched = new int[] { -1, -1 };
			stage.clear();
			addElementsToStage();
			startBattle();
		}
		Copista.charactersSet.characters[Copista.maestroIndex].stopMusic();
		super.show();
	}

	@Override
	protected void onPressBack() {
		Copista.getInstance().setScreen(Copista.getInstance().gameModeScreen);
	}

	private class Texts extends Actor {
		BitmapFont font;
		boolean showPlayer1 = false;
		boolean showPlayer2 = false;
		String[] playerkeys = new String[] { "", "" };

		public Texts() {
			font = Copista.font;
			font.setColor(Color.BLACK);
			font.setScale(.67f);
		}

		void showPlayer(int player, boolean show) {
			System.out.println("Poniendo player " + player + " a " + show);
			if (player == 1) {
				showPlayer1 = show;
			} else if (player == 2) {
				showPlayer2 = show;
			}
			System.out.println("Show player1 " + showPlayer1 + " Show player2 " + showPlayer2);
		}

		@Override
		public void act(float delta) {
			for (int j = 0; j < playerkeys.length; j++) {
				playerkeys[j] = "( ";
				for (int i = 0; i < currentNoteSet.size(); i++) {
					if (j == 0)
						playerkeys[j] += Copista.p0keysName[i] + " ";
					else
						playerkeys[j] += Copista.p1keysName[i] + " ";
				}
				playerkeys[j] += ")";
			}

			super.act(delta);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			if (showPlayer1) {
				font.drawMultiLine(batch, "PLAYER 1", 500, 100, 100, HAlignment.CENTER);
				font.drawMultiLine(batch, playerkeys[0], 500, 50, 100, HAlignment.CENTER);
			}
			if (showPlayer2) {
				font.drawMultiLine(batch, "PLAYER 2", 38, 100, 100, HAlignment.CENTER);
				font.drawMultiLine(batch, playerkeys[1], 38, 50, 100, HAlignment.CENTER);
			}

		}

	}

	private class LifeUpdater extends Actor {
		private final boolean enabled[] = new boolean[] { false, false };

		public void enable(boolean enable, int player) {
			this.enabled[player] = enable;
		}

		@Override
		public void act(float delta) {
			if (enabled[0]) {
				lifesLeft[0] -= delta * 5;
				marcadoresPuntuacion[0].setValue(lifesLeft[0]);
				if (lifesLeft[0] < 0) {
					// game over
					System.out.println("Game Over");
					GameOverScreen.setWinner(2);
					Copista.getInstance().setScreen(Copista.getInstance().gameOverScreen);
				}
			}
			if (enabled[1]) {
				lifesLeft[1] -= delta * 5;
				marcadoresPuntuacion[1].setValue(lifesLeft[1]);
				if (lifesLeft[1] < 0) {
					// game over
					System.out.println("Game Over");
					GameOverScreen.setWinner(1);
					Copista.getInstance().setScreen(Copista.getInstance().gameOverScreen);
				}
			}
			super.act(delta);
		}
	}
}
