package com.pasukanlangit.id.melijo.presentation.developmentkey

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.razir.progressbutton.hideProgress
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentDevelopmentKeyBinding
import com.pasukanlangit.id.melijo.presentation.AuthActivity
import com.pasukanlangit.id.melijo.presentation.AuthViewModel
import com.pasukanlangit.id.melijo.presentation.safeNavigate
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.setUpToProgressButton
import com.pasukanlangit.id.melijo.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DevelopmentKeyFragment : DialogFragment(R.layout.fragment_development_key) {

    private val binding : FragmentDevelopmentKeyBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogNoClose)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.attributes?.windowAnimations = R.style.dialog_animation_slide
        return dialog

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isSaveSession = viewModel.getSession()

        with(binding){
            btnSubmit.setUpToProgressButton(this@DevelopmentKeyFragment)

            btnSubmit.setOnClickListener {
                val keyValue = edtKey.text.toString().trim()

                if(keyValue.isEmpty()){
                    Toast.makeText(requireContext(), "Key tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.checkKeyIsValid(keyValue).observe(viewLifecycleOwner){
                    when(it){
                        is MyResponse.Success -> {
                            btnSubmit.hideProgress(getString(R.string.submit))

                            if(!isSaveSession){
                                val action = DevelopmentKeyFragmentDirections.actionDevelopmentKeyFragmentToAccountChooserFragment()
                                findNavController().safeNavigate(action)
                            }else{
                                (activity as AuthActivity).navigateToMain(viewModel.accountType)
                            }
                        }
                        is MyResponse.Loading -> {
                            btnSubmit.showLoading()
                        }
                        is MyResponse.Error -> {
                            btnSubmit.hideProgress(getString(R.string.submit))
                            Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


    override fun onCancel(dialog: DialogInterface) {
        activity?.finish()
    }
}