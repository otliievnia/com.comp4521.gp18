package com.example.comp4521;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.comp4521.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SigninTest {

    @Rule
    public ActivityScenarioRule<Landing> mActivityScenarioRule =
            new ActivityScenarioRule<>(Landing.class);

    @Test
    public void signinTest() {
        ViewInteraction button = onView(
allOf(withId(R.id.signInBtn), withText("Sign In"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        button.check(matches(isDisplayed()));
        
        ViewInteraction imageView = onView(
allOf(withId(R.id.landingImage),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        imageView.check(matches(isDisplayed()));
        
        ViewInteraction button2 = onView(
allOf(withId(R.id.signUpBtn), withText("Sign Up"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        button2.check(matches(isDisplayed()));
        
        ViewInteraction materialButton = onView(
allOf(withId(R.id.signInBtn), withText("Sign In"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
0),
isDisplayed()));
        materialButton.perform(click());
        
        ViewInteraction materialButton2 = onView(
allOf(withId(R.id.signInBtn), withText("Sign In"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
5),
isDisplayed()));
        materialButton2.perform(click());
        }
    
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
