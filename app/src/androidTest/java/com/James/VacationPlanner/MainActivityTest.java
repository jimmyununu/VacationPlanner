package com.James.VacationPlanner;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.James.VacationPlanner.ui.MainActivity;
import com.James.VacationPlanner.utils.SecurePreferencesHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;



@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private Context context;

    @Before
    public void setUp() {
        // Launch MainActivity and get the context
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> context = activity.getApplicationContext());
    }

    @Test
    public void testSaveVacation_InvalidDateFormat() {
        // Input invalid date format and trigger save
        Espresso.onView(withId(R.id.titleInput)).perform(ViewActions.typeText("Test Vacation"));
        Espresso.onView(withId(R.id.startDateInput)).perform(ViewActions.typeText("invalid-date"));
        Espresso.onView(withId(R.id.endDateInput)).perform(ViewActions.typeText("2024-12-31"));
        Espresso.onView(withId(R.id.saveButton)).perform(ViewActions.click());
    }

    @Test
    public void testSaveVacation_EndDateBeforeStartDate() {
        // Input end date before start date and trigger save
        Espresso.onView(withId(R.id.titleInput)).perform(ViewActions.typeText("Test Vacation"));
        Espresso.onView(withId(R.id.startDateInput)).perform(ViewActions.typeText("2024-12-31"));
        Espresso.onView(withId(R.id.endDateInput)).perform(ViewActions.typeText("2024-01-01"));
        Espresso.onView(withId(R.id.saveButton)).perform(ViewActions.click());
    }

    @Test
    public void testResetPin() {
        // Set a mock PIN
        SecurePreferencesHelper.putString(context, "user_pin", "1234");
        Espresso.onView(withId(R.id.button_reset_pin)).perform(ViewActions.click());

        // if pin is null test is passed
        String pin = SecurePreferencesHelper.getString(context, "user_pin", null);
        assertNull(pin);
    }

    @Test
    public void testBulkAddVacations() throws InterruptedException {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        for (int i = 1; i <= 10; i++) {
            Espresso.onView(withId(R.id.titleInput))
                    .perform(ViewActions.clearText(), ViewActions.typeText("Vacation " + i));
            Espresso.onView(withId(R.id.hotelInput))
                    .perform(ViewActions.clearText(), ViewActions.typeText("Hotel " + i));
            Espresso.onView(withId(R.id.startDateInput))
                    .perform(ViewActions.clearText(), ViewActions.typeText("2024-01-01"));
            Espresso.onView(withId(R.id.endDateInput))
                    .perform(ViewActions.clearText(), ViewActions.typeText("2024-12-31"));
            Espresso.onView(withId(R.id.saveButton)).perform(ViewActions.click());
            // small delay to simulate user speed
            Thread.sleep(500);
        }
        //checks that 10 vacations have been added
        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.vacationRecyclerView);
            int itemCount = recyclerView.getAdapter().getItemCount();
            assertEquals(10, itemCount);
        });


    }




}



