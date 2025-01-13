package com.android_ai.csc13009.app.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.widget.FrameLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Language
import com.android_ai.csc13009.app.utils.adapter.LanguageAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LangBottomFragment(val lang : List<Language>) : BottomSheetDialogFragment() {
    var langSelection : ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialogFragment =  super.onCreateDialog(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.layout_bottom_lang, null)
        bottomSheetDialogFragment.setContentView(view)
        bottomSheetDialogFragment.setOnShowListener {
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet?.background = null // Loại bỏ nền mặc định
            bottomSheet?.setBackgroundResource(R.drawable.dialog_bg) // Áp dụng nền bo góc
        }

        val rcvData = view.findViewById<RecyclerView>(R.id.rvLanguage)
        val adapter = LanguageAdapter(lang)
        val linearLayoutManager = LinearLayoutManager(context)
        rcvData.layoutManager = linearLayoutManager
        rcvData.adapter = adapter
        adapter.onItemClick = {
            langSelection?.invoke(it)
            dismiss()
        }

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rcvData.addItemDecoration(itemDecoration)

        return bottomSheetDialogFragment
    }
}