
package UserTests;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import User.User;
import User.UserRepository;
import User.UserService;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import static org.testng.Assert.*;

public class UserServiceTest {
    private UserService userService;

    public UserServiceTest() {
        UserRepository userRepository = new UserRepository();
        this.userService = new UserService(userRepository);
    }

    @Test(priority = 1)
    public void testUpdateUserFromExcel() throws IOException {
        User initialUser = new User(6L, "original_user", "original_password");
        userService.registerUser(initialUser);

        FileInputStream file = new FileInputStream(new File("src/test/resources/User/Users.xlsx"));
        XSSFWorkbook workbook = null;

        try {
            workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(1);
            Long userId = (long) row.getCell(0).getNumericCellValue();
            String newUsername = row.getCell(1).getStringCellValue();
            String newPassword = row.getCell(2).getStringCellValue();

            User updatedUser = new User(userId, newUsername, newPassword);
            boolean updated = userService.updateUser(updatedUser);
            assertTrue(updated, "User should be updated successfully.");

            User foundUser = userService.getUserById(userId);
            assertNotNull(foundUser, "User should be found by ID after update.");
            assertEquals(foundUser.getUsername(), newUsername, "Updated username should match.");
            assertEquals(foundUser.getPassword(), newPassword, "Updated password should match.");
        } finally {
            if (workbook != null) workbook.close();
            file.close();
        }
    }

 


    @Test(priority = 2)
    @Parameters({"username", "password"})
    public void testAuthenticateUserFromXML(@Optional("defaultUsername") String username, 
                                             @Optional("defaultPassword") String password) throws Exception {
        User testUser = new User(1L, username, password);
        userService.registerUser(testUser);

        User authenticatedUser = userService.authenticateUser(username, password);

        assertNotNull(authenticatedUser, "Authenticated user should not be null.");
        assertEquals(authenticatedUser.getUsername(), username, "Authenticated username should match.");
        assertEquals(authenticatedUser.getPassword(), password, "Authenticated password should match.");
    }


}
