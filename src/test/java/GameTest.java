import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameTest {

    // Тест 1: Победа первого игрока (ветка "больше")
    @Test
    public void testFirstPlayerWins() {
        Player petya = new Player(123, "Петя", 120);
        Player vasya = new Player(456, "Вася", 98);
        Game game = new Game();

        game.register(petya);
        game.register(vasya);
        int actual = game.round("Петя", "Вася");

        Assertions.assertEquals(1, actual);
    }

    // Тест 2: Победа второго игрока (ветка "меньше")
    @Test
    public void testSecondPlayerWins() {
        Player petya = new Player(123, "Петя", 98);
        Player vasya = new Player(456, "Вася", 120);
        Game game = new Game();

        game.register(petya);
        game.register(vasya);
        int actual = game.round("Петя", "Вася");

        Assertions.assertEquals(2, actual);
    }

    // Тест 3: Ничья (ветка "равно")
    @Test
    public void testDraw() {
        Player petya = new Player(123, "Петя", 120);
        Player vasya = new Player(456, "Вася", 120);
        Game game = new Game();

        game.register(petya);
        game.register(vasya);
        int actual = game.round("Петя", "Вася");

        Assertions.assertEquals(0, actual);
    }

    // Тест 4: Первый игрок не найден (ветка проверки player1 == null)
    @Test
    public void testFirstPlayerNotFound() {
        Player petya = new Player(123, "Петя", 120);
        Game game = new Game();

        game.register(petya);
        Assertions.assertThrows(NotRegisteredException.class,
                () -> game.round("Жора", "Петя")
        );
    }

    // Тест 5: Второй игрок не найден (ветка проверки player2 == null)
    @Test
    public void testSecondPlayerNotFound() {
        Player petya = new Player(123, "Петя", 120);
        Game game = new Game();

        game.register(petya);
        Assertions.assertThrows(NotRegisteredException.class,
                () -> game.round("Петя", "Жора")
        );
    }

    // Тест 6: Первый игрок найден после второго в цикле (ветка else-if для player2)
    @Test
    public void testFirstPlayerFoundAfterSecondInLoop() {
        Player vasya = new Player(456, "Вася", 98);  // Будет найден первым в цикле
        Player petya = new Player(123, "Петя", 120); // Будет найден вторым в цикле
        Game game = new Game();

        game.register(vasya);  // Вася добавлен первым
        game.register(petya);   // Петя добавлен вторым

        // Ищем "Петя" (будет найден после Васи)
        int actual = game.round("Петя", "Вася");

        Assertions.assertEquals(1, actual); // Петя побеждает
    }

    // Тест 7: Проверка else-if ветки для первого игрока
    @Test
    public void testBothPlayersFoundButSecondCheckInElseIf() {
        Player petya = new Player(123, "Петя", 120);
        Player vasya = new Player(456, "Вася", 98);
        Player anna = new Player(789, "Анна", 150);
        Game game = new Game();

        game.register(anna);   // Анна не участвует в поиске
        game.register(petya);  // Петя
        game.register(vasya);  // Вася

        // При поиске:
        // 1. Анна - не подходит ни под одно условие
        // 2. Петя - подходит под playerName1
        // 3. Вася - подходит под playerName2 в else-if
        int actual = game.round("Петя", "Вася");

        Assertions.assertEquals(1, actual);
    }

    // Тест 8: Оба игрока не зарегистрированы
    @Test
    public void testBothPlayersNotFound() {
        Game game = new Game();  // Пустой список

        Assertions.assertThrows(NotRegisteredException.class,
                () -> game.round("Жора", "Коля")
        );
    }

    // Тест 9: Двойная регистрация одного игрока
    @Test
    public void testDuplicateRegistration() {
        Player petya = new Player(123, "Петя", 120);
        Player vasya = new Player(456, "Вася", 98);
        Game game = new Game();

        game.register(petya);
        game.register(petya);  // Регистрируем Петю дважды
        game.register(vasya);

        // Должен найти первого Петю
        int actual = game.round("Петя", "Вася");

        Assertions.assertEquals(1, actual);
    }

    // Тест 10: Игроки с одинаковыми именами
    @Test
    public void testPlayersWithSameName() {
        Player petya1 = new Player(123, "Петя", 120);
        Player petya2 = new Player(124, "Петя", 50);  // Тоже Петя, но слабее
        Player vasya = new Player(456, "Вася", 98);
        Game game = new Game();

        game.register(petya1);
        game.register(petya2);
        game.register(vasya);

        // Должен найти первого Петю (120 силы)
        int actual = game.round("Петя", "Вася");

        Assertions.assertEquals(2, actual);
    }

    // Тест 11: Игрок с нулевой силой
    @Test
    public void testPlayerWithZeroStrength() {
        Player petya = new Player(123, "Петя", 0);
        Player vasya = new Player(456, "Вася", 120);
        Game game = new Game();

        game.register(petya);
        game.register(vasya);

        int actual = game.round("Петя", "Вася");

        Assertions.assertEquals(2, actual);
    }

    // Тест 12: Пустой список игроков
    @Test
    public void testEmptyPlayerList() {
        Game game = new Game();

        Assertions.assertThrows(NotRegisteredException.class,
                () -> game.round("Петя", "Вася")
        );
    }

    // Тест 13: Проверка корректности сообщения исключения
    @Test
    public void testExceptionMessage() {
        Game game = new Game();
        game.register(new Player(123, "Петя", 120));

        Exception exception = Assertions.assertThrows(
                NotRegisteredException.class,
                () -> game.round("Петя", "НеСуществующий")
        );

        String expectedMessage = "НеСуществующий";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}