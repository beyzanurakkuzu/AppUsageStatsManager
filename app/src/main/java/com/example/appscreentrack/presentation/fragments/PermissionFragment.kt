package com.example.appscreentrack.presentation.fragments

import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appscreentrack.R
import com.example.appscreentrack.databinding.FragmentPermissionBinding

class PermissionFragment : Fragment() {
    private lateinit var binding: FragmentPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermissionBinding.inflate(inflater, container, false)

        binding.buttonSettings.setOnClickListener {
            openSettings()
        }

        binding.buttonProceed.setOnClickListener {
            showDialogOrProceed()
        }
        return binding.root
    }

    private fun showDialogOrProceed() {
        if (!hasUsageAccessPermission())
            showUsageAccessPermissionDialog()
        else {
            findNavController().navigate(R.id.action_permissionFragment_to_homeFragment)
        }
    }

    private fun showUsageAccessPermissionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission")
            .setMessage(
                "In order to use the app, please grant the usage access permission in settings."
            )
            .setNegativeButton("No") { _, _ ->
                activity?.finish()
            }
            .setPositiveButton("Okay") { _, _ ->
                openSettings()
            }
            .create().show()
    }

    private fun hasUsageAccessPermission(): Boolean {
        val appOpsManager = context?.getSystemService(
            Context.APP_OPS_SERVICE
        ) as AppOpsManager? ?: return false

        val mode = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> appOpsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                requireContext().applicationInfo.uid,
                requireContext().packageName
            )
            else -> appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                requireContext().applicationInfo.uid,
                requireContext().applicationInfo.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }
}