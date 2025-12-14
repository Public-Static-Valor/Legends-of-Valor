package com.legends.game;

import java.util.Scanner;

import com.legends.io.ConsoleInput;
import com.legends.io.ConsoleOutput;
import com.legends.utils.audio.SoundManager;

public class GameLauncher {

    public static void launch() {
        // Initial loading (e.g. sounds)
        runWithLoading(() -> {
            SoundManager.getInstance();
        });

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

            // Game initialization loading
            runWithLoading(() -> {
                // Play game start sound
                SoundManager.getInstance().playGameStartSound();
                game.init();
            });

            game.start();
        } catch (com.legends.game.QuitGameException e) {
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

    private static void runWithLoading(Runnable task) {
        Thread loadingThread = new Thread(() -> {
            String[] frames = { "|", "/", "-", "\\" };
            int i = 0;
            System.out.print("Loading ");
            while (!Thread.currentThread().isInterrupted()) {
                System.out.print("\rLoading " + frames[i++ % frames.length]);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.print("\rLoading Complete!   \n");
        });
        loadingThread.start();

        try {
            task.run();
        } finally {
            loadingThread.interrupt();
            try {
                loadingThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
