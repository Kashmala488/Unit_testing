
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.*;

import User.User;
import User.UserRepository;

import static org.testng.Assert.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryTest {
    private UserRepository userRepository;

    @BeforeMethod
    public void setUp() {
        userRepository = new UserRepository();
    }

    @Test(priority = 1)
    public void testSaveUser() {
        User user = new User(1L, "john_doe", "password123");
        userRepository.save(user);

        User foundUser = userRepository.findById(1L);
        assertNotNull(foundUser, "User should be found by ID.");
        assertEquals(foundUser.getUsername(), "john_doe", "Username should match.");
    }

    @Test(priority = 2)
    public void testFindById() {
        User user = new User(2L, "jane_doe", "securepassword");
        userRepository.save(user);

        User foundUser = userRepository.findById(2L);
        assertNotNull(foundUser, "User should be found by ID.");
        assertEquals(foundUser.getUsername(), "jane_doe", "Username should match.");
    }

    @Test(priority = 3)
    public void testDeleteUser() {
        User user = new User(3L, "mark_smith", "password456");
        userRepository.save(user);

        boolean deleted = userRepository.delete(3L);
        assertTrue(deleted, "User should be deleted successfully.");
        assertNull(userRepository.findById(3L), "User should not be found after deletion.");
    }

    @Test(priority = 4)
    public void testFindAllUsers() {
        User user1 = new User(4L, "alice", "password");
        User user2 = new User(5L, "bob", "password");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> allUsers = userRepository.findAll();
        assertEquals(allUsers.size(), 2, "Should return two users.");
    }

    @Test(priority = 5)
    public void testExistsByUsername() {
        User user = new User(6L, "charlie", "pass");
        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("charlie");
        assertTrue(exists, "User should exist by username.");

        exists = userRepository.existsByUsername("non_existing_user");
        assertFalse(exists, "User should not exist for non-existing username.");
    }

    @Test(priority = 6)
    public void testAddUsersFromExcel() throws IOException {
        FileInputStream file = new FileInputStream(new File("src/test/resources/User/Users.xlsx"));
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            List<User> users = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Long userId = (long) row.getCell(0).getNumericCellValue();
                String username = row.getCell(1).getStringCellValue();
                String password = row.getCell(2).getStringCellValue();
                users.add(new User(userId, username, password));
            }

            for (User user : users) {
                userRepository.save(user);
            }

            for (User user : users) {
                assertEquals(userRepository.findById(user.getId()), user, "User should be found by ID.");
            }
        } finally {
            if (workbook != null) workbook.close();
            file.close();
        }
    }

    @Test(priority = 7)
    @Parameters({"userId", "username", "password"})
    public void testAddUserFromXML(@Optional("7") Long userId, @Optional("xmlUser") String username, @Optional("xmlPass") String password) {
        User user = new User(userId, username, password);
        userRepository.save(user);
        
        User foundUser = userRepository.findById(userId);
        assertNotNull(foundUser, "User should be found by ID from XML parameters.");
        assertEquals(foundUser.getUsername(), username, "Username should match from XML parameters.");
    }
}
