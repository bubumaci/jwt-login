package com.beatareka.jwtlogin

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.beatareka.jwtlogin.view.MainActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {
    @get : Rule
    var mActivityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun loginScreenIsVisibleOnCleanRun() {
        onView(withId(R.id.hello_text_view_id)).check(matches(isDisplayed()));
    }

    @Test
    fun loginScreenFillingFieldsEmptyFirst() {
        onView(withId(R.id.hello_text_view_id)).check(matches(isDisplayed()));
        onView(withId(R.id.save_button)).check(matches(isNotEnabled()))
        onView(withId(R.id.username_edit_text_id)).perform(typeText("usr"), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text_id)).perform(typeText("pw"), closeSoftKeyboard())
        onView(withId(R.id.save_button)).check(matches(isEnabled()))
        onView(withId(R.id.save_button)).perform(click())
        onView(withId(R.id.loading_view_id)).check(matches(isDisplayed()))
        onView(withId(R.id.error_message_id)).check(matches(withText("")))

//        todo: idling resources?
//        Thread.sleep(3000)
//        onView(withId(R.id.user_full_name_textview_id)).check(matches(isDisplayed()))
    }
}