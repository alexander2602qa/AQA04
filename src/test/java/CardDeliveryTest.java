import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

import org.openqa.selenium.Keys;

public class CardDeliveryTest {

    public String setDate(int days) {
        return LocalDate.now()
                .plusDays(days)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void shouldCreateApplicationIfAllDataAreCorrect() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(setDate(7));
        $("[data-test-id='name'] input").setValue("Антон Иванов");
        $("[data-test-id='phone'] input").setValue("+79155554321");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='notification']")
                .shouldHave(cssClass("notification_visible"), Duration.ofSeconds(15));
    }

    @Test
    void shouldNotCreateApplicationIfCityIsEmpty() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(setDate(7));
        $("[data-test-id='name'] input").setValue("Антон Иванов");
        $("[data-test-id='phone'] input").setValue("+79155554321");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='city'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotCreateApplicationIfCityIsNotAccessible() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Мыски");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(setDate(7));
        $("[data-test-id='name'] input").setValue("Антон Иванов");
        $("[data-test-id='phone'] input").setValue("+79155554321");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='city'] .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotCreateApplicationIfDateIsInPast() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(setDate(-7));
        $("[data-test-id='name'] input").setValue("Антон Иванов");
        $("[data-test-id='phone'] input").setValue("+79155554321");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='date'] .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotCreateApplicationIfDateIsInAfter2days() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(setDate(+2));
        $("[data-test-id='name'] input").setValue("Антон Иванов");
        $("[data-test-id='phone'] input").setValue("+79155554321");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='date'] .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotCreateApplicationIfNameInEnglish() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(setDate(+7));
        $("[data-test-id='name'] input").setValue("Anton Ivanov");
        $("[data-test-id='phone'] input").setValue("+79155554321");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='name'] .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotCreateApplicationIfNameIsEmpty() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(setDate(+7));
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("+79155554321");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='name'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotCreateApplicationIfPhoneIsNotCorrect() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(setDate(+7));
        $("[data-test-id='name'] input").setValue("Антон Иванов");
        $("[data-test-id='phone'] input").setValue("79155554321+");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='phone'] .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotCreateApplicationIfPhoneIsEmpty() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(setDate(+7));
        $("[data-test-id='name'] input").setValue("Антон Иванов");
        $("[data-test-id='phone'] input").setValue("");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='phone'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotCreateApplicationIfAgreementInNotClicked() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(setDate(+7));
        $("[data-test-id='name'] input").setValue("Антон Иванов");
        $("[data-test-id='phone'] input").setValue("+79155554321");
        $(".button").click();
        $("[data-test-id='agreement']").shouldHave(cssClass("input_invalid"));
    }

}
