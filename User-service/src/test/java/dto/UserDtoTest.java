package dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoTest {
    @Test
    public void toString_checkForCorrectString() {
        UserDto userDto = new UserDto("a", "b", 10);
        String expected = "Name: a, email: b, age: 10";

        String actual = userDto.toString();

        assertEquals(expected, actual);
    }
}
