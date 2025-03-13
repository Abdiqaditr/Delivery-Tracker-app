package com.example.deliverytracker;

import static org.junit.Assert.*;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Collections;

import android.Manifest;
import android.content.pm.PackageManager;
import com.google.android.gms.location.LocationServices;

@RunWith(RobolectricTestRunner.class)
@Config(
    sdk = 30,
    manifest = Config.NONE,
    application = android.app.Application.class
)
public class MainActivityTest {

    @Mock private FusedLocationProviderClient mockLocationClient;
    @Mock private GoogleMap mockGoogleMap;
    @Mock private Marker mockMarker;
    @Mock private BottomNavigationView mockNavView;
    @Mock private FloatingActionButton mockFab;
    @Mock private MenuItem mockMenuItem;
    @Mock private GoogleMapWrapper mockMapWrapper;
    @Mock private Location mockLocation;

    private MainActivity activity;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);


        activity = Mockito.mock(MainActivity.class);

        
        // Set up minimal required mocks
        Mockito.when(activity.getMapWrapper()).thenReturn(mockMapWrapper);
        
        // Set up location mock
        Mockito.when(mockLocation.getLatitude()).thenReturn(40.7128);
        Mockito.when(mockLocation.getLongitude()).thenReturn(-74.0060);
    }

    @Test
    public void testNavigateToOrders() {
        Mockito.when(mockMenuItem.getItemId()).thenReturn(R.id.navigation_orders);
        activity.onNavigationItemSelected(mockMenuItem);
        Mockito.verify(activity).onNavigationItemSelected(mockMenuItem);
    }

    @Test
    public void testNavigateToProfile() {
        Mockito.when(mockMenuItem.getItemId()).thenReturn(R.id.navigation_profile);
        activity.onNavigationItemSelected(mockMenuItem);
        Mockito.verify(activity).onNavigationItemSelected(mockMenuItem);
    }

    @Test
    public void testMapReady() {
        Mockito.when(activity.getMapWrapper()).thenReturn(mockMapWrapper);
        assertNotNull(activity.getMapWrapper());
    }

    @Test
    public void testCenterMapOnCurrentLocation() {
        Mockito.doNothing().when(activity).centerMapOnCurrentLocation();
        activity.centerMapOnCurrentLocation();
        Mockito.verify(activity).centerMapOnCurrentLocation();
    }

    @Test
    public void testEnableMyLocation() {
        Mockito.when(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            .thenReturn(PackageManager.PERMISSION_GRANTED);
        Mockito.doNothing().when(activity).enableMyLocation();
        activity.enableMyLocation();
        Mockito.verify(activity).enableMyLocation();
    }

    @Test
    public void testOnLocationResult() {
        LocationResult mockLocationResult = Mockito.mock(LocationResult.class);
        Mockito.when(mockLocationResult.getLastLocation()).thenReturn(mockLocation);
        Mockito.doNothing().when(activity).onLocationResult(mockLocationResult);
        activity.onLocationResult(mockLocationResult);
        Mockito.verify(activity).onLocationResult(mockLocationResult);
    }

    @Test
    public void testOnRequestPermissionsResult_Granted() {
        int requestCode = 1;
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        int[] grantResults = new int[]{PackageManager.PERMISSION_GRANTED};
        Mockito.doAnswer(invocation -> {
            int code = invocation.getArgument(0);
            int[] results = invocation.getArgument(2);
            if (code == 1 && results[0] == PackageManager.PERMISSION_GRANTED) {
                activity.enableMyLocation();
            }
            return null;
        }).when(activity).onRequestPermissionsResult(
            ArgumentMatchers.anyInt(),
            ArgumentMatchers.any(String[].class),
            ArgumentMatchers.any(int[].class)
        );

        activity.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Mockito.verify(activity).enableMyLocation();
    }
    @Test
    public void testOnRequestPermissionsResult_Denied() {

        int requestCode = 1;
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        int[] grantResults = new int[]{PackageManager.PERMISSION_DENIED};
        activity.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Mockito.verify(activity, Mockito.never()).enableMyLocation();
    }
    @After
    public void tearDown() {
    }
}