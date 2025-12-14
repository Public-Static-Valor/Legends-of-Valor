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
        boolean running = true;

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("Choose a game:");
            System.out.println("[1] Legends: Monsters and Heroes");
            System.out.println("[2] Legends of Valor");
            System.out.println("[3] Quit");

            int choice = -1;
            try {
                System.out.print("Enter choice: ");
                String line = sc.nextLine();
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            if (choice == 3) {
                running = false;
                System.out.println("Goodbye!");
                continue;
            }

            GameInterface game = createGame(choice);

            if (game == null) {
                System.out.println("Invalid choice.");
                continue;
            }

            try {
                // Game initialization loading
                runWithLoading(() -> {
                    // Play game start sound
                    SoundManager.getInstance().playGameStartSound();
                    game.init();
                });
                
                // Small delay to ensure audio system is stable
                try { Thread.sleep(500); } catch (InterruptedException e) {}

                game.start();
            } catch (com.legends.game.QuitGameException e) {
                System.out.println("\n" + e.getMessage());
                System.out.println("Returning to Main Menu...");
            } finally {
                // Stop sounds but don't cleanup yet
                SoundManager.getInstance().stopAllSounds();
            }
        }

        // Cleanup sound resources
        SoundManager.getInstance().cleanup();
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
