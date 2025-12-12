package com.legends.Game;

import java.util.Scanner;

import com.legends.io.ConsoleInput;
import com.legends.io.ConsoleOutput;

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

            game.init();
            game.start();
        } catch (QuitGameException e) {
            System.out.println("\n" + e.getMessage());
            System.out.println("Goodbye!");
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
