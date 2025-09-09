package com.userservice.console;

import com.userservice.dto.UserDto;
import com.userservice.enums.InputMode;
import com.userservice.enums.ResponseCode;
import lombok.AllArgsConstructor;
import com.userservice.mapper.UserMapper;
import com.userservice.entity.UserEntity;
import com.userservice.service.UserServiceImpl;
import com.userservice.service.UserService;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

@AllArgsConstructor
public class ConsoleUi {
    private final UserService userService = new UserServiceImpl();
    private final Scanner scanner = new Scanner(System.in);
    
    public void launch() {
        InputMode code = InputMode.OTHER;
        while (code != InputMode.EXIT) {
            code = startScreen();
            switch (code) {
                case GET_ALL -> getAll();
                case GET_USER -> getUser();
                case UPDATE -> updateUser();
                case SAVE -> saveUser();
                case DELETE -> deleteUser();
                default -> {}
            }
        }
//        HibernateUtil.shutDown();
    }
    
    private void getAll() {
        InputMode mode = getAllScreen();
        recallInput(mode);
    }
    
    private void getUser() {
        InputMode mode = getUserScreen(getUserInput());
        recallInput(mode);
    }

    private void updateUser() {
        InputMode mode = updateUserScreen(updateUserInput());
        recallInput(mode);
    }

    private void saveUser() {
        InputMode mode = saveUserScreen(saveUserInput());
        recallInput(mode);
    }

    private void deleteUser() {
        InputMode mode = deleteUserScreen(deleteUserInput());
        recallInput(mode);
    }

    private void recallInput(InputMode mode) {
        while (mode != InputMode.EXIT) {
            System.out.println(TextConstants.INVALID_MODE);
            mode = getModeInput();
        }
    }

    private InputMode startScreen() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println(TextConstants.CHOOSE_OPTION);
        for (int i = 0; i < TextConstants.START_OPTIONS.size(); ++i) {
            System.out.println(i + 1 + ": " + TextConstants.START_OPTIONS.get(i));
        }
        System.out.println(TextConstants.EXIT);
        return getModeInput();
    }

    private InputMode getAllScreen() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println("All users' id:");
        userService.getAllUsers().forEach(x -> System.out.println(x.getUuid()));
        System.out.println(TextConstants.CHOOSE_OPTION);
        System.out.println(TextConstants.EXIT);
        return getModeInput();
    }

    private String getUserInput() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println("Enter user's uuid:");
        return scanner.nextLine();
    }

    private InputMode getUserScreen(String uuid) {
        System.out.println(uuid + " user info:");
        UserMapper mapper = new UserMapper();
        UserDto userDto = mapper.userEntityToDto(userService.getUserById(uuid));
        System.out.println(userDto);
        System.out.println(TextConstants.CHOOSE_OPTION);
        System.out.println(TextConstants.EXIT);
        return getModeInput();
    }

    private ResponseCode updateUserInput() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println("Enter updating user's info in the next format\n(id must exist in the database):");
        System.out.println("id, name, surname, email, age");
        String input = scanner.nextLine();
        UserEntity updatedUser = parseInput(input, false);
        return userService.updateUser(updatedUser);
    }

    private InputMode updateUserScreen(ResponseCode code) {
        if (code == ResponseCode.OK) {
            System.out.println("User was updated successfully");
        } else {
            System.out.println("User wasn't updated");
        }
        System.out.println(TextConstants.CHOOSE_OPTION);
        System.out.println(TextConstants.EXIT);
        return getModeInput();
    }

    private ResponseCode saveUserInput() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println("Enter new user's info in the next format:");
        System.out.println("name, surname, email, age");
        String input = "uuid, " + scanner.nextLine();
        UserEntity newUser = parseInput(input, true);
        return userService.saveUser(newUser);
    }

    private InputMode saveUserScreen(ResponseCode code) {
        if (code == ResponseCode.OK) {
            System.out.println("User was saved successfully");
        } else {
            System.out.println("User wasn't saved");
        }
        System.out.println(TextConstants.CHOOSE_OPTION);
        System.out.println(TextConstants.EXIT);
        return getModeInput();
    }

    private ResponseCode deleteUserInput() {
        System.out.println(TextConstants.APP_NAME);
        System.out.println("Enter user's uuid:");
        String uuid = scanner.nextLine();
        UserEntity toDelete = userService.getUserById(uuid);
        return userService.deleteUser(toDelete);
    }

    private InputMode deleteUserScreen(ResponseCode code) {
        if (code == ResponseCode.OK) {
            System.out.println("User was deleted successfully");
        } else {
            System.out.println("User wasn't deleted");
        }
        System.out.println(TextConstants.CHOOSE_OPTION);
        System.out.println(TextConstants.EXIT);
        return getModeInput();
    }

    private UserEntity parseInput(String input, boolean isNewUser) {
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
        UserEntity user = new UserEntity(name, email, age);
        if (isNewUser) {
            return user;
        }
        UserEntity oldUser = userService.getUserById(uuid);
        if (oldUser == null) {
            return null;
        }
        user.setUuid(uuid);
        user.setCreatedAt(oldUser.getCreatedAt());
        return user;
    }

    private InputMode getModeInput() {
        try {
            int input = Integer.parseInt(scanner.nextLine());
            Optional<InputMode> foundMode = Arrays.stream(InputMode.values())
                    .filter(x -> x.getMenuCode() == input)
                    .findAny();
            return foundMode.orElse(InputMode.OTHER);
        } catch (NumberFormatException e) {
            return InputMode.OTHER;
        }
    }
}
