package com.thk.layoutsample2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thk.layoutsample2.databinding.FragmentConstraintBarrierBinding

class ConstraintBarrierFragment : Fragment() {
    private lateinit var binding: FragmentConstraintBarrierBinding

    private var isTextLong = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConstraintBarrierBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn.setOnClickListener {
            binding.text1.text = if (isTextLong) "long text" else "loooooooooooooong text"
            isTextLong = !isTextLong
        }
    }
}