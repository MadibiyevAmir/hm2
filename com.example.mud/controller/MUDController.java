package com.example.mud.controller;

import com.example.mud.model.Player;
import com.example.mud.model.Room;
import com.example.mud.model.Item;
import java.util.Scanner;

public class MUDController {
    private final Player player;
    private boolean running;
    private final Scanner scanner;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
        this.scanner = new Scanner(System.in);
    }

    public void runGameLoop() {
        System.out.println("Добро пожаловать в MUD! Введите 'help' для списка команд.");
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            handleInput(input);
        }
    }

    private void handleInput(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "look" -> lookAround();
            case "move" -> move(argument);
            case "pick" -> pickUp(argument);
            case "inventory" -> checkInventory();
            case "help" -> showHelp();
            case "exit", "quit" -> running = false;
            default -> System.out.println("Неизвестная команда.");
        }
    }

    private void lookAround() {
        Room room = player.getCurrentRoom();
        System.out.println("Вы находитесь в: " + room.getDescription());
        if (!room.getItems().isEmpty()) {
            System.out.print("Предметы: ");
            for (Item item : room.getItems()) {
                System.out.print(item.getName() + " ");
            }
            System.out.println();
        } else {
            System.out.println("Здесь ничего нет.");
        }
    }

    private void move(String direction) {
        Room nextRoom = player.getCurrentRoom().getExit(direction);
        if (nextRoom != null) {
            player.move(nextRoom);
            System.out.println("Вы переместились в: " + nextRoom.getDescription());
        } else {
            System.out.println("Вы не можете идти в этом направлении!");
        }
    }

    private void pickUp(String itemName) {
        Room room = player.getCurrentRoom();
        Item item = room.takeItem(itemName);
        if (item != null) {
            player.pickUpItem(item);
            System.out.println("Вы подобрали: " + item.getName());
        } else {
            System.out.println("Такого предмета здесь нет!");
        }
    }

    private void checkInventory() {
        if (player.getInventory().isEmpty()) {
            System.out.println("Ваш инвентарь пуст.");
        } else {
            System.out.println("Вы несете:");
            for (Item item : player.getInventory()) {
                System.out.println("- " + item.getName());
            }
        }
    }

    private void showHelp() {
        System.out.println("Доступные команды:");
        System.out.println("look - осмотреть комнату");
        System.out.println("move <направление> - двигаться (forward, back, left, right)");
        System.out.println("pick <предмет> - подобрать предмет");
        System.out.println("inventory - проверить инвентарь");
        System.out.println("help - показать команды");
        System.out.println("exit - выйти из игры");
    }
}
