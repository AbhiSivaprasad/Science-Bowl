package com.abhi.android.sciencebowl;

import android.app.Application;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class ApplicationTest extends ApplicationTestCase<Application> {
    private String badEmail = "asdf";
    private String badPassword = ".`.";
    private String email = "bsd405@bs.com";
    private String pass = "123456";

    public ApplicationTest() {
        super(Application.class);
    }

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Test
    public void testGoogle(){
        try {
            onView(withText("SIGN OUT")).perform(click());
        } catch (NoMatchingViewException e) {
        }
        onView(withId(R.id.bGSignIn)).perform(click());
        onView(withId(R.id.play_button)).check(matches(withText("PLAY")));
        onView(withText("SETTINGS")).perform(click());
        int m = (int) (Math.random()*6.0);
        onView(withId(R.id.math_button)).perform(click());
        onView(withId(R.id.difficulty_seekbar)).perform(setProgress(m));
        Espresso.pressBack();
        onView(withText("SIGN OUT")).perform(click());
        onView(withId(R.id.bGSignIn)).perform(click());
        onView(withText("SETTINGS")).perform(click());

        onView(withId(R.id.difficulty_seekbar)).check(matches(withProgress(m)));
    }
    @Test
    public void testFacebook() throws InterruptedException {

        try {
            onView(withText("SIGN OUT")).perform(click());
        } catch (NoMatchingViewException e) {
        }
        onView(withId(R.id.button_facebook_login)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.play_button)).check(matches(withText("PLAY")));
        onView(withText("SETTINGS")).perform(click());
        int m = (int) (Math.random()*6.0);
        onView(withId(R.id.math_button)).perform(click());
        onView(withId(R.id.difficulty_seekbar)).perform(setProgress(m));
        Espresso.pressBack();
        onView(withText("SIGN OUT")).perform(click());
        onView(withId(R.id.button_facebook_login)).perform(click());
        Thread.sleep(2000);
        onView(withText("SETTINGS")).perform(click());
        onView(withId(R.id.difficulty_seekbar)).check(matches(withProgress(m)));
    }

    @Test
    public void testEmailSignUp() throws InterruptedException {

        try {
            onView(withText("SIGN OUT")).perform(click());
        } catch (NoMatchingViewException e) {
        }
        onView(withId(R.id.bSignUp)).perform(click());

        onView(withId(R.id.bRegister)).perform(click());
        onView(withId(R.id.etEmail2)).perform(typeText(badEmail));
        onView(withId(R.id.etPass2)).perform(typeText(badPassword));
        onView(withId(R.id.etEmail2)).perform(clearText());
        onView(withId(R.id.bRegister)).perform(click());
        onView(withId(R.id.etEmail2)).perform(clearText(),typeText(email));
        onView(withId(R.id.bRegister)).perform(click());
        onView(withId(R.id.etPass2)).perform(clearText(), typeText(pass));
        onView(withId(R.id.bRegister)).perform(click());
        onView(withId(R.id.etPassConf)).perform(typeText(pass));
        onView(withText("Register")).perform(click());

        try {
            onView(withId(R.id.play_button)).check(matches(withText("PLAY")));
        }catch (NoMatchingViewException e){

            Espresso.pressBack();
            onView(withId(R.id.bOpenEmail)).perform(click());
            onView(withId(R.id.etEmail)).perform(typeText(email));
            closeSoftKeyboard();
            onView(withId(R.id.etPass)).perform(typeText(pass));
            closeSoftKeyboard();
            onView(withId(R.id.bSignIn)).perform(click());
        }
        onView(withText("SETTINGS")).perform(click());
        int m = (int) (Math.random()*6.0);
        onView(withId(R.id.math_button)).perform(click());
        onView(withId(R.id.difficulty_seekbar)).perform(setProgress(m));
        Espresso.pressBack();
        onView(withText("SIGN OUT")).perform(click());
        onView(withId(R.id.bOpenEmail)).perform(click());
        onView(withId(R.id.etEmail)).perform(typeText(email));
        closeSoftKeyboard();
        onView(withId(R.id.etPass)).perform(typeText(pass));
        closeSoftKeyboard();
        onView(withId(R.id.bSignIn)).perform(click());

        onView(withText("SETTINGS")).perform(click());


        onView(withId(R.id.difficulty_seekbar)).check(matches(withProgress(m)));
    }

    private Matcher<View> withSelected(final Matcher<Boolean> b) {
        return new BoundedMatcher<View, Button>(Button.class) {

            @Override
            public boolean matchesSafely(Button view) {
                boolean m = view.isSelected();
                return b.matches(m);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is selected: ");
                b.describeTo(description);
            }
        };
    }

    public static Matcher<View> withProgress(final int expectedProgress) {
        return new BoundedMatcher<View, SeekBar>(SeekBar.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("expected: ");
                description.appendText(""+expectedProgress);
            }

            @Override
            public boolean matchesSafely(SeekBar seekBar) {
                return seekBar.getProgress() == expectedProgress;
            }
        };
    }

    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                ((SeekBar) view).setProgress(progress);
            }

            @Override
            public String getDescription() {
                return "Set a progress";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }
}