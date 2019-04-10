package com.example.revendiquons;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.revendiquons.db.AppDatabase;
import com.example.revendiquons.db.repository.PropositionRepository;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.revendiquons", appContext.getPackageName());

        Toast.makeText(appContext, "TEST IS RUNNING !", Toast.LENGTH_SHORT).show();

        AppDatabase.getAppDatabase(appContext).PropositionDao().deletePropositions();
    }
}
