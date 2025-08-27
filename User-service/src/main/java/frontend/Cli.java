package frontend;

import backend.HibernateUtil;
import backend.User;
import backend.UserDao;

import java.util.Arrays;
import java.util.Scanner;

public class Cli {
    
    private final UserDao userDao = new UserDao();
    private final Scanner scanner = new Scanner(System.in);
    
    public void appCycle() {
        int code = ModeConstants.OK;
        while (code != ModeConstants.EXIT) {
            code = startScreen();
            switch (code) {
                case ModeConstants.GET_ALL -> getAll();
                case ModeConstants.GET_USER -> getUser();
                case ModeConstants.UPDATE -> updateUser();
                case ModeConstants.SAVE -> saveUser();
                case ModeConstants.DELETE -> deleteUser();
                default -> {}
            }
        }
        HibernateUtil.shutDown();
    }
    
    private void getAll() {
        int code = getAllScreen();
        recallInput(code);
    }
    
    private void getUser() {
        int code = getUserScreen(getUserInput());
        recallInput(code);
    }

    private void updateUser() {
        int code = updateUserScreen(updateUserInput());
        recallInput(code);
    }

    private void saveUser() {
        int code = saveUserScreen(saveUserInput());
        recallInput(code);
    }

    private void deleteUser() {
        int code = deleteUserScreen(deleteUserInput());
        recallInput(code);
    }

    private void recallInput(int code) {
        while (code != ModeConstants.EXIT) {
            System.out.println(TextConstants.INVALID_MODE);
            code = getIntInput();
        }
    }

    private int startScreen() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println(TextConstants.CHOOSE_OPTION);
        for (int i = 0; i < TextConstants.START_OPTIONS.size(); ++i) {
            System.out.println(i + 1 + ": " + TextConstants.START_OPTIONS.get(i));
        }
        System.out.println(TextConstants.EXIT);
        return getIntInput();
    }

    private int getAllScreen() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println("All users' id");
        userDao.getAllUsers().forEach(x -> System.out.println(x.getUuid()));
        System.out.println(TextConstants.CHOOSE_OPTION);
        System.out.println(TextConstants.EXIT);
        return getIntInput();
    }

    private String getUserInput() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println("Enter user's uuid");
        return scanner.nextLine();
    }

    private int getUserScreen(String uuid) {
        System.out.println(uuid + " user info");
        System.out.println(userDao.getUserById(uuid));
        System.out.println(TextConstants.CHOOSE_OPTION);
        System.out.println(TextConstants.EXIT);
        return getIntInput();
    }

    private int updateUserInput() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println("Enter updating user's info in the next format\n(id must exist in the database):");
        System.out.println("id, name, surname, email, age");
        String input = scanner.nextLine();
        User updatedUser = parseInput(input, false);
        if (updatedUser == null) {
            return ModeConstants.ERROR;
        }
        return userDao.updateUser(updatedUser);
    }

    private int updateUserScreen(int code) {
        if (code == ModeConstants.OK) {
            System.out.println("User was updated successfully");
        } else {
            System.out.println("User wasn't updated");
        }
        System.out.println(TextConstants.CHOOSE_OPTION);
        System.out.println(TextConstants.EXIT);
        return getIntInput();
    }

    private int saveUserInput() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println("Enter new user's info in the next format:");
        System.out.println("name, surname, email, age");
        String input = "uuid, " + scanner.nextLine();
        User newUser = parseInput(input, true);
        return userDao.saveUser(newUser);
    }

    private int saveUserScreen(int code) {
        if (code == ModeConstants.OK) {
            System.out.println("User was saved successfully");
        } else {
            System.out.println("User wasn't saved");
        }
        System.out.println(TextConstants.CHOOSE_OPTION);
        System.out.println(TextConstants.EXIT);
        return getIntInput();
    }

    private int deleteUserInput() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println("Enter user's uuid");
        String uuid = scanner.nextLine();
        User toDelete = userDao.getUserById(uuid);
        if (toDelete == null) {
            return ModeConstants.ERROR;
        }
        return userDao.deleteUser(toDelete);
    }

    private int deleteUserScreen(int code) {
        if (code == ModeConstants.OK) {
            System.out.println("User was deleted successfully");
        } else {
            System.out.println("User wasn't deleted");
        }
        System.out.println(TextConstants.CHOOSE_OPTION);
        System.out.println(TextConstants.EXIT);
        return getIntInput();
    }

    private User parseInput(String input, boolean newUser) {
        if (input == null) {
            return null;
        }
        String[] parsed = input.split(", ");
        if (parsed.length != 5 || Arrays.stream(parsed).anyMatch(x -> x == null || x.isEmpty())) {
            return null;
        }
        String uuid = parsed[0];
        String name = parsed[1] + " " + parsed[2];
        String email = parsed[3];
        int age;
        try {
            age = Integer.parseInt(parsed[4]);
        } catch (NumberFormatException e) {
            return null;
        }
        User user = new User(name, email, age);
        if (!newUser) {
            user.setUuid(uuid);
        }
        return user;
    }

    private int getIntInput() {
        int input;
        try {
            input = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            input = ModeConstants.ERROR;
        }
        return input;
    }
}
