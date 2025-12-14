package com.legends.Game;

import java.util.Scanner;

import com.legends.io.ConsoleInput;
import com.legends.io.ConsoleOutput;
import com.legends.utils.audio.SoundManager;

public class GameLauncher {

    public static void launch() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose a game:");
        System.out.println("[1] Legends: Monsters and Heroes");
        System.out.println("[2] Legends of Valor");

        int choice = sc.nextInt();

        try {
            GameInterface game = createGame(choice);

            if (game == null) {
                System.out.println("Invalid choice. Exiting...");
                sc.close();
                return;
            }

            // Play game start sound
            SoundManager.getInstance().playGameStartSound();

            game.init();
            game.start();
        } catch (QuitGameException e) {
            System.out.println("\n" + e.getMessage());
            System.out.println("Goodbye!");
        } finally {
            // Cleanup sound resources
            SoundManager.getInstance().cleanup();
        }

        sc.close();
    }

    private static GameInterface createGame(int choice) {
        switch (choice) {
            case 1:
                return new GameMonstersAndHeroes(new ConsoleInput(), new ConsoleOutput());
            case 2:
                return new GameValor(new ConsoleInput(), new ConsoleOutput());
            default:
                return null;
        }
    }
}
