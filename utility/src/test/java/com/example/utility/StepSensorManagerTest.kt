package com.example.utility

import androidx.lifecycle.LifecycleOwner
import com.example.utility.activity_checker.SystemActivityRecognitionCheckerFake
import com.example.utility.sensor.StepSensorListener
import com.example.utility.sensor.StepSensorManager
import com.example.utility.sensor.SystemSensorProviderFake
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue
import org.mockito.junit.MockitoJUnitRunner
import io.mockk.*

@RunWith(MockitoJUnitRunner::class)
class StepSensorManagerTest {
    private val systemSensorProviderFake = SystemSensorProviderFake()
    private val activityRecognitionCheckerFake = SystemActivityRecognitionCheckerFake()
    private val listener = mockk<StepSensorListener>(relaxed = true)
    private val lifecycleOwner = mockk<LifecycleOwner>(relaxed = true)
    private lateinit var stepSensorManager: StepSensorManager

    @Before fun setUp(){
        stepSensorManager =
            StepSensorManager(systemSensorProviderFake, activityRecognitionCheckerFake, listener)
    }

    @Test
    fun `onResume() and recognition granted sets listener`() {
        stepSensorManager.onResume(lifecycleOwner)

        assertTrue(systemSensorProviderFake.registeredListener == listener)

    }

    @Test
    fun `onResume() and recognition not granted does not call onActiveStep or onTotalStep`() {
        activityRecognitionCheckerFake.isGranted = false

        stepSensorManager.onResume(lifecycleOwner)

        verify(exactly = 0) { listener.onTotalStepCountChanged }
    }

    @Test
    fun `onResume() calls onRequestActivityRecognitionPermission when permissions are not granted`() {
        activityRecognitionCheckerFake.isGranted = false
        val callback = mockk<() -> Unit >(relaxed = true)
        stepSensorManager.onRequestActivityRecognitionPermission = callback

        stepSensorManager.onResume(lifecycleOwner)

        verify(exactly = 1) { callback.invoke() }
    }

    @Test
    fun `registerListener correctly sets callbacks`() {
        val callback1 = mockk<() -> Unit>(relaxed = true)
        val callback2 = mockk<(Int) -> Unit>(relaxed = true)
        stepSensorManager.registerListener(callback1, callback2)

        verify { listener.onTotalStepCountChanged = callback2 }
        verify { listener.onActiveStepDetected = callback1 }
    }

    @Test
    fun `Calling unRegister() unregisters listener`() {
        stepSensorManager.unregisterListener()

        assertTrue(systemSensorProviderFake.registeredListener == null)
    }
}