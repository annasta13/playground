/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.annas.playground.kotlin.ui.screen.arcore

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.opengl.GLSurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.DefaultLifecycleObserver
import com.annas.playground.R
import com.annas.playground.java.helpers.arcore.DepthSettings
import com.annas.playground.java.helpers.arcore.InstantPlacementSettings
import com.annas.playground.java.helpers.arcore.SnackbarHelper
import com.annas.playground.java.helpers.arcore.TapHelper
import com.annas.playground.kotlin.helper.arcore.ARCoreSessionLifecycleHelper
import com.google.ar.core.Config
import com.google.ar.core.Config.InstantPlacementMode
import com.google.ar.core.Session

/** Contains UI elements for Hello AR. */
class ArView(
    private val activity: Activity,
    private val instantPlacementSettings: InstantPlacementSettings,
    private val arcoreSessionHelper: ARCoreSessionLifecycleHelper
//    private val surfaceView: SurfaceView,
//    private val arSession: () -> Session?
) : DefaultLifecycleObserver {
    private val root = View.inflate(activity, R.layout.activity_arcore, null)
    private val depthSettings = DepthSettings()
    private val surfaceView = root.findViewById<GLSurfaceView>(R.id.surfaceview)
    private val settingsButton = root.findViewById<ImageView>(R.id.settings_button)

    private val session get() = arcoreSessionHelper.session

    init {
        settingsButton.setOnClickListener { v ->
            PopupMenu(activity, v).apply {
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.depth_settings -> launchDepthSettingsMenuDialog()
                        R.id.instant_placement_settings -> launchInstantPlacementSettingsMenuDialog()
                        else -> null
                    } != null
                }
                inflate(R.menu.settings_menu)
                show()
            }
        }
    }

    val snackbarHelper = SnackbarHelper()

    @SuppressLint("ClickableViewAccessibility")
    val tapHelper = TapHelper(activity).also { surfaceView.setOnTouchListener(it) }

    /**
     * Shows a pop-up dialog on the first tap in HelloARRenderer, determining whether the user wants
     * to enable depth-based occlusion. The result of this dialog can be retrieved with
     * DepthSettings.useDepthForOcclusion().
     */
    fun showOcclusionDialogIfNeeded() {
        val session = arcoreSessionHelper.session ?: return
        val isDepthSupported = session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)
        if (!depthSettings.shouldShowDepthEnableDialog() || !isDepthSupported) {
            return // Don't need to show dialog.
        }

        // Asks the user whether they want to use depth-based occlusion.
        AlertDialog.Builder(activity)
            .setTitle(R.string.options_title_with_depth)
            .setMessage(R.string.depth_use_explanation)
            .setPositiveButton(R.string.button_text_enable_depth) { _, _ ->
                depthSettings.setUseDepthForOcclusion(true)
            }
            .setNegativeButton(R.string.button_text_disable_depth) { _, _ ->
                depthSettings.setUseDepthForOcclusion(false)
            }
            .show()
    }

    private fun launchInstantPlacementSettingsMenuDialog() {
        val resources = activity.resources
        val strings = resources.getStringArray(R.array.instant_placement_options_array)
        val checked = booleanArrayOf(instantPlacementSettings.isInstantPlacementEnabled)
        AlertDialog.Builder(activity)
            .setTitle(R.string.options_title_instant_placement)
            .setMultiChoiceItems(strings, checked) { _, which, isChecked ->
                checked[which] = isChecked
            }
            .setPositiveButton(R.string.done) { _, _ ->
                val session = session ?: return@setPositiveButton
                instantPlacementSettings.isInstantPlacementEnabled = checked[0]
                configureSession(session)
            }
            .show()
    }

    /** Shows checkboxes to the user to facilitate toggling of depth-based effects. */
    private fun launchDepthSettingsMenuDialog() {
        val session = session ?: return

        // Shows the dialog to the user.
        val resources: Resources = activity.resources
        val checkboxes =
            booleanArrayOf(
                depthSettings.useDepthForOcclusion(),
                depthSettings.depthColorVisualizationEnabled()
            )
        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            // With depth support, the user can select visualization options.
            val stringArray = resources.getStringArray(R.array.depth_options_array)
            AlertDialog.Builder(activity)
                .setTitle(R.string.options_title_with_depth)
                .setMultiChoiceItems(stringArray, checkboxes) { _, which, isChecked ->
                    checkboxes[which] = isChecked
                }
                .setPositiveButton(R.string.done) { _, _ ->
                    depthSettings.setUseDepthForOcclusion(checkboxes[0])
                    depthSettings.setDepthColorVisualizationEnabled(checkboxes[1])
                }
                .show()
        } else {
            // Without depth support, no settings are available.
            AlertDialog.Builder(activity)
                .setTitle(R.string.options_title_without_depth)
                .setPositiveButton(R.string.done) { _, _ -> /* No settings to apply. */ }
                .show()
        }
    }

    private fun configureSession(session: Session) {
        session.configure(
            session.config.apply {
                lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR

                // Depth API is used if it is configured in Hello AR's settings.
                depthMode =
                    if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        Config.DepthMode.AUTOMATIC
                    } else {
                        Config.DepthMode.DISABLED
                    }

                // Instant Placement is used if it is configured in Hello AR's settings.
                instantPlacementMode =
                    if (instantPlacementSettings.isInstantPlacementEnabled) {
                        InstantPlacementMode.LOCAL_Y_UP
                    } else {
                        InstantPlacementMode.DISABLED
                    }
            }
        )
    }
}
