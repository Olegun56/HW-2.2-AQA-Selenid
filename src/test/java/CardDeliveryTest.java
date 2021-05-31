import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import javax.swing.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class CardDeliveryTest {

    public String localDate(int earlyDate) {
        String inputDate;
        LocalDate date = LocalDate.now().plusDays(earlyDate);
        inputDate = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date);
        return inputDate;


    }

    @BeforeEach
    void setUpAll() {
        open("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        closeWindow();

    }

    @Test
    void shouldInputForm() {
        $("[data-test-id=city] input").setValue("Оренбург");
        $("[data-test-id=date] input").doubleClick();
        //  $("[data-test-id=date] input").clear();
        //  $("[data-test-id=date] input").setValue(localDate(4));
        $("[data-test-id=date] input").sendKeys(Keys.chord(BACK_SPACE, localDate(4)));
        $("[data-test-id=name] input").setValue("Есипов Олег");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(20));
        $("[data-test-id=notification] .notification__content").shouldHave(Condition.exactText("Встреча успешно забронирована на " + localDate(4)));


    }

    @Test
    void shouldInputInvalidCity() {
        $("[data-test-id=city] input").setValue("Orenburg");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.chord(BACK_SPACE, localDate(4)));
        $("[data-test-id=name] input").setValue("Есипов Олег");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        //$("[data-test-id=city] .input__sub").shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
        String text = $(".input_invalid > span > .input__sub").getText();
        assertEquals("Доставка в выбранный город недоступна", text);
    }

    @Test
    void shouldInputInvalidName() {
        $("[data-test-id=city] input").setValue("Оренбург");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.chord(BACK_SPACE, localDate(4)));
        $("[data-test-id=name] input").setValue("Esipov Oleg");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        //  $("[data-test-id=name] .input__sub").shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        String text = $(".input_invalid > span > .input__sub").getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);

    }

    @Test
    void shouldInputInvalidDate() {
        $("[data-test-id=city] input").setValue("Оренбург");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.chord(BACK_SPACE, localDate(2)));
        $("[data-test-id=name] input").setValue("Есипов Олег");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        String text = $(".input_invalid").getText();
        assertEquals("Заказ на выбранную дату невозможен", text);


    }

    @Test
    void shouldInputInvalidAgreement() {
        $("[data-test-id=city] input").setValue("Оренбург");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.chord(BACK_SPACE, localDate(3)));
        $("[data-test-id=name] input").setValue("Есипов Олег");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $(".button__text").click();
        String text = $(".checkbox__text").getCssValue("color");
        assertEquals("rgba(255, 92, 92, 1)", text);


    }

    @Test
    void shouldInputInvalidPhoneNumber() {
        $("[data-test-id=city] input").setValue("Оренбург");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.chord(BACK_SPACE, localDate(3)));
        $("[data-test-id=name] input").setValue("Есипов Олег");
        $("[data-test-id=phone] input").setValue("+7901234567");
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        String text = $(".input_invalid > span > .input__sub").getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }
}