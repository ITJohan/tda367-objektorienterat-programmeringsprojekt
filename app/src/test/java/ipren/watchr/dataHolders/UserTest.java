package ipren.watchr.dataHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ipren.watchr.R;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class UserTest {
    Context appContext;

    //Needed to create bitmaps
    @Before
    public void setUp() {
        appContext = ApplicationProvider.getApplicationContext();
    }
    //Testing all constructors
    @Test
    public void getUserName() {
        User user = new User("expected - David");
        assertTrue(user.getUserName().equals("expected - David"));
        assertFalse(user.getUserName().equals("invalid"));
        user = new User();
        assertTrue(user.getUserName().equals("No user name"));
        Bitmap testBitMap = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.ic_no_user_photo_24px);
        user = new User("expected - Fred", testBitMap);
        assertTrue(user.getUserName().equals("expected - Fred"));
        assertFalse(user.getUserName().equals("invalid"));
    }
    //Testing all constructors
    @Test
    public void getUserProfilePicture() {
        User user = new User("Not used");
        assertNull(user.getUserProfilePicture());
        user = new User();
        assertNull(user.getUserProfilePicture());
        Bitmap testBitMap = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.ic_no_user_photo_24px);
        user = new User("Not used", testBitMap);
        assertTrue(user.getUserProfilePicture().equals(testBitMap));
    }
}